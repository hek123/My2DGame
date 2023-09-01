package game.character.monster;

import Utility.ImageAnchor;
import game.character.HPBar;
import game.character.movementAI.RandomWalk;
import game.visual.Entity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static game.main.GamePanel.*;

public class SkeletonLord extends Monster {
    static private final ImageAnchor[][] images = loadSpriteImages(imageFolder, "skeletonlord", 2);

    public SkeletonLord(int x, int y) {
        super(new Rectangle(tileSize-24, tileSize-40, tileSize, 3*tileSize/2));
//        animation = new SpriteWithHPBar(images, .2, 2);
        super.spriteImages = images;
        nbSprites = 2;
        setSpriteUpdatePeriod(.2);

        setPosition(x, y);
        game.entityManager.addMovingEntityToMap(this);

        speed = .8;
        maxHealth = 10;
        health = maxHealth;

        dexterity = 1;

        hpBar = new HPBar(3 * tileSize / 2, 10, 3., tileSize / 4, -12);

        movementAI = new RandomWalk(this, 1.5, false);
    }

    @Override
    public int expWhenKilled() {
        return 10;
    }

    @Override
    protected Loot[] initLootMap() {
        return new Loot[0];
    }

    @Override
    protected void entityInteraction(Entity entity) {

    }
}
