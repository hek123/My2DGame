package game.ui.itemUI;

import game.inventory.Slot;
import game.item.Item;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

import static game.main.GamePanel.*;

/**
 * An implemented ItemContainer for a single Slot with custom background.
 * @param <T> Type of entity the slot is allowed to hold.
 */
public abstract class ItemSlotUI<T extends Item> implements ItemContainer {
    final public Rectangle slotBounds = new Rectangle(tileSize, tileSize);
    final T[] item;

    public ItemSlotUI(T[] item) {
        assert item.length == 1;
        this.item = item;
    }

    @Override
    public final Slot getSlotAt(Point pos) {
        if (slotBounds.contains(pos) && item[0] != null) {
            Slot out = new Slot(item[0]);
            out.ownerIdx = 0;
            item[0] = null;
            return out;
        }
        else return null;
    }

    @Override
    public final ItemGrabHandler.ABC putSlotAt(Point pos, Slot slot) {
        if (slotBounds.contains(pos)) {
            System.out.println("Put slot in weapon");
            try {
                T t = (T) slot.peekItem();
                Slot out;
                if (item[0] != null) {
                    out = new Slot(item[0]);
                    out.ownerIdx = slot.ownerIdx;
                } else {
                    out = null;
                }
                item[0] = t;

                return new ItemGrabHandler.ABC(out, true);
            } catch (ClassCastException | ArrayStoreException e) {
                System.out.println("Only Items of type " + item.getClass() + " can be equipped");
            }
        }
        return new ItemGrabHandler.ABC(null, false);
    }

    @Override
    public final void returnSlot(@NotNull Slot slot) {
        System.out.println("weapon return");
        assert slot.ownerIdx == 0;
        try {
            item[0] = (T) slot.peekItem();
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final Rectangle getBBox() {
        return slotBounds;
    }

    @Override
    public final void draw(Graphics2D g2d) {
        g2d.drawImage(getItemSlotImage(), slotBounds.x, slotBounds.y, null);

        if (item[0] != null) {
            g2d.drawImage(item[0].getItemImage(), slotBounds.x + 3, slotBounds.y + 3, null);
        }
    }

    protected abstract BufferedImage getItemSlotImage();
}
