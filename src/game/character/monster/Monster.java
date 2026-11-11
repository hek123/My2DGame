package game.character.monster;

import Utility.Vector2D;
import game.character.*;
import game.character.Character;
import game.event.Damage;
import game.object.SuperObject;
import game.visual.Entity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

import static game.main.GamePanel.*;

public abstract class Monster extends Character {
    static protected final String imageFolder = "/monster/";

    private final Random random = new Random();

    // Entity 'disappears when dying'
    private boolean solid = true;

    // hpBar
    protected HPBar hpBar = new HPBar(tileSize, 10, 3.);

    protected final Loot[] lootTable;

    protected Monster(Rectangle BBox) {
        super(BBox);

        lootTable = initLootMap();
    }

    @Override
    public void killedBy(Character character) {
        solid = false;
        setCurrentSpeed(0);
        moving = false;

//        animation = new DyingAnimation(animation);

        // Loot drop
        for (Loot loot : lootTable) {
            double draw = random.nextDouble();
            int idx = -1;
            double min_prob = 1.;
            for (int i = 0; i < loot.probability.length; i++) {
                if (draw < loot.probability[i] && loot.probability[i] - draw < min_prob) {
                    idx = i;
                    min_prob = loot.probability[i] - draw;
                }
            }
            if (idx != -1) {
                for (int i = 0; i < loot.amount[idx]; i++) {
                    double theta = random.nextDouble() * 2 * Math.PI;
                    double r = Math.sqrt(random.nextDouble()) * tileSize;
                    double dx = r * Math.cos(theta), dy = r * Math.sin(theta);
                    Entity.createEntity(getX() + (int) dx, getY() + (int) dy, loot.objectClass);
                }
            }
        }
    }

    @Override
    protected void hasKilled(Character character) {}

    public void receiveDamage(@NotNull Damage damage) {
        super.receiveDamage(damage);
        hpBar.makeVisible();
    }

    abstract public int expWhenKilled();

    abstract protected Loot[] initLootMap();

    @Override
    public final boolean isSolid() {
        return solid;
    }

    protected static final class Loot {
        final Class<? extends SuperObject> objectClass;
        final double[] probability;
        final int[] amount;

        Loot(Class<? extends SuperObject> object, double[] probability, int[] amount) {
            assert probability.length == amount.length;
            for (int i = 0; i < probability.length; i++) {
                assert 0 <= probability[i] && probability[i] <= 1;
                assert amount[i] > 0;
            }

            this.objectClass = object;
            this.probability = probability;
            this.amount = amount;
        }
    }

    // ### Animations ###

    @Override
    protected void drawSprite(Graphics2D g2d, Vector2D framePos) {
        super.drawSprite(g2d, framePos);
        hpBar.draw(g2d, (double) health / maxHealth, getX() - framePos.x, getY() - framePos.y);
    }

//    protected class SpriteWithHPBar extends MovingSprite {
//        public SpriteWithHPBar(ImageAnchor[][] spriteImages, double spriteUpdatePeriod, int nbSprites) {
//            super(spriteImages, spriteUpdatePeriod, nbSprites);
//        }
//
//        @Override
//        public void drawA(Graphics2D g2d, Vector2D framePos) {
//            super.drawA(g2d, framePos);
//            hpBar.draw(g2d, (double) health / maxHealth, getX() - framePos.x, getY() - framePos.y);
//        }
//    }
}
