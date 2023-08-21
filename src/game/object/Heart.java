package game.object;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import game.player.Player;

import static game.main.GamePanel.*;

public class Heart extends SuperObject {
    private static final ImageAnchor image = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "heart_full.png"));

    public Heart() {
        removeIfInvisible = true;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    protected void getObject(Player player) {
        game.entityManager.removeEntityFromMap(this);
        player.receiveHealth(2);
    }

    @Override
    public ImageAnchor getImage() {
        return image;
    }
}
