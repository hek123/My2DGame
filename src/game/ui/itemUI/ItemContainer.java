package game.ui.itemUI;

import game.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * ItemContainer is an interface for visible items to support drag & drop inside the container and between containers
 * The effective drag & drop is implemented in the ItemGrabHandler
 */
public interface ItemContainer {
    Slot getSlotAt(Point pos);

    ItemGrabHandler.ABC putSlotAt(Point pos, Slot slot);

    void returnSlot(@NotNull Slot slot);

    Rectangle getBBox();

    void draw(Graphics2D g2d);
}
