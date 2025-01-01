package game.player.magic;

import game.item.Named;

public interface Magic extends Named {
    int magicCost();

    void use();
}
