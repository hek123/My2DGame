package game.inventory;

import game.item.Item;

import java.util.*;

public class Inventory {
    public final int inventorySlots;
    private final Slot[] inventory;

    public Inventory(int capacity) {
        assert capacity > 0;
        inventorySlots = capacity;
        inventory = new Slot[capacity];
    }

    /**
     * Puts the item in the inventory.
     * If there is a slot with place for another item the item is added to this slot. Else a new slot is created for
     * this item.
     * @param item the item to be added to the inventory
     * @return true if the item was correctly added to the inventory, false if the inventory is full
     */
    public boolean putItem(Item item) {
        if (item.getSlotCapacity() == 0) {
            System.err.println("Warning: slotCapacity = 0");
            return false;
        } else {
            int emptySlot = -1;
            for (int i = 0; i < inventorySlots; i++) {
                if (inventory[i] == null) {
                    if (emptySlot == -1) emptySlot = i;
                } else if (inventory[i].getItemName().equals(item.getName())) {
                    if (inventory[i].addItem(item)) return true;
                }
            }
            if (emptySlot == -1) return false;
            inventory[emptySlot] = new Slot(item);
            return true;
        }
    }

    public boolean putSlot(int idx, Slot slot) {
        assert 0 <= idx && idx < inventorySlots;
        if (inventory[idx] == null) {
            inventory[idx] = slot;
            return true;
        }
        return false;
    }

    /**
     * Returns the item residing at the given slot index, null if the slot is empty
     * @param slot index
     * @return item or null
     */
    public Item peekItem(int slot) {
        if (inventory[slot] != null) {
            return inventory[slot].peekItem();
        }
        return null;
    }

    public Slot peekSlot(int slot) {
        return inventory[slot];
    }

    /**
     * returns and removes one item in the requested slot index
     * @param slot index
     * @return item
     */
    public Item pollItem(int slot) {
        assert (0 <= slot && slot < inventorySlots);
        if (inventory[slot] == null) {
            return null;
        } else {
            Item item = inventory[slot].pollItem();
            if (inventory[slot].isEmpty()) inventory[slot] = null;
            return item;
        }
    }

    public Slot pollSlot(int slot) {
        assert (0 <= slot && slot < inventorySlots);
        Slot out = inventory[slot];
        inventory[slot] = null;
        return out;
    }

    /**
     * returns and removes the first occurrence of the requested item
     * @param itemName name of the item to find
     * @return item
     */
    public Item getItem(String itemName) {
        for (int i = 0; i < inventorySlots; i++) {
            if (inventory[i] != null) {
                if (inventory[i].getItemName().equals(itemName)) {
                    Item item = inventory[i].pollItem();
                    if (inventory[i].isEmpty()) inventory[i] = null;
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * get the number of items residing at the given slot index
     * @param slot index
     * @return the number of items
     */
    public int nbItems(int slot) {
        assert (0 <= slot && slot < inventorySlots);
        assert inventory[slot] != null;
        return inventory[slot].size();
    }

    /**
     * Swaps the Slots of the two indices
     * @param slot1 idx1
     * @param slot2 idx2
     */
    public void swapSlots(int slot1, int slot2) {
        Slot tmp = inventory[slot1];
        inventory[slot1] = inventory[slot2];
        inventory[slot2] = tmp;
    }
}

