package game.character;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import Utility.Vector2D;
import game.character.movementAI.MovementAI;
import game.character.movementAI.TerritoryBound;
import game.event.Damage;
import game.character.DamageState;
import game.main.GamePanel;
import game.visual.Entity;
import game.visual.MovingEntity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

import static game.main.Game.game;
import static game.main.GamePanel.FPS;

/**
 * A Character extends the MovingEntity class -which only implements graphics related methods- to a character with properties
 * like health, speed, damage, ...
 */
public abstract class Character extends MovingEntity {
    protected Color territoryColor = new Color(100, 100, 50, 80);

    // CHARACTER ATTRIBUTES
    protected int maxHealth, health;
    public double speed;
    public int passiveDamage = 0;
    public int strength, dexterity;

    public int magic, maxMagic;

    // States
    public DamageState damage_state = DamageState.normal;
    protected MovementAI movementAI;

    // counters
    protected int damageCoolDownCounter = 0, dyingCounter = 0;
    protected int damageCoolDownTime = FPS;

    protected Character(Rectangle BBox) {
        super(BBox);

        setDirection(Direction.DOWN);
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public void drawDebugInfo(Graphics2D g2d, Vector2D framePos) {
        super.drawDebugInfo(g2d, framePos);

        // territory
        if (movementAI instanceof TerritoryBound territoryBound) {
            Rectangle territory = territoryBound.getTerritory();
            if (territory != null) {
                g2d.setColor(territoryColor);
                g2d.fillRect(territory.x - framePos.x, territory.y - framePos.y, territory.width, territory.height);
            }
        }
    }

    public final int getMaxHealth() {
        return maxHealth;
    }

    public final int getHealth() {
        return health;
    }

    public final void receiveHealth(int amount) {
        health += amount;
        if (health > maxHealth) health = maxHealth;
    }

    public final boolean isDead() {
        assert health >= 0 && health <= maxHealth;
        return health == 0;
    }

    @Override
    protected final void action() {
        switch (damage_state) {
            case normal -> {
                if (movementAI != null) movementAI.updateAI();
            }
            case dying -> System.err.println("The dead are walking!");
            case invincible -> {
                damageCoolDownCounter++;
                if (damageCoolDownCounter >= damageCoolDownTime) {
                    damageCoolDownCounter = 0;
                    damage_state = DamageState.normal;
                }
            }

        }
//        if (dying)
//            System.err.println("The dead are walking!");
//
//        if (movementAI != null) movementAI.updateAI();
//
//        if (invincible) {
//            damageCoolDownCounter++;
//            if (damageCoolDownCounter >= damageCoolDownTime) {
//                damageCoolDownCounter = 0;
//                invincible = false;
//            }
//        }
    }


    // ### Damage & Kill interaction ###

    @Override
    public void attackedBy(Character character) {
        receiveDamage(new Damage(character, character.getAttackDamage(), 0));
    }

    /**
     * called when entity receives damage
     * @param damage the damage received
     */
    public void receiveDamage(@NotNull Damage damage) {
        if (damage_state == DamageState.normal) {
            assert (!isDead());
            health -= (int) (Math.pow(1.5, -getDefence()) * damage.physical_damage());
            if (health <= 0) {
                health = 0;
                damage.source().hasKilled(this);
                killedBy(damage.source());
                damage_state = DamageState.dying;
                game.entityManager.removeMovingEntityFromMap(this);
            } else {
                damage_state = DamageState.invincible;
            }
        }
//        if (isDead()) return;
//        if (!invincible) {
//            health -= (int) (Math.pow(1.5, -getDefence()) * damage.physical_damage());
//            if (health <= 0) {
//                health = 0;
//                damage.source().hasKilled(this);
//                killedBy(damage.source());
//                dying = true;
//                game.entityManager.removeMovingEntityFromMap(this);
//            } else {
//                invincible = true;
//            }
//        }
    }

    /**
     * called when this character has been killed, and thus dies
     */
    protected abstract void killedBy(Character character);

    /**
     * called when this character has killed another character
     * @param character character that has been killed
     */
    protected abstract void hasKilled(Character character);

    public int getDefence() {
        return dexterity;
    }

    public int getAttackDamage() {
        return strength;
    }

    // ### Animations ###

    @Override
    public void updateA() {
        if (damage_state == DamageState.dying) {
            if (dyingCounter >= FPS / 2) {
                System.out.println("dead");
                game.entityManager.removeAnimationFromMap(this);
            }
            dyingCounter++;
        } else {
            updateSpriteCounter();
        }
    }

    @Override
    protected void entityInteraction(Entity entity) {

    }

    @Override
    public void drawA(Graphics2D g2d, Vector2D framePos) {
        switch (damage_state) {
            case dying -> {
                if (dyingCounter % (FPS / 6) < (FPS / 12))
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
            }
            case invincible -> {
                if (damageCoolDownCounter % (FPS / 3) < (FPS / 6))
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
            }
        }
        drawSprite(g2d, framePos);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));

//        if (dying) {
//            if (dyingCounter % (FPS / 6) < (FPS / 12))
//                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
//            drawSprite(g2d, framePos);
//            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
//        } else {
//            if (invincible && damageCoolDownCounter % (FPS / 3) < (FPS / 6))
//                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
//            drawSprite(g2d, framePos);
//            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
//        }
    }

    // ### UTILS ###
    static protected ImageAnchor[][] loadSpriteImages(String spriteFolder, String spriteName, int nbSprites) {
        ImageAnchor[][] imagesOut = new ImageAnchor[4][nbSprites];
        String[] dirs = new String[]{"_right_", "_down_", "_left_", "_up_"};
        for (int dir = 0; dir < 4; dir++) {
            for (int spriteNum = 0; spriteNum < nbSprites; spriteNum++) {
                imagesOut[dir][spriteNum] = new ImageAnchor(UtilityTool.loadAutoScaledImage(
                        spriteFolder + spriteName + dirs[dir] + (spriteNum+1) + ".png"));
            }
        }
        return imagesOut;
    }

    static protected ImageAnchor[][] loadSimpleSpriteImages(String spriteFolder, String spriteName, int nbSprites) {
        ImageAnchor[][] imagesOut = new ImageAnchor[4][nbSprites];
        for (int spriteNum = 0; spriteNum < nbSprites; spriteNum++) {
            imagesOut[0][spriteNum] = new ImageAnchor(UtilityTool.loadAutoScaledImage(
                    spriteFolder + spriteName + "_down_" + (spriteNum+1) + ".png"));
        }
        imagesOut[1] = imagesOut[2] = imagesOut[3] = imagesOut[0];
        return imagesOut;
    }
}
