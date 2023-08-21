//package game.visual;
//
//import Utility.ImageAnchor;
//import Utility.Vector2D;
//import game.character.Direction;
//import game.tile.TileManager;
//import org.jetbrains.annotations.NotNull;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.Deque;
//import java.util.Map;
//import java.util.function.Supplier;
//
//import static game.main.GamePanel.FPS;
//import static game.main.GamePanel.tileSize;
//
///**
// * Implements and extends Entity for moving entities
// */
//public abstract class MovingEntity2 extends Entity {
//    static private final Stroke nextBBoxStroke = new BasicStroke(1);
//
//    // Position & Movement
//    private final Rectangle bBox;
//    /**
//     * the current position, in double precision to support non integer speed
//     */
//    private double x, y;
//    /**
//     * flag indicating if the entity is moving
//     */
//    public boolean moving;
//    /**
//     * the current moving speed of the entity
//     */
//    public double currentSpeed;
//    /**
//     * the current direction in which the entity moves, expressed in radians [0, 2*PI)
//     */
//    public double direction = 0;
//
//    /**
//     * List with targets with which the entity has to collide
//     */
//    private final ArrayList<Rectangle> collisionTargets = new ArrayList<>();
//
//    // Graphics
//    protected int spriteState = 0;
//    protected final int nbSprites;
//    protected int spriteCounter = 0, currentSprite = 0;
//    protected int spriteUpdatePeriod = (int) Math.round(.15 * FPS);
//
//    // DebugInfo
//    private final Deque<Rectangle> targetBox = new ArrayDeque<>();
//
//    protected MovingEntity2(@NotNull Rectangle solidArea, int nbSprites) {
//        checkBBox(solidArea);
//        bBox = solidArea;
//        this.nbSprites = nbSprites;
//    }
//
//    @Override
//    public Rectangle getBBox() {
//        return new Rectangle((int) x + bBox.x, (int) y + bBox.y, bBox.width, bBox.height);
//    }
//
//    @Override
//    public final int getX() {
//        return (int) x;
//    }
//
//    @Override
//    public final int getY() {
//        return (int) y;
//    }
//
//    @Override
//    public final void setPosition(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }
//
//    /**
//     *
//     * @return true if the entity is moving
//     */
//    public final boolean isMoving() {
//        return moving & currentSpeed > 0;
//    }
//
//    /**
//     * returns the bounding box of the entity if the entity would move according to its direction and currentSpeed
//     * @return bounding box
//     */
//    public final Rectangle getNextBBox() {
//        double[] xy = polarToCarthesian(currentSpeed * tileSize / FPS, direction);
//        return new Rectangle((int)(x + xy[0]) + bBox.x, (int)(y + xy[1]) + bBox.y, bBox.width, bBox.height);
//    }
//
//    void addCollisionTarget(Rectangle target) {
//        collisionTargets.add(target);
//    }
//
//    Direction getDirection() {
//        int dir = (int) Math.round(direction * 4 / (2 * Math.PI));
//        return switch (dir % 4) {
//            case 0 -> Direction.RIGHT;
//            case 1 -> Direction.DOWN;
//            case 2 -> Direction.LEFT;
//            case 3 -> Direction.UP;
//            default -> throw new RuntimeException();
//        };
//    }
//-
//    /**
//     * moves the sprite in its current direction until it touches the closest target and sets the currentSpeed equal to zero
//     */
//    private void collide() {
//        assert  !collisionTargets.isEmpty();
//        currentSpeed = 0;
//        targetBox.addAll(collisionTargets);
//
//        setPosition(getX(), getY());
//
//        Supplier<Boolean> hasCollided = () -> {
//            Rectangle nextBBox = getBBox(direction, 1);
//            for (Rectangle target : collisionTargets) {
//                if (target.intersects(nextBBox))
//                    return true;
//            }
//            return false;
//        };
//
//        while (!hasCollided.get()) {
//            move(direction, 1);
//        }
//
//        collisionTargets.clear();
//    }
//
//    /**
//     * translates the entity over a distance of &lt;step&gt;, in the direction &lt;direction&gt;
//     * @param direction direction in which to move
//     * @param step distance over which to move in pixels
//     */
//    private void move(double direction, double step) {
//        double[] xy = polarToCarthesian(step, direction);
//        x += xy[0];
//        y += xy[1];
//    }
//
//    protected final void move() {
//        CollisionChecker.checkTile(this);
//        CollisionChecker.checkEntity(this);
//        if (moving) {
//            if (collisionTargets.isEmpty())
//                move(direction, currentSpeed * tileSize / FPS);
//            else
//                collide();
//        }
//    }
//
//    /**
//     * The action to be taken if the entity collides with another entity
//     * @param entity entity to interact with
//     */
//    protected abstract void entityInteraction(Entity entity);
//
//    /**
//     * The entity actions and behavior are to be defined here, called before move()
//     */
//    protected abstract void action();
//
//    protected void invisibleAction() {};
//
//    /**
//     * This method is called every game loop iteration before the entity is drawn
//     */
//    public final void update() {
//        if (TileManager.visible.intersects(getBBox())) {
//            // character specific action
//            action();
//
//            // move
//            move();
//        } else {
//            invisibleAction();
//        }
//    }
//
//    @Override
//    protected void drawDebugInfo(Graphics2D g2d, Vector2D framePos) {
//        super.drawDebugInfo(g2d, framePos);
//
//        // nextBBox
//        g2d.setColor(Color.yellow);
//        g2d.setStroke(nextBBoxStroke);
//        Rectangle bBox = getNextBBox();
//        g2d.drawRect(bBox.x - framePos.x, bBox.y - framePos.y, bBox.width, bBox.height);
//
//        // collisions
//        bBox = targetBox.poll();
//        while (bBox != null) {
//            g2d.setColor(Color.white);
//            bBox.translate(-framePos.x, -framePos.y);
//            g2d.drawRect(bBox.x, bBox.y, bBox.width, bBox.height);
//
//            bBox = targetBox.poll();
//        }
//    }
//
//    // ### UTILITY ###
//    /**
//     * updates the spriteCounter, use to create animations
//     */
//    public final void updateSpriteCounter() {
//        if (moving) {
//            spriteCounter++;
//            if (spriteCounter == spriteUpdatePeriod) {
//                spriteCounter = 0;
//                currentSprite++;
//                currentSprite %= nbSprites;
//            }
//        } else {
//            currentSprite = 0;
//        }
//    }
//
//    protected abstract BufferedImage getSpriteImage(int state, int sprite, @NotNull Direction direction);
//
//    @Override
//    protected ImageAnchor getVisibleImage() {
//        ImageAnchor out = new ImageAnchor(getSpriteImage(spriteState, currentSprite, direction), new Vector2D(0, 0));
//        switch (direction) {
//            case UP -> out.anchor().y = out.image().getHeight() - tileSize;
//            case LEFT -> out.anchor().x = out.image().getWidth() - tileSize;
//        }
//        return out;
//    }
//
//    // UTILITY
//    /**
//     * converts Direction into spriteIndex
//     * @param direction direction
//     * @return sprite index 0-3
//     */
//    static public int dirToInt(@NotNull Direction direction) {
//        switch (direction) {
//            case DOWN -> {
//                return 0;
//            }
//            case RIGHT -> {
//                return 3;
//            }
//            case LEFT -> {
//                return 2;
//            }
//            case UP -> {
//                return 1;
//            }
//            default -> {
//                return -1;
//            }
//        }
//    }
//
//    static public double[] polarToCarthesian(double r, double theta) {
//        return new double[]{r * Math.cos(theta), r * Math.sin(theta)};
//    }
//
//    static int angleToInt(double direction) {
//        return (int) Math.round(direction * 4 / (2 * Math.PI));
//    }
//}
