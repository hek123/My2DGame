package game.event;

import static game.main.Game.game;


public class DamagePit extends Event {
    DamagePit(int x, int y) {
        super(x, y);
        oneTime = true;
    }

    @Override
    public void action() {
        game.ui.dialogue("you fall into a pit", game.player.getBBox(), game.player.getDirection());
        game.player.receiveDamage(new Damage(null, 1, 0));
    }
}
