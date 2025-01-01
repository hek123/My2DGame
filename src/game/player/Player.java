package game.player;

import Utility.ImageAnchor;
import Utility.Vector2D;
import game.character.Character;
import game.character.Direction;
import game.character.monster.*;
import game.character.movementAI.MovementAI;
import game.character.npc.NPC;
import game.inventory.Inventory;
import game.object.SuperObject;
import game.main.*;
import game.object.shield.Shield;
import game.object.weapon.Weapon;
import game.player.magic.FireBallMagic;
import game.player.magic.Magic;
import game.tile.TileManager;
import game.visual.Entity;
import game.projectile.Arrow;
import game.visual.animations.Particle;
import main.Sound;

import static game.main.GamePanel.*;

import java.awt.*;
import java.awt.event.KeyEvent;


public final class Player extends Character implements MovementAI {
    static private final Stroke attackBoxStroke = new BasicStroke(1);

    // Attack & damage
    private final Rectangle[] attackRange = {
            new Rectangle(48-3, 21, 30, 16),
            new Rectangle(16, 48, 16, 30),
            new Rectangle(-30+3, 21, 30, 16),
            new Rectangle(16, -30, 16, 30)
    };
    boolean attacking = false;

    // Player attributes
    public int level;
    public int XP, nextLevelXP;
    public int coin;
    public final Weapon[] weapon = new Weapon[1];
    public final Shield[] shield = new Shield[1];
    public Magic magicSpell = new FireBallMagic(this);
    public final int crawling_speed = 1;

    // Counters
    private int magicRefillCounter = 0;

    // Graphics
    static private final ImageAnchor[][][] spriteImages = new ImageAnchor[2][4][2];
    static {
        spriteImages[0] = loadSpriteImages("/player/walking/", "boy", 2);
        spriteImages[1] = loadSpriteImages("/player/attacking/", "boy_attack", 2);
        fixAttackingSpriteAnchor(spriteImages[1], 2);
    }

    // Controls
    private final int up_key = KeyEvent.VK_UP, down_key = KeyEvent.VK_DOWN,
            left_key = KeyEvent.VK_LEFT, right_key = KeyEvent.VK_RIGHT,
            attack_key = KeyEvent.VK_F, spell_key = KeyEvent.VK_R, shoot_key = KeyEvent.VK_V,
            crawl_key = KeyEvent.VK_SHIFT;

    public final Inventory inventory = new Inventory(10);

    public Player() {
        super(new Rectangle(9, 16, 30, 30));
        super.spriteImages = spriteImages[0];
        nbSprites = 2;
        setSpriteUpdatePeriod(.15);

        setPosition(23 * GamePanel.tileSize, 21 * GamePanel.tileSize);
        game.entityManager.addEntityToMap(this);

        movementAI = this;

        speed = 3;
        maxHealth = 6;
        health = maxHealth;
        maxMagic = 6;
        magic = maxMagic;
        level = 0;
        strength = 1;
        dexterity = 0;
        XP = 0;
        nextLevelXP = 8;
        coin = 0;
    }

    @Override
    public void drawDebugInfo(Graphics2D g2d, Vector2D framePos) {
        super.drawDebugInfo(g2d, framePos);

        // attackBox
        if (attacking) {
            g2d.setColor(Color.orange);
            g2d.setStroke(attackBoxStroke);
            Rectangle box = getAttackRange();
            g2d.drawRect(box.x + getX() - framePos.x, box.y + getY() - framePos.y, box.width, box.height);
        }
    }

    public void updateAI() {
        // movement
        setCurrentSpeed(keyHandler.isKeyPressed(crawl_key) ? crawling_speed : speed);
        moving = true;
        if (keyHandler.isKeyPressed(up_key)) {
            setDirection(Direction.UP);
        } else if (keyHandler.isKeyPressed(down_key)) {
            setDirection(Direction.DOWN);
        } else if (keyHandler.isKeyPressed(right_key)) {
            setDirection(Direction.RIGHT);
        } else if (keyHandler.isKeyPressed(left_key)) {
            setDirection(Direction.LEFT);
        } else {
            moving = false;
            setCurrentSpeed(0);
        }
        assert TileManager.visible.contains(getNextBBox());

        // attacking
        if (keyHandler.isKeyClicked(attack_key) && !attacking) {
            attacking = true;
            Sound.playSoundEffect(Sound.parry);
            game.entityManager.collisionChecker.checkAttack(this);
            super.spriteImages = spriteImages[1];
            spriteCounter = 0;
        }

        // magic spells
        if (keyHandler.isKeyClicked(spell_key)) {
            if (magic >= magicSpell.magicCost()) {
                magicSpell.use();
                magic -= magicSpell.magicCost();
            }
        }
        // shoot arrow
        if (keyHandler.isKeyClicked(shoot_key)) {
            new Arrow(this);
        }

        // Magic refillCounter
        if (magic < maxMagic) {
            magicRefillCounter++;
            if (magicRefillCounter > 2*FPS) {
                magic++;
                magicRefillCounter = 0;
            }
        }
    }

    public void attack(Entity entity) {
        Particle p = new Particle(5, Color.lightGray, .2);
        p.generate((int) entity.getBBox().getCenterX(), (int) entity.getBBox().getCenterY(), Math.random() * Math.TAU, 5);

        entity.attackedBy(this);
    }

    @Override
    protected void killedBy(Character character) {}

    @Override
    protected void hasKilled(Character character) {
        if (character instanceof Monster monster)
            getExp(monster.expWhenKilled());
    }

    @Override
    public void entityInteraction(Entity entity) {
        if (entity instanceof SuperObject obj) {
            obj.getObject(this);
        } else if (entity instanceof NPC npc) {
            npc.speak(this);
        } else if (entity instanceof Monster monster) {
            receiveDamageFrom(monster.passiveDamage, monster);
            Sound.playSoundEffect(Sound.receiveDamage);
        }
    }

    public void getExp(int exp) {
        XP += exp;
        while (XP >= nextLevelXP) {
            XP -= nextLevelXP;
            level++;
            nextLevelXP += 2;
            maxHealth += 2;
            maxMagic += 2;
            health += 2;
            strength++;
            dexterity++;

            Sound.playSoundEffect("levelup");
            game.ui.scrollingMessage("levelup!");
        }
    }

    public int getAttackDamage() {
        if (weapon[0] == null) return strength;
        else return strength + weapon[0].getDamage();
    }

    public Rectangle getAttackRange() {
        return new Rectangle(attackRange[dirToInt(getDirection())]);
    }

    @Override
    public void updateA() {
        if (attacking) {
            double elapsedTime = (double) spriteCounter / GamePanel.FPS;
            if (elapsedTime < .12) {
                currentSprite = 0;
            } else if (elapsedTime < .42) {
                currentSprite = 1;
            } else if (spriteCounter < .45) {
                currentSprite = 0;
            } else {
                attacking = false;
                super.spriteImages = spriteImages[0];
            }
            spriteCounter++;
        } else {
            updateSpriteCounter();
        }
    }

    static final private int width = 5;
    static private final Stroke stroke = new BasicStroke(2*width);

    static private final Color red = new Color(.8f, 0.f, 0.f);
    static private final Color transpRed = new Color(.8f, 0.f, 0.f, .5f);

    @Override
    public void drawA(Graphics2D g2d, Vector2D framePos) {
        super.drawA(g2d, framePos);

        if (invincible) {
            g2d.setColor((damageCoolDownCounter % (FPS / 2) < FPS / 4) ? red : transpRed);
            g2d.setStroke(stroke);
            g2d.drawRect(width, width, TileManager.visible.width - 2 * width, TileManager.visible.height - 2 * width);
        }
    }
}
