package game.ui.itemUI;

import game.inventory.Inventory;
import game.inventory.Slot;
import game.item.Item;
import game.ui.Scalable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static game.main.GamePanel.*;

// TODO: Optimize to avoid using a grid of Rectangles, one can directly get the inventory index from mouse location
/**
 * An implemented ItemContainer for inventories. Displayed as a grid.
 * The only method to override is getBBox() which should return the InventoryUI bounding box based on the screen Dimension
 */
public abstract class InventoryUI implements Scalable, ItemContainer {
    static private final Color borderColor = new Color(120, 120, 120);
    static private final Stroke stroke = new BasicStroke(2);
    static private final Font font = new Font("Arial", Font.BOLD, 4 * scale);


    private final Rectangle[] grid;

    private final Inventory inventory;
    private Rectangle bBox = new Rectangle();


    public InventoryUI(Inventory inventory) {
        scalableList.add(this);

        this.inventory = inventory;

        grid = new Rectangle[inventory.inventorySlots];
    }

    protected abstract Rectangle getBBox(Dimension dimension);

    public void draw(Graphics2D g2d) {
        g2d.setColor(borderColor);
        g2d.setStroke(stroke);

        for (int i = 0; i < inventory.inventorySlots; i++) {
            // draw slot border
            g2d.drawRect(grid[i].x, grid[i].y, tileSize, tileSize);

            // draw slot item
            if (inventory.peekSlot(i) != null) {
                g2d.drawImage(inventory.peekSlot(i).getSlotItemImage(),
                        grid[i].x + (tileSize- Item.imageSize) / 2, grid[i].y + (tileSize - Item.imageSize) / 2, null);

                g2d.setColor(Color.white);
                g2d.setFont(font);
                g2d.drawString(String.valueOf(inventory.nbItems(i)), grid[i].x + 3, grid[i].y - 3 + tileSize);
                g2d.setColor(borderColor);
            }
        }
    }

    // TODO: add row & dimension check
    @Override
    public final void set(Dimension value) {
        bBox = getBBox(value);
        final int nbCols = bBox.width / tileSize;
        final int nbRows = bBox.height / tileSize;

        int x = bBox.x, y = bBox.y;

        for (int i = 0; i < inventory.inventorySlots; i++) {
            grid[i] = new Rectangle(x, y, tileSize, tileSize);

            if ((i + 1) % nbCols != 0) x += tileSize;
            else {
                y += tileSize;
                x = bBox.x;
            }
        }
    }

    @Override
    public final Slot getSlotAt(Point pos) {
        for (int i = 0; i < inventory.inventorySlots; i++) {
            if (grid[i].contains(pos)) {
                Slot slot = inventory.pollSlot(i);
                if (slot == null) {
//                    System.out.println("Grabbed empty slot at " + i);
                } else {
//                    System.out.println("Grab slot at " + i);
                    assert slot.ownerIdx == -1;
                    slot.ownerIdx = i;
                }
                return slot;
            }
        }
        return null;
    }

    @Override
    public final ItemGrabHandler.ABC putSlotAt(Point pos, Slot slot) {
        for (int i = 0; i < inventory.inventorySlots; i++) {
            if (grid[i].contains(pos)) {
//                System.out.println("Put slot in " + i);
                Slot currentSlot = inventory.pollSlot(i);
                inventory.putSlot(i, slot);
                if (currentSlot != null) {
//                    System.out.println("populated!");
                    currentSlot.ownerIdx = slot.ownerIdx;
                }
                slot.ownerIdx = -1;
                return new ItemGrabHandler.ABC(currentSlot, true);
            }
        }
        return new ItemGrabHandler.ABC(null, false);
    }

    @Override
    public final void returnSlot(@NotNull Slot slot) {
//        System.out.println("Return slot to " + slot.ownerIdx);
        assert (slot.ownerIdx != -1);

        assert inventory.putSlot(slot.ownerIdx, slot);
        slot.ownerIdx = -1;
    }

    @Override
    public final Rectangle getBBox() {
        return bBox;
    }
}
