package game.projectile;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import game.character.Character;
import game.player.magic.Magic;
import game.visual.Entity;
import game.visual.animations.Particle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static game.main.GamePanel.*;

public class FireBall extends Projectile {
    private static final ImageAnchor[][] spriteImages = new ImageAnchor[4][2];
    static {
        spriteImages[0][0] = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "blue_fireball_1.png"));
        spriteImages[0][1] = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "blue_fireball_2.png"));

        BufferedImage image0 = UtilityTool.loadScaledImage(imageFolder + "blue_fireball_1.png"),
                image1 = UtilityTool.loadScaledImage(imageFolder + "blue_fireball_2.png");
        BufferedImage image = new BufferedImage(tileSize, tileSize, image0.getType());

        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.drawImage(image0, 0, 2, null);
        g2d.dispose();

        double[] angles = {0, Math.PI / 2, Math.PI, 3 * Math.PI / 2};
        for (int i = 0; i < 4; i++) {
            spriteImages[i][0] = new ImageAnchor(new BufferedImage(tileSize, tileSize, image.getType()));
            g2d = (Graphics2D) spriteImages[i][0].image().getGraphics();
            g2d.rotate(angles[i], (double) tileSize / 2, (double) tileSize / 2);
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
        }

        g2d = (Graphics2D) image.getGraphics();
        g2d.drawImage(image1, 0, 2, null);
        g2d.dispose();
        for (int i = 0; i < 4; i++) {
            spriteImages[i][1] = new ImageAnchor(new BufferedImage(tileSize, tileSize, image.getType()));
            g2d = (Graphics2D) spriteImages[i][1].image().getGraphics();
            g2d.rotate(angles[i], (double) tileSize / 2, (double) tileSize / 2);
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
        }
    }
    private static final Particle particle = new Particle(8, Color.red, .4);

    public FireBall(Character source) {
        super(new Rectangle(16, 16, 16, 16), source, 8.5, source.getExactDirection());
        super.spriteImages = spriteImages;
        nbSprites = 2;
        setSpriteUpdatePeriod(.15);
    }

    @Override
    protected void action() {
        if (!isMoving()) {
            game.entityManager.removeMovingEntityFromMap(this);
            System.out.println("fiya dead");
        }
    }

    @Override
    public void entityInteraction(Entity entity) {
        if (collisionExceptions.contains(entity)) {
            System.err.println("Fireball hit Player");
        } else if (entity instanceof Character character) {
            character.receiveDamageFrom(source.strength + 1, source);
        }
    }

    @Override
    protected void collide(ArrayList<Rectangle> collisionTargets) {
        double x = 0, y = 0;
        for (Rectangle bBox: collisionTargets) {
            x += bBox.getCenterX();
            y += bBox.getCenterY();
        }
        particle.generate((int)(x / collisionTargets.size()), (int)(y / collisionTargets.size()), Math.random() * Math.TAU, 2.);
        particle.generate((int)(x / collisionTargets.size()), (int)(y / collisionTargets.size()), Math.random() * Math.TAU, 2.);
        particle.generate((int)(x / collisionTargets.size()), (int)(y / collisionTargets.size()), Math.random() * Math.TAU, 2.);
    }
}
