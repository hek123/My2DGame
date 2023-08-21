package game.object;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import game.player.Player;

import static game.main.GamePanel.*;


public class Coin extends SuperObject {
    static private final ImageAnchor image = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "coin_bronze.png"));

    public Coin() {
        removeIfInvisible = true;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    protected void getObject(Player player) {
        player.coin++;
        game.entityManager.removeEntityFromMap(this);
    }

    @Override
    public ImageAnchor getImage() {
        return image;
    }
}
