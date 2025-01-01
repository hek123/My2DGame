package game.visual;

import Utility.ImageAnchor;
import Utility.Vector2D;
import game.character.Character;
import game.main.DebugInfo;
import game.visual.animations.Animation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static game.main.GamePanel.*;

/**
 * Baseclass for all entities (characters & objects).
 */
public abstract class Entity implements Animation, DebugInfo {
    static public void createEntity(int x, int y, Class<? extends Entity> eClass, Object... initArgs) {
        try {
            Object[] args = new Object[initArgs.length + 2];
            args[0] = x;
            args[1] = y;
            System.arraycopy(initArgs, 0, args, 2, initArgs.length);
            Class<?>[] classes = new Class<?>[initArgs.length + 2];
            classes[0] = classes[1] = int.class;
            for (int i = 2; i < args.length; i++)
                classes[i] = args[i].getClass();
            eClass.getDeclaredConstructor(classes).newInstance(args);
        } catch (Exception e1) {
            try {
                Class<?>[] classes = new Class<?>[initArgs.length];
                for (int i = 0; i < initArgs.length; i++)
                    classes[i] = initArgs[i].getClass();
                Entity entity = eClass.getDeclaredConstructor(classes).newInstance(initArgs);
                entity.setPosition(x, y);
                if (entity instanceof MovingEntity movingEntity)
                    game.entityManager.addMovingEntityToMap(movingEntity);
                else
                    game.entityManager.addEntityToMap(entity);
            } catch (Exception e2) {
                System.out.println("E1:");
                e1.printStackTrace();
                System.out.println("E2:");
                e2.printStackTrace();
                throw new RuntimeException("Failed to initialize instance of class " + eClass.getName());
            }
        }
    }

    /**
     * true if entity is always displayed on the background
     */
    public boolean background;
    public boolean removeIfInvisible = false;

    protected double x, y;
    public final Rectangle bBox;

    /**
     *
     * @param bBox relative coordinates of the bounding box.
     */
    protected Entity(@NotNull Rectangle bBox) {
        // Check bounding box
        assert !bBox.isEmpty();
        this.bBox = bBox;
    }

    /**
     *
     * @return the absolute position of the bounding box.
     */
    public final Rectangle getBBox() {
        return new Rectangle((int)x + bBox.x, (int)y + bBox.y, bBox.width, bBox.height);
    }

    public abstract boolean isSolid();

    /**
     *
     * @return X coordinate of the current position
     */
    public final int getX() {
        return (int) x;
    }

    /**
     *
     * @return Y coordinate of the current position
     */
    public final int getY() {
        return (int) y;
    }

    /**
     * Sets the current position to (x, y)
     * @param x X coordinate
     * @param y Y coordinate
     */
    public final void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D getCenterPos() {
        return new Vector2D((int) getBBox().getCenterX(), (int) getBBox().getCenterY());
    }

    public void attackedBy(Character character) {}

    public final void drawImage(Graphics2D g2d, Vector2D framePos, ImageAnchor imageAnchor) {
        g2d.drawImage(imageAnchor.image(), getX() - imageAnchor.anchor().x - framePos.x, getY() - imageAnchor.anchor().y - framePos.y, null);
    }

    private final Stroke bBoxStroke = new BasicStroke(2);
    private final Color bBoxColor = Color.RED;

    @Override
    public void drawDebugInfo(Graphics2D g2d, Vector2D framePos) {
        // draw bbox
        g2d.setStroke(bBoxStroke);
        g2d.setColor(bBoxColor);
        Rectangle bBox = getBBox();
        bBox.translate(-framePos.x, -framePos.y);
        g2d.drawRect(bBox.x, bBox.y, bBox.width, bBox.height);

        // draw anchor used for the position of the entity
        g2d.setColor(Color.red);
        g2d.fillOval(getX() - framePos.x - 3, getY() - framePos.y - 3, 6, 6);
    }

    @Override
    public final int getLayerLevel() {
        if (background) return getY() - game.tileManager.tileMap.getHeight() * tileSize;
        else return getY();
    }

    @Override
    public final boolean isVisible(Rectangle screenBounds) {
        return screenBounds.intersects(getBBox());
    }
}
