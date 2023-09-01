package game.visual;

import Utility.ImageAnchor;
import Utility.Vector2D;
import game.character.Direction;
import game.tile.TileManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.function.Supplier;

import static game.main.GamePanel.*;

/**
 * Implements and extends Entity for moving entities
 */
public abstract class MovingEntity extends Entity {
    static private final Stroke nextBBoxStroke = new BasicStroke(1);

    // Position & Movement
    private double speed, direction;
    /**
     * flag indicating if the entity is moving
     */
    public boolean moving;
    static public final double RIGHT = 0., DOWN = Math.PI / 2, LEFT = Math.PI, UP = 3 * Math.PI / 2;

    /**
     * List with targets with which the entity has to collide
     */
    private final ArrayList<Rectangle> collisionTargets = new ArrayList<>();
    public final HashSet<Entity> collisionExceptions = new HashSet<>();

    // DebugInfo
    private final Deque<Rectangle> targetBox = new ArrayDeque<>();

    protected MovingEntity(@NotNull Rectangle solidArea) {
        super(solidArea);
        collisionExceptions.add(this);
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
//        System.out.println(direction * 180 / Math.PI);
        int div = (int) Math.round(direction / (Math.PI / 2));
//        System.out.println(div);
        return switch (div) {
            case 0 -> Direction.RIGHT;
            case 1 -> Direction.DOWN;
            case 2 -> Direction.LEFT;
            case 3 -> Direction.UP;
            default -> throw new ArithmeticException(Integer.toString(div));
        };
    }
    public final double getExactDirection() {
        return direction;
    }

    public final void setDirection(Direction direction) {
        this.direction = switch (direction) {
            case RIGHT -> RIGHT;
            case DOWN -> DOWN;
            case LEFT -> LEFT;
            case UP -> UP;
        };
    }
    public final void setDirection(double direction) {
        this.direction = direction % Math.TAU;
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
    public final Rectangle getNextBBox() {
        return getBBox(getVx() * tileSize / FPS, getVy() * tileSize / FPS);
    }

    protected final Rectangle getBBox(double dx, double dy) {
        return new Rectangle((int)(x + dx) + bBox.x, (int)(y + dy) + bBox.y, bBox.width, bBox.height);
    }

    final void addCollisionTarget(Rectangle target) {
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

    /**
     * translates the entity over dx, dy;
     * @param dx step in x direction
     * @param dy step in y direction
     */
    protected void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    private void move() {
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

    protected void invisibleAction() {}

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

    protected ImageAnchor[][] spriteImages;

    protected int spriteCounter = 0, currentSprite = 0;
    private int spriteUpdatePeriod;
    protected int nbSprites = -1;

    public final void setSpriteUpdatePeriod(double updatePeriod) {
        spriteUpdatePeriod = (int) Math.round(updatePeriod * FPS);
    }

    @Override
    public void updateA() {
        updateSpriteCounter();
    }
    protected void updateSpriteCounter() {
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
    }

    @Override
    public void drawA(Graphics2D g2d, Vector2D framePos) {
        drawSprite(g2d, framePos);
    }
    protected void drawSprite(Graphics2D g2d, Vector2D framePos) {
        drawImage(g2d, framePos, spriteImages[MovingEntity.dirToInt(getDirection())][currentSprite]);
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
            case RIGHT -> 0;
            case DOWN -> 1;
            case LEFT -> 2;
            case UP -> 3;
        };
    }
    static public int dirToInt(double direction) {
        assert 0 <= direction && direction < Math.TAU;
        return (int) Math.round(direction / (Math.PI / 2));
    }
}
