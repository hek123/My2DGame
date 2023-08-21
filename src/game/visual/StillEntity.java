package game.visual;

import Utility.ImageAnchor;
import Utility.Vector2D;
import game.visual.animations.Animation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Implementation of Entity for entities that do not move
 */
public abstract class StillEntity extends Entity {
    private final Rectangle bBox;
    private int x, y;

    protected StillEntity(@NotNull Rectangle solidArea) {
        checkBBox(solidArea);
        bBox = solidArea;
        animation = new ShowObject(getImage());
    }

    @Override
    public final Rectangle getBBox() {
        return new Rectangle(x + bBox.x, y + bBox.y, bBox.width, bBox.height);
    }

    @Override
    public final int getX() {
        return x;
    }

    @Override
    public final int getY() {
        return y;
    }

    @Override
    public final void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract ImageAnchor getImage();


    protected class ShowObject extends EntityAnimation {
        private final ImageAnchor imageAnchor;

        public ShowObject(ImageAnchor imageAnchor) {
            this.imageAnchor = imageAnchor;
        }

        @Override
        public @NotNull Animation updateA() {
            return animation;
        }

        @Override
        public void drawA(Graphics2D g2d, Vector2D framePos) {
            drawImage(g2d, framePos, imageAnchor);
        }
    }

}
