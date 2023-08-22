package game.object;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import game.player.Player;
import game.item.Item;

import java.awt.image.BufferedImage;

import static game.main.GamePanel.game;

public class ObjectKey extends SuperObject implements Item {
    static private final ImageAnchor image = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "key.png"));
    static private final BufferedImage itemImage = UtilityTool.scaleImage(image.image(), Item.imageSize, Item.imageSize);

    private int msgTimer = 0;

    @Override
    public ImageAnchor getImage() {
        return image;
    }

    @Override
    public void getObject(Player player) {
        if (player.inventory.putItem(this)) {
            game.entityManager.removeEntityFromMap(this);
            game.ui.scrollingMessage("get Key");
        } else {
            if (msgTimer == 0) {
                game.ui.dialogue("inventory is full", this.getBBox(), player.getDirection());
            } else if (msgTimer >= 30) {
                msgTimer = 0;
            }
            msgTimer++;
        }
    }

    @Override
    public BufferedImage getItemImage() {
        return itemImage;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
