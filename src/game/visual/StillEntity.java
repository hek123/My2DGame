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
    protected StillEntity(Rectangle solidArea) {
        super(solidArea);
        animation = new ShowObject(getImage());
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
