package game.character;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import Utility.Vector2D;
import game.character.movementAI.MovementAI;
import game.character.movementAI.TerritoryBound;
import game.visual.MovingEntity;
import game.visual.animations.Animation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static game.main.GamePanel.*;

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
    boolean invincible = false, dying = false;
    protected MovementAI movementAI;

    // counters
    protected int damageCoolDownCounter = 0;
    protected int damageCoolDownTime = FPS;

    // animations
    protected Animation walkingAnimation, dyingAnimation;
    protected InvincibleAnimation invincibleAnimation;


    protected Character(Rectangle BBox) {
        super(BBox);

        setDirection(Direction.DOWN);
    }

//    public void setSpeed(double speed) {
//        currentSpeed = speed;
//    }

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

    @Override
    public boolean isSolid() {
        return true;
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
        if (dying)
            System.err.println("The dead are walking!");

        if (movementAI != null) movementAI.updateAI();

        if (invincible) {
            damageCoolDownCounter++;
            if (damageCoolDownCounter >= damageCoolDownTime) {
                damageCoolDownCounter = 0;
                invincible = false;
            }
        }
    }


    // ### Damage & Kill interaction ###

    @Override
    public void attackedBy(Character character) {
        receiveDamageFrom(character.getAttackDamage(), character);
    }

    /**
     * called when entity receives damage
     * @param damage the amount of damage received
     * @param source from which character the damage originates
     */
    public void receiveDamageFrom(int damage, Character source) {
        if (isDead()) return;
        if (!invincible) {
            health -= (int) (Math.pow(1.5, -getDefence()) * damage);
            if (health <= 0) {
                health = 0;
                killedBy(source);
                source.hasKilled(this);
                dying = true;
            } else {
                invincible = true;
                if (invincibleAnimation == null) invincibleAnimation = new InvincibleAnimation(animation);
                else invincibleAnimation.reset(animation);
                animation = invincibleAnimation;
            }
        }
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
    protected class InvincibleAnimation extends EntityAnimation {
        private Animation baseAnimation;

        protected InvincibleAnimation(@NotNull Animation baseAnimation) {
            this.baseAnimation = baseAnimation;
        }

        @Override
        public @NotNull Animation updateA() {
            baseAnimation.updateA();
            animation = invincible ? animation : baseAnimation;
            return animation;
        }

        @Override
        public void drawA(Graphics2D g2d, Vector2D framePos) {
            if (damageCoolDownCounter % (FPS / 3) < (FPS / 6))
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
            baseAnimation.drawA(g2d, framePos);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
        }

        public void reset(@NotNull Animation baseAnimation) {
            assert baseAnimation != this;
            this.baseAnimation = baseAnimation;
        }
    }

    protected class DyingAnimation extends EntityAnimation {
        Animation baseAnimation;
        int dyingCounter = 0;

        public DyingAnimation(Animation baseAnimation) {
            this.baseAnimation = baseAnimation;
            game.entityManager.removeMovingEntityFromMap(Character.this);
            game.entityManager.addAnimationToMap(this);
        }

        @Override
        public void drawA(Graphics2D g2d, Vector2D framePos) {
            if (dyingCounter % (FPS / 6) < (FPS / 12))
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
            baseAnimation.drawA(g2d, framePos);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
        }

        @Override
        public @NotNull Animation updateA() {
            if (dyingCounter >= FPS / 2) {
                System.out.println("dead");
                game.entityManager.removeAnimationFromMap(this);
            }
            dyingCounter++;
            return animation;
        }
    }

    // ### UTILS ###
    static protected ImageAnchor[][] loadSpriteImages(String spriteFolder, String spriteName, int nbSprites) {
        ImageAnchor[][] imagesOut = new ImageAnchor[4][nbSprites];
        String[] dirs = new String[]{"_down_", "_up_", "_left_", "_right_"};
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
