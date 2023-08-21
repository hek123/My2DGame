package game.object;

import game.player.Player;
import game.visual.StillEntity;
import game.visual.MovingEntity;

import java.awt.*;

import static game.main.GamePanel.*;


public abstract class SuperObject extends StillEntity implements InteractiveObject {
    /**
     * resource folder containing object images
     */
    protected static String imageFolder = "/object/";

    protected SuperObject() {
        this(new Rectangle(0, 0, tileSize, tileSize));
    }
    protected SuperObject(Rectangle bBox) {
        super(bBox);
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
