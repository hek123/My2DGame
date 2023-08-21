package game.ui;

import Utility.UtilityTool;
import game.object.shield.Shield;
import game.object.weapon.Weapon;
import game.ui.itemUI.InventoryUI;
import game.ui.itemUI.ItemGrabHandler;
import game.ui.itemUI.ItemSlotUI;

import java.awt.*;
import java.awt.image.BufferedImage;

import static game.main.GamePanel.*;


public class InventoryScreen extends subWindow implements Scalable {
    private final int slotSize = tileSize;

    private final PlayerInventory items;
    private final WeaponSlot weaponSlot;
    private final ShieldSlot shieldSlot;

    public InventoryScreen(ItemGrabHandler itemGrabHandler) {
        scalableList.add(this);

        background = new Color(200, 200, 200);
        borderColor = new Color(150, 150, 150);
        stroke = new BasicStroke(6);
        arc = 10;
        borderArc = 10;

        x = tileSize / 2;
        y = tileSize / 2;

        items = new PlayerInventory();
        itemGrabHandler.addContainer(items);
        weaponSlot = new WeaponSlot();
        itemGrabHandler.addContainer(weaponSlot);
        shieldSlot = new ShieldSlot();
        itemGrabHandler.addContainer(shieldSlot);
    }

    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        items.draw(g2d);
        weaponSlot.draw(g2d);
        shieldSlot.draw(g2d);
    }

    @Override
    public void set(Dimension dimension) {
        width = dimension.width - tileSize;
        height = dimension.height - 3 * tileSize / 2;

        weaponSlot.slotBounds.x = dimension.width / 2 + tileSize / 2;
        weaponSlot.slotBounds.y = 3 * tileSize / 4;

        shieldSlot.slotBounds.x = weaponSlot.slotBounds.x + tileSize;
        shieldSlot.slotBounds.y = weaponSlot.slotBounds.y;
    }

    private class PlayerInventory extends InventoryUI {
        public PlayerInventory() {
            super(game.player.inventory);
        }

        @Override
        protected Rectangle getBBox(Dimension dimension) {
            return new Rectangle(
                    tileSize / 2 + slotSize / 4,
                    tileSize / 2 + slotSize / 4,
                    (dimension.width - tileSize) / 2,
                    (dimension.height - 2 * tileSize)
            );
        }
    }

    private static class WeaponSlot extends ItemSlotUI<Weapon> {
        static private final BufferedImage image = UtilityTool.loadScaledImage("/inventory/SwordSlot.png");

        WeaponSlot() {
            super(game.player.weapon);
        }

        @Override
        protected BufferedImage getItemSlotImage() {
            return image;
        }
    }

    private static class ShieldSlot extends ItemSlotUI<Shield> {
        static private final BufferedImage image = UtilityTool.loadScaledImage("/inventory/ShieldSlot.png");

        public ShieldSlot() {
            super(game.player.shield);
        }

        @Override
        protected BufferedImage getItemSlotImage() {
            return image;
        }
    }
}
