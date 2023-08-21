package game.object.weapon;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import Utility.Vector2D;
import game.item.Item;
import game.object.SuperObject;
import game.player.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

import static game.main.GamePanel.game;

public class Sword extends SuperObject implements Weapon {
    static private final ImageAnchor image = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "sword_normal.png"), new Vector2D(0, 0));
    static private final BufferedImage icon = UtilityTool.scaleImage(image.image(), Item.imageSize, Item.imageSize);

    public Sword() {
        super();
    }

    @Override
    public @NotNull String getName() {
        return "Iron Sword";
    }

    @Override
    protected void getObject(Player player) {
        if (player.inventory.putItem(this))
            game.entityManager.removeEntityFromMap(this);
        else
            game.ui.scrollingMessage("Inventory is full");
    }

    @Override
    public ImageAnchor getImage() {
        return image;
    }

    @Override
    public BufferedImage getItemImage() {
        return icon;
    }


    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public int getDamage() {
        return 1;
    }
}
