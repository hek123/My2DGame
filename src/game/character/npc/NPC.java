package game.character.npc;

import game.character.Direction;
import game.character.Character;
import game.player.Player;

import java.awt.*;

import static game.main.GamePanel.*;

public abstract class NPC extends Character {
    static final String imageFolder = "/npc/";

    public NPC(Rectangle bBox) {
        super(bBox);
    }

    public void speak(Player player) {
        switch (player.direction) {
            case UP -> direction = Direction.DOWN;
            case DOWN -> direction = Direction.UP;
            case LEFT -> direction = Direction.RIGHT;
            case RIGHT -> direction = Direction.LEFT;
        }
    }

    @Override
    protected void killedBy(Character character) {
        throw new RuntimeException("NPC cannot die");
    }

    @Override
    protected void hasKilled(Character character) {
        throw new RuntimeException("NPC cannot kill");
    }

    @Override
    public void receiveDamageFrom(int damage, Character source) {}
}
