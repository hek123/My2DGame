package game.inventory;

import game.item.Item;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;


public class Slot {
    private final String itemName;
    private final Item[] items;
    private final int capacity;
    private int head = 0;

    public int ownerIdx = -1;

    public Slot(@NotNull String itemName, int capacity) {
        this.itemName = itemName;
        this.capacity = capacity;
        items = new Item[capacity];
    }
    public Slot(@NotNull Item item) {
        this(item.getName(), item.getSlotCapacity());
        addItem(item);
    }

    /**
     * add the item to the current slot
     * @param item Item
     * @return true if successful, false if the slot is full
     */
    boolean addItem(@NotNull Item item) {
        if (item.getName().equals(itemName)) {
            if (size() < capacity) {
                items[head] = item;
                head++;
                return true;
            }
        }
        return false;
    }

    /**
     * return and remove one item from the slot
     * @return item
     */
    Item pollItem() {
        if (size() > 0) {
            head--;
            Item item = items[head];
            items[head] = null;
            return item;
        }
        return null;
    }

    /**
     * return the item residing in the slot (first item is returned)
     * @return item
     */
    public Item peekItem() {
        System.out.println("peek peek");
        if (size() > 0) {
            return items[head - 1];
        }
        return null;
    }

    /**
     *
     * @return the number of items in the slot
     */
    int size() {
        assert 0 <= head && head <= capacity;
        return head;
    }

    boolean isFull() {
        return head == capacity;
    }

    public boolean isEmpty() {
        return head == 0;
    }

    String getItemName() {
        return itemName;
    }

    public BufferedImage getSlotItemImage() {
        if (head <= 0)
            System.err.println("Warning: accessing image of empty Slot");
        return items[head - 1].getItemImage();
    }
}
