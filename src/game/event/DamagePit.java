package game.event;

import static game.main.GamePanel.game;


public class DamagePit extends Event {
    DamagePit(int x, int y) {
        super(x, y);
        oneTime = true;
    }

    @Override
    public void action() {
        game.ui.dialogue("you fall into a pit", game.player.getBBox(), game.player.direction);
        game.player.receiveDamageFrom(1, null);
    }
}
