package game.object.weapon;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import game.item.Item;
import game.object.SuperObject;
import game.player.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

import static game.main.GamePanel.game;

public class Axe extends SuperObject implements Weapon {
    static final private ImageAnchor image = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "axe.png"));
    static final private BufferedImage itemImage = UtilityTool.scaleImage(image.image(), Item.imageSize, Item.imageSize);

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public @NotNull String getName() {
        return "Axe";
    }

    @Override
    public BufferedImage getItemImage() {
        return itemImage;
    }

    @Override
    protected void getObject(Player player) {
        if (player.inventory.putItem(this))
            game.entityManager.removeEntityFromMap(this);
        else
            game.ui.scrollingMessage("Inventory is full");
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public ImageAnchor getImage() {
        return image;
    }
}
