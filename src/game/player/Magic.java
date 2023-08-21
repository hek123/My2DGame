package game.player;

import game.item.Named;

public interface Magic extends Named {
    int magicCost();

    void use();
}
