package game.ui.itemUI;

import game.inventory.Slot;
import game.item.Item;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static game.main.GamePanel.*;

public final class ItemGrabHandler {
    private final List<ItemContainer> itemContainers = new ArrayList<>();

    private Slot grabbedSlot;
    private ItemContainer grabbedContainer;

    public void update() {
        if (mouseHandler.mousePressed && grabbedSlot == null) {
            for (ItemContainer c: itemContainers) {
                if (c.getBBox().contains(mouseHandler.pressedLocation)) {
                    grabbedSlot = c.getSlotAt(mouseHandler.pressedLocation);
                    if (grabbedSlot != null) {
                        grabbedContainer = c;
                        return;
                    }
                }
            }
        }
        if (!mouseHandler.mousePressed && grabbedSlot != null) {
            for (ItemContainer c: itemContainers) {
                if (c.getBBox().contains(mouseHandler.releasedLocation)) {
                    ABC abc = c.putSlotAt(mouseHandler.releasedLocation, grabbedSlot);
                    if (!abc.flag) {
                        grabbedContainer.returnSlot(grabbedSlot);
                    } else if (abc.slot != null) {
                        grabbedContainer.returnSlot(abc.slot);
                    }
                    grabbedContainer = null;
                    grabbedSlot = null;
                    return;
                }
            }
            grabbedContainer.returnSlot(grabbedSlot);
            grabbedContainer = null;
            grabbedSlot = null;
        }
    }

    public void drawGrabbedItem(Graphics2D g2d) {
        if (grabbedSlot != null) {
            if (!grabbedSlot.isEmpty()) {
                Point mousePosition = mouseHandler.getMousePosition();
                if (mousePosition != null)
                    g2d.drawImage(grabbedSlot.getSlotItemImage(), mousePosition.x - Item.imageSize / 2, mousePosition.y - Item.imageSize / 2, null);
            }
        }
    }

    public void addContainer(ItemContainer c) {
        // TODO: assertion useless because of dynamic size. Implement this in a dynamic way?
//        Rectangle newBBox = c.getBBox();
//        itemContainers.forEach((ItemContainer ci) -> {assert !(newBBox.intersects(ci.getBBox()));});

        itemContainers.add(c);
    }

    record ABC(Slot slot, boolean flag) {}
}
