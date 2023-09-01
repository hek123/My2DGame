package game.visual;

import Utility.DataStructures.Graph;
import Utility.Vector2D;
import game.main.GamePanel;
import game.player.Player;
import game.tile.Tile;
import game.tile.TileManager;
import game.visual.animations.Animation;
import org.jetbrains.annotations.NotNull;

import static game.main.GamePanel.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EntityManager {
    private final ArrayList<Animation> animations = new ArrayList<>();
    private final ArrayList<MovingEntity> movingEntities = new ArrayList<>();
    private final ArrayList<Entity> entities = new ArrayList<>();

    public final CollisionChecker collisionChecker = new CollisionChecker();

    private boolean animationUpdateLock;
    private final ArrayList<Animation> animationsToRemove = new ArrayList<>();
    private final ArrayList<Animation> animationsToAdd = new ArrayList<>();

    public void updateMovingEntities() {
        for (MovingEntity entity: getMovingEntityArray()) {
            entity.update();
        }
    }

    synchronized public void updateAnimations() {
        assert animations.size() == new HashSet<>(animations).size();

        animationUpdateLock = true;
        animations.forEach(Animation::updateA);
        animationUpdateLock = false;

        animations.removeAll(animationsToRemove);
        animationsToRemove.clear();
        animations.addAll(animationsToAdd);
        animationsToAdd.clear();
    }

    public void drawAnimations(Graphics2D g2d, Vector2D framePos) {
        for (Animation animation: getSortedAnimationsArray()) {
            if (animation.isVisible(TileManager.visible))
                animation.drawA(g2d, framePos);
            else if (animation instanceof Entity entity) {
                if (entity.removeIfInvisible) {
                    if (entity instanceof MovingEntity movingEntity) removeMovingEntityFromMap(movingEntity);
                    else removeEntityFromMap(entity);
                }
            }
        }
    }

    public void drawEntityDebugInfo(Graphics2D g2d, Vector2D framePos) {
        for (Entity entity: getEntityArray()) {
            entity.drawDebugInfo(g2d, framePos);
        }
    }

    synchronized private Animation[] getSortedAnimationsArray() {
        animations.sort(Comparator.comparingInt(Animation::getLayerLevel));
        return animations.toArray(new Animation[0]);
    }
    synchronized private MovingEntity[] getMovingEntityArray() {
        return movingEntities.toArray(new MovingEntity[0]);
    }
    synchronized private Entity[] getEntityArray() {return entities.toArray(new Entity[0]);}

    synchronized public void addAnimationToMap(@NotNull Animation animation) {
        assert !animations.contains(animation);
        if (animationUpdateLock) animationsToAdd.add(animation);
        else animations.add(animation);
    }
    synchronized public void addMovingEntityToMap(MovingEntity entity) {
        addEntityToMap(entity);
        assert !movingEntities.contains(entity);
        movingEntities.add(entity);
    }
    synchronized public void addEntityToMap(Entity entity) {
        addAnimationToMap(entity);
        assert !entities.contains(entity);
        entities.add(entity);
    }

    synchronized public void removeAnimationFromMap(@NotNull Animation animation) {
        if (animationUpdateLock) animationsToRemove.add(animation);
        else if (!animations.remove(animation)) throw new AssertionError();
    }
    synchronized public void removeMovingEntityFromMap(MovingEntity entity) {
        removeEntityFromMap(entity);
        if (!movingEntities.remove(entity)) throw new AssertionError();
    }
    synchronized public void removeEntityFromMap(Entity entity) {
        removeAnimationFromMap(entity);
        if (!entities.remove(entity)) throw new AssertionError();
    }

    synchronized public void clearMap() {
        animations.clear();
        entities.clear();
        movingEntities.clear();
    }

    public class CollisionChecker implements Graph<Vector2D> {
        void checkTile(MovingEntity entity) {
            if (entity.isMoving()) {
                Rectangle bBox = entity.getNextBBox();
                int x1 = bBox.x / GamePanel.tileSize;
                int x2 = (bBox.x + bBox.width - 1) / GamePanel.tileSize;
                int y1 = bBox.y / GamePanel.tileSize;
                int y2 = (bBox.y + bBox.height - 1) / GamePanel.tileSize;

                int[][] corners = {{x1, y1}, {x1, y2}, {x2, y1}, {x2, y2}};

                for (int i = 0; i < 4; i++) {
                    Tile tile = game.tileManager.tileMap.getTile(corners[i][0], corners[i][1]);
                    Rectangle tileBounds = new Rectangle(corners[i][0] * GamePanel.tileSize, corners[i][1] * GamePanel.tileSize, GamePanel.tileSize, GamePanel.tileSize);

                    if (bBox.intersects(tileBounds) && tile.solid) {
                        entity.addCollisionTarget(tileBounds);
                    }
                    if (tile.collisionInteraction != null) {
                        tile.collisionInteraction.entityInteraction(entity, corners[i][0], corners[i][1]);
                    }
                }
            }
        }

        void checkEntity(MovingEntity character) {
            Rectangle characterBox = character.getNextBBox();

            if (character.isMoving()) {
                for (Entity entity : getEntityArray()) {
                    if (!character.collisionExceptions.contains(entity)) {
                        Rectangle entityBox = entity.getBBox();
                        if (characterBox.intersects(entityBox)) {
                            if (entity.isSolid()) {
                                character.addCollisionTarget(entityBox);
                            }
                            character.entityInteraction(entity);
                        }
                    }
                }
            }
        }

        public void checkAttack(Player character) {
            Rectangle attackBox = character.getAttackRange();
            attackBox.translate(character.getX(), character.getY());

            for (Entity entity : getEntityArray()) {
                if (character != entity && attackBox.intersects(entity.getBBox())) {
                    character.attack(entity);
                }
            }
        }

        public boolean checkTile(StillEntity entity) {
            Rectangle bBox = entity.getBBox();
            int x1 = bBox.x / GamePanel.tileSize;
            int x2 = (bBox.x + bBox.width - 1) / GamePanel.tileSize;
            int y1 = bBox.y / GamePanel.tileSize;
            int y2 = (bBox.y + bBox.height - 1) / GamePanel.tileSize;

            int[][] corners = {{x1, y1}, {x1, y2}, {x2, y1}, {x2, y2}};

            for (int i = 0; i < 4; i++) {
                Tile tile = game.tileManager.tileMap.getTile(corners[i][0], corners[i][1]);
                Rectangle tileBounds = new Rectangle(corners[i][0] * GamePanel.tileSize, corners[i][1] * GamePanel.tileSize, GamePanel.tileSize, GamePanel.tileSize);

                if (bBox.intersects(tileBounds) && tile.solid) {
                    return true;
                }
            }
            return false;
        }

        public List<Vector2D> getNeighbours(Vector2D pos) {
            List<Vector2D> neighbours = new ArrayList<>(4);

            for (int i = 0; i < 4; i++) {
                Vector2D neighbour = Vector2D.add(pos, Vector2D.directions[i]);
                if (!checkSolid(neighbour.x, neighbour.y))
                    neighbours.add(neighbour);
            }
            return neighbours;
        }

        private boolean checkSolid(int x, int y) {
            boolean solid = game.tileManager.tileMap.getTile(x, y).isSolid();
            if (solid) return true;

            for (Entity entity: entities) {
                if (entity.getBBox().contains(x, y) && entity.isSolid()) return true;
            }

            return false;
        }
    }

}
