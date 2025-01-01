package game.character.monster;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import Utility.Vector2D;
import game.character.movementAI.MovementAI;
import game.player.Player;
import game.projectile.GreenFireBall;
import game.projectile.Projectile;
import game.visual.Entity;
import game.visual.MovingEntity;
import game.visual.animations.Animation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static game.main.GamePanel.*;

public class GreenGuard extends Monster implements MovementAI {
    static final private int nbSprites = 6, fps = 10;
    static final private ImageAnchor[] sprites = new ImageAnchor[nbSprites];

    static {
        final int width = tileSize, height = 2*tileSize;
        BufferedImage spriteImages = UtilityTool.loadScaledImage(imageFolder + "BurningGuardSprites.png", nbSprites * width, height);
        for (int i = 0; i < nbSprites; i++) {
            sprites[i] = new ImageAnchor(spriteImages.getSubimage(i * width, 0, width, height), new Vector2D(0, tileSize));
        }
    }

    private int ctr = 0;
    private int incr = 1;

    public GreenGuard() {
        super(new Rectangle(tileSize, tileSize));

        maxHealth = 10;
        health = maxHealth;

        passiveDamage = 2;

        speed = 0;
        strength = 2;

        movementAI = this;

        super.nbSprites = nbSprites;
        spriteImages = new ImageAnchor[4][nbSprites];
        Arrays.fill(spriteImages, sprites);
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
            player.receiveDamageFrom(passiveDamage, this);
        }
    }

    @Override
    public void updateAI() {
        if (ctr > 2 * FPS) {
            System.out.println("fireball!!");
            new GreenFireBall(2., Projectile.getAngle(this, game.player), this);
            ctr = 0;
        }
        ctr++;
    }

    @Override
    protected void updateSpriteCounter() {
        if (spriteCounter * fps / FPS >= 1) {
            currentSprite += incr;
            if (currentSprite == nbSprites - 1 || currentSprite == 0) {
                incr *= -1;
            }
            spriteCounter = 0;
        }
        spriteCounter++;
    }
}
