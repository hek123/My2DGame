package game.player.magic;

import game.character.Character;
import game.projectile.FireBall;


public class FireBallMagic implements Magic {
    private final Character source;

    public FireBallMagic(Character source) {
        this.source = source;
    }

    @Override
    public String getName() {
        return "Fireball";
    }

    @Override
    public void use() {
        new FireBall(source);
    }

    @Override
    public int magicCost() {
        return 2;
    }
}
