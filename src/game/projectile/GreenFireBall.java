package game.projectile;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import Utility.Vector2D;
import game.character.Character;
import game.visual.Entity;
import game.visual.animations.Animation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

import static game.main.GamePanel.game;

public class GreenFireBall extends Projectile {
    static private final BufferedImage image = UtilityTool.loadScaledImage(imageFolder + "GreenFireball.png");

    private final ImageAnchor imageAnchor;

    public GreenFireBall(double speed, double direction, Character source) {
        super(new Rectangle(16, 16, 16, 16), source, speed);

        imageAnchor = new ImageAnchor(UtilityTool.rotateImage(image, direction));
        animation = new GFA();

        fire(direction);

        assert isMoving();
    }

    @Override
    protected void entityInteraction(Entity entity) {
        if (collisionExceptions.contains(entity)) {
            System.err.println("Fireball hit Source");
        } else if (entity instanceof Character character) {
            character.receiveDamageFrom(source.strength, source);
        } //else if (entity instanceof FireBall fireBall) {
//            System.out.println("fireWorks");
//            moving = false;
//            fireBall.addCollisionTarget(this.getBBox());
////            fireBall.moving = false;
////            fireBall.entityInteraction(this);
//        }
    }

    @Override
    protected void action() {
        if (!isMoving()) game.entityManager.removeMovingEntityFromMap(this);
    }

    @Override
    public void attackedBy(Character character) {
        setDirection(getExactDirection() + Math.PI);
        collisionExceptions.remove(source);
        source = character;
    }

    class GFA extends EntityAnimation {
        @Override
        public @NotNull Animation updateA() {
            return animation;
        }

        @Override
        public void drawA(Graphics2D g2d, Vector2D framePos) {
            drawImage(g2d, framePos, imageAnchor);
        }
    }
}
