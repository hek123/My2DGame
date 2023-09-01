package game.character.monster;

import Utility.ImageAnchor;
import game.character.Character;
import game.character.movementAI.RandomWalk;
import game.object.Coin;
import game.object.Heart;
import game.player.Player;
import game.visual.Entity;

import java.awt.*;

import static game.main.GamePanel.*;

public class GreenSlime extends Monster {
    static final ImageAnchor[][] spriteImages = loadSimpleSpriteImages(imageFolder, "greenslime", 2);

    public GreenSlime(int x, int y, Rectangle territory) {
        super(new Rectangle(3, 18, 42, 30));
//        animation = new SpriteWithHPBar(spriteImages, .15, 2);
        super.spriteImages = spriteImages;
        nbSprites = 2;
        setSpriteUpdatePeriod(.15);

        setPosition(x, y);
        game.entityManager.addMovingEntityToMap(this);

        speed = 1;
        maxHealth = 4;
        health = maxHealth;

        passiveDamage = 1;
        strength = 2;

        movementAI = new RandomWalk(this, 1., false, territory);
    }
    public GreenSlime(int x, int y) {
        this(x, y, null);
    }

    @Override
    public void receiveDamageFrom(int damage, Character source) {
        super.receiveDamageFrom(damage, source);
        setDirection(source.getDirection());
    }

    @Override
    public int expWhenKilled() {
        return 3;
    }

    @Override
    protected Loot[] initLootMap() {
        Loot[] lootMap = new Loot[2];
        lootMap[0] = new Loot(Coin.class, new double[]{.5, .1}, new int[]{1, 2});
        lootMap[1] = new Loot(Heart.class, new double[]{.2}, new int[]{1});
        return lootMap;
    }

    @Override
    public void entityInteraction(Entity entity) {
        if (entity instanceof Player player) {
            player.receiveDamageFrom(getAttackDamage(), this);
        }
    }

    @Override
    public void killedBy(Character character) {
        super.killedBy(character);

        game.ui.scrollingMessage("Killed Green Slime!");
    }
}
