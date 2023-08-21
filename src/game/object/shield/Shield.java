package game.object.shield;

import game.item.Item;

public interface Shield extends Item {
    int getDefence();

    @Override
    default int getSlotCapacity() {
            return 1;
        }
}
