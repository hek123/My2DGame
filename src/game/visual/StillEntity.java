package game.visual;

import Utility.ImageAnchor;
import Utility.Vector2D;

import java.awt.*;

/**
 * Implementation of Entity for entities that do not move
 */
public abstract class StillEntity extends Entity {
    private final ImageAnchor imageAnchor;

    protected StillEntity(Rectangle solidArea, ImageAnchor imageAnchor) {
        super(solidArea);
        this.imageAnchor = imageAnchor;
    }

    @Override
    public void updateA() {}

    @Override
    public void drawA(Graphics2D g2d, Vector2D framePos) {
            drawImage(g2d, framePos, imageAnchor);
        }
}
