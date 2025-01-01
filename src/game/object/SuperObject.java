package game.object;

import Utility.ImageAnchor;
import Utility.Vector2D;
import game.player.Player;
import game.visual.StillEntity;
import game.visual.MovingEntity;

import java.awt.*;
import java.awt.image.BufferedImage;

import static game.main.GamePanel.*;


public abstract class SuperObject extends StillEntity implements InteractiveObject {
    /**
     * resource folder containing object images
     */
    protected static final String imageFolder = "/object/";

    protected SuperObject(BufferedImage image) {
        this(new ImageAnchor(image));
    }
    @Deprecated
    protected SuperObject(ImageAnchor imageAnchor) {
        this(new Rectangle(tileSize, tileSize), imageAnchor);
        assert imageAnchor.anchor().x == 0 && imageAnchor.anchor().y == 0;
        assert imageAnchor.image().getWidth() == tileSize && imageAnchor.image().getHeight() == tileSize;
    }
    protected SuperObject(Rectangle bBox, ImageAnchor imageAnchor) {
        super(bBox, imageAnchor);
        background = true;
    }

    @Override
    public void getObject(MovingEntity entity) {
        if (entity instanceof Player player) {
            getObject(player);
        }
    }

    protected abstract void getObject(Player player);
}
