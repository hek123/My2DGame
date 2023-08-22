package game.visual;

import Utility.ImageAnchor;
import Utility.Vector2D;
import game.character.Direction;
import game.tile.TileManager;
import game.visual.animations.Animation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.function.Supplier;

import static game.main.GamePanel.*;

/**
 * Implements and extends Entity for moving entities
 */
public abstract class MovingEntity extends Entity {
    static private final Stroke nextBBoxStroke = new BasicStroke(1);

    // Position & Movement
    private final Rectangle bBox;
    /**
     * the current position, in double precision to support non integer speed
     */
    private double x, y;
    /**
     * The current speed in x and y direction
     */
    private double speed, direction;
    /**
     * flag indicating if the entity is moving
     */
    public boolean moving;
    /**
     * the current moving speed of the entity
     */
//    protected double currentSpeed;
    /**
     * the current direction in which the entity moves
     */
//    public @NotNull Direction direction = Direction.DOWN;

    /**
     * List with targets with which the entity has to collide
     */
    private final ArrayList<Rectangle> collisionTargets = new ArrayList<>();

    // DebugInfo
    private final Deque<Rectangle> targetBox = new ArrayDeque<>();

    protected MovingEntity(@NotNull Rectangle solidArea) {
        checkBBox(solidArea);
        bBox = solidArea;
    }

    @Override
    public Rectangle getBBox() {
        return new Rectangle((int) x + bBox.x, (int) y + bBox.y, bBox.width, bBox.height);
    }

    @Override
    public final int getX() {
        return (int) x;
    }

    @Override
    public final int getY() {
        return (int) y;
    }

    @Override
    public final void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return true if the entity is moving
     */
    public final boolean isMoving() {
        return moving && speed > 0;
    }

    public final double getVx() {
        return speed * Math.cos(direction);
    }
    public final double getVy() {
        return speed * Math.sin(direction);
    }

    public final Direction getDirection() {
        direction %= Math.TAU;
        System.out.println(direction * 180 / Math.PI);
        int div = (int) Math.round(direction / (Math.PI / 2));
        System.out.println(div);
        Direction out = switch (div) {
            case 0 -> Direction.RIGHT;
            case 1 -> Direction.DOWN;
            case 2 -> Direction.LEFT;
            case 3 -> Direction.UP;
            default -> throw new RuntimeException(Integer.toString(div));
        };
        return out;
    }
    public final void setDirection(Direction direction) {
        double speed = getCurrentSpeed();
        this.direction = switch (direction) {
            case RIGHT -> 0.;
            case DOWN -> Math.PI / 2;
            case LEFT -> Math.PI;
            case UP -> 3 * Math.PI / 2;
        };
    }
    public final void setDirection(double direction) {
        this.direction = direction;
    }
    public final double getCurrentSpeed() {
        return speed;
    }
    public final void setCurrentSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * returns the bounding box of the entity if the entity would move according to its direction and currentSpeed
     * @return bounding box
     */
    public Rectangle getNextBBox() {
        return getBBox(getVx(), getVx());
    }

//    private Rectangle getBBox(Direction direction, double step) {
//        return switch (direction) {
//            case UP -> new Rectangle((int) x + bBox.x, (int) (y - step) + bBox.y, bBox.width, bBox.height);
//            case RIGHT -> new Rectangle((int) (x + step) + bBox.x, (int) y + bBox.y, bBox.width, bBox.height);
//            case DOWN -> new Rectangle((int) x + bBox.x, (int) (y + step) + bBox.y, bBox.width, bBox.height);
//            case LEFT -> new Rectangle((int) (x - step) + bBox.x, (int) y + bBox.y, bBox.width, bBox.height);
//        };
//    }
    private Rectangle getBBox(double dx, double dy) {
        return new Rectangle((int)(x + dx) + bBox.x, (int)(y + dy) + bBox.y, bBox.width, bBox.height);
    }

    void addCollisionTarget(Rectangle target) {
        collisionTargets.add(target);
    }

    /**
     * moves the sprite in its current direction until it touches the closest target and sets the currentSpeed equal to zero
     */
    private void collide() {
        assert  !collisionTargets.isEmpty();
        double vx_n = Math.cos(direction), vy_n = Math.sin(direction);
        speed = 0;

        targetBox.addAll(collisionTargets);

        setPosition(getX(), getY());

        Supplier<Boolean> hasCollided = () -> {
            Rectangle nextBBox = getBBox(vx_n, vy_n);
            for (Rectangle target : collisionTargets) {
                if (target.intersects(nextBBox))
                    return true;
            }
            return false;
        };

        while (!hasCollided.get()) {
            move(vx_n, vy_n);
        }

        collide(collisionTargets);

        collisionTargets.clear();
    }

    protected void collide(ArrayList<Rectangle> collisionTargets) {}

//    /**
//     * translates the entity over a distance of &lt;step&gt;, in the direction &lt;direction&gt;
//     * @param direction direction in which to move
//     * @param step distance over which to move in pixels
//     */
//    private void move(Direction direction, double step) {
//        switch (direction) {
//            case UP -> y -= step;
//            case DOWN -> y += step;
//            case LEFT -> x -= step;
//            case RIGHT -> x += step;
//        }
//    }
    /**
     * translates the entity over dx, dy;
     * @param dx step in x direction
     * @param dy step in y direction
     */
    private void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    protected final void move() {
        game.entityManager.collisionChecker.checkTile(this);
        game.entityManager.collisionChecker.checkEntity(this);
        if (moving) {
            if (collisionTargets.isEmpty())
                move(getVx() * tileSize / FPS, getVy() * tileSize / FPS);
            else
                collide();
        }
    }

    /**
     * The action to be taken if the entity collides with another entity
     * @param entity entity to interact with
     */
    protected abstract void entityInteraction(Entity entity);

    /**
     * The entity actions and behavior are to be defined here, called before move()
     */
    protected abstract void action();

    protected void invisibleAction() {};

    /**
     * This method is called every game loop iteration before the entity is drawn
     */
    public final void update() {
        if (TileManager.visible.intersects(getBBox())) {
            // character specific action
            action();

            // move
            move();
        } else {
            invisibleAction();
        }
    }

    @Override
    public void drawDebugInfo(Graphics2D g2d, Vector2D framePos) {
        super.drawDebugInfo(g2d, framePos);

        // nextBBox
        g2d.setColor(Color.yellow);
        g2d.setStroke(nextBBoxStroke);
        Rectangle bBox = getNextBBox();
        g2d.drawRect(bBox.x - framePos.x, bBox.y - framePos.y, bBox.width, bBox.height);

        // collisions
        bBox = targetBox.poll();
        while (bBox != null) {
            g2d.setColor(Color.white);
            bBox.translate(-framePos.x, -framePos.y);
            g2d.drawRect(bBox.x, bBox.y, bBox.width, bBox.height);

            bBox = targetBox.poll();
        }
    }


    public class MovingSprite extends EntityAnimation {
        private final ImageAnchor[][] spriteImages;

        protected int spriteCounter = 0, currentSprite = 0;
        public int spriteUpdatePeriod;
        protected final int nbSprites;

        public MovingSprite(ImageAnchor[][] spriteImages, double spriteUpdatePeriod, int nbSprites) {
            assert spriteImages.length == 4;
            assert spriteImages[0].length == nbSprites;
            assert spriteUpdatePeriod > 0;

            this.spriteImages = spriteImages;
            this.spriteUpdatePeriod = (int) Math.round(spriteUpdatePeriod * FPS);
            this.nbSprites = nbSprites;
        }

        @Override
        public @NotNull Animation updateA() {
            if (moving) {
                spriteCounter++;
                if (spriteCounter == spriteUpdatePeriod) {
                    spriteCounter = 0;
                    currentSprite++;
                    currentSprite %= nbSprites;
                }
            } else {
                currentSprite = 0;
            }
            return animation;
        }

        @Override
        public void drawA(Graphics2D g2d, Vector2D framePos) {
            drawImage(g2d, framePos, spriteImages[MovingEntity.dirToInt(getDirection())][currentSprite]);
        }
    }


    // ### UTILS ###

    static public void fixAttackingSpriteAnchor(ImageAnchor[][] spriteImages, int nbSprites) {
        for (int i = 0; i < nbSprites; i++) {
            spriteImages[dirToInt(Direction.UP)][i].anchor().y = spriteImages[dirToInt(Direction.UP)][i].image().getHeight() - tileSize;
            spriteImages[dirToInt(Direction.LEFT)][i].anchor().x = spriteImages[dirToInt(Direction.LEFT)][i].image().getWidth() - tileSize;
        }
    }

    /**
     * converts Direction into spriteIndex
     * @param direction direction
     * @return sprite index 0-3
     */
    static public int dirToInt(@NotNull Direction direction) {
        return switch (direction) {
            case DOWN -> 0;
            case RIGHT -> 3;
            case LEFT -> 2;
            case UP -> 1;
        };
    }
}
