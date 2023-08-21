package game.object;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import Utility.Vector2D;
import game.player.Player;

import static game.main.GamePanel.game;


public class ObjectBoots extends SuperObject {
    static private final ImageAnchor image = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "boots.png"), new Vector2D(0, 0));

    @Override
    public ImageAnchor getImage() {
        return image;
    }

    @Override
    public void getObject(Player player) {
        game.entityManager.removeEntityFromMap(this);
        player.speed += 2;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
