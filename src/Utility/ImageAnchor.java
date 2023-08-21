package Utility;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Structure containing an image and it's anchor (default (0, 0))
 */
public record ImageAnchor(BufferedImage image, Vector2D anchor) {
    public ImageAnchor(BufferedImage image) {
        this(image, new Vector2D());
    }

    public Rectangle getBBox() {
        return new Rectangle(anchor.x, anchor.y, image.getWidth(), image.getHeight());
    }
}
