package game.object.weapon;

import game.item.Item;
import game.item.Named;

public interface Weapon extends Item, Named {
    int getDamage();

    @Override
    default int getSlotCapacity() {
        return 1;
    }
}
