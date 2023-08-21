package game.character.monster;

import Utility.ImageAnchor;
import game.player.Player;
import game.visual.Entity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static game.main.GamePanel.*;

public class RedSlime extends Monster {
    static final ImageAnchor[][] spriteImages = loadSimpleSpriteImages(imageFolder, "redslime", 2);

    protected RedSlime(int x, int y) {
        super(new Rectangle(3, 18, 42, 30));
        animation = new SpriteWithHPBar(spriteImages, .15, 2);

        setPosition(x, y);
        game.entityManager.addMovingEntityToMap(this);

        speed = 1.6;
        maxHealth = 4;
        health = maxHealth;

        passiveDamage = 0;
        strength = 2;
    }

    @Override
    public int expWhenKilled() {
        return 5;
    }

    @Override
    protected Loot[] initLootMap() {
        return new Loot[0];
    }

    @Override
    protected void entityInteraction(Entity entity) {
        if (entity instanceof Player player) {
            player.receiveDamageFrom(getAttackDamage(), this);
        }
    }
}
