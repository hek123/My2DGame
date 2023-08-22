package game.character.monster;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import Utility.Vector2D;
import game.player.Player;
import game.visual.Entity;
import game.visual.animations.Animation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static game.main.GamePanel.FPS;
import static game.main.GamePanel.tileSize;

public class GreenGuard extends Monster {
    static final private int nbSprites = 6;
    static final private ImageAnchor[] sprites = new ImageAnchor[nbSprites];

    static {
        final int width = tileSize, height = 2*tileSize;
        BufferedImage spriteImages = UtilityTool.loadScaledImage(imageFolder + "BurningGuardSprites.png", nbSprites * width, height);
        for (int i = 0; i < nbSprites; i++) {
            sprites[i] = new ImageAnchor(spriteImages.getSubimage(i * width, 0, width, height), new Vector2D(0, tileSize));
        }
    }

    public GreenGuard() {
        super(new Rectangle(tileSize, tileSize));

        maxHealth = 10;
        health = maxHealth;

        passiveDamage = 2;

        speed = 0;
        strength = 2;

        movementAI = null;
        animation = new SimpleAnimation(nbSprites, 10, SimpleAnimation.Type.backNForth);
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

    protected class SimpleAnimation extends EntityAnimation {
        private int animationCtr = 0, sprite = 0;
        private int incr = 1;
        private final int nbSprites, fps;

        private final Type type;

        public enum Type {
            cycle, backNForth
        }

        public SimpleAnimation(int nbSprites, int fps, Type type) {
            assert nbSprites >= 2;

            this.nbSprites = nbSprites;
            this.fps = fps;
            this.type = type;
        }

        @Override
        public @NotNull Animation updateA() {
            if (animationCtr * fps / FPS >= 1) {
                sprite += incr;
                if (sprite == nbSprites) {
                    switch (type) {
                        case cycle -> sprite = 0;
                        case backNForth -> {
                            incr *= -1;
                            sprite = nbSprites - 1;
                        }
                    }
                } else if (sprite == -1) {
                    assert type == Type.backNForth;
                    incr *= -1;
                    sprite = 1;
                }
                animationCtr = 0;
            }
            animationCtr++;
            return animation;
        }

        @Override
        public void drawA(Graphics2D g2d, Vector2D framePos) {
            drawImage(g2d, framePos, sprites[sprite]);
        }
    }
}
