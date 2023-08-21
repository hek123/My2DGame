package game.projectile;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import game.character.Character;
import game.player.Magic;
import game.player.Player;
import game.visual.Entity;
import game.visual.animations.Particle;

import java.awt.*;
import java.util.ArrayList;

import static game.main.GamePanel.*;

public class FireBall extends Projectile implements Magic {
    private static final ImageAnchor[][] spriteImages = new ImageAnchor[4][2];
    static {
        spriteImages[0][0] = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "blue_fireball_1.png"));
        spriteImages[0][1] = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "blue_fireball_2.png"));
        spriteImages[1] = spriteImages[2] = spriteImages[3] = spriteImages[0];
    }
    private static final Particle particle = new Particle(8, Color.red, .4);

    private final double speed = 8.5;

    private final Character source;

    public FireBall(Character source) {
        super(new Rectangle(16, 16, 16, 16));
        animation = new MovingSprite(spriteImages, .15, 2);
        this.source = source;
    }

    @Override
    public String getName() {
        return "Fireball";
    }

    @Override
    public void use() {
        new FireBall(source).fire();
    }

    private void fire() {
        Rectangle sourceBBox = source.getNextBBox();
//        Rectangle thisSA = getSolidArea();
        Rectangle thisSA = new Rectangle(0, 0, tileSize, tileSize);
        switch (source.direction) {
            case LEFT -> setPosition(sourceBBox.x - thisSA.x - thisSA.width, source.getY());
            case RIGHT -> setPosition(sourceBBox.x + sourceBBox.width - thisSA.x, source.getY());
            case DOWN -> setPosition(source.getX(), sourceBBox.y + sourceBBox.height - thisSA.y);
            case UP -> setPosition(source.getX(), sourceBBox.y- thisSA.height - thisSA.y);
        }

        direction = source.direction;
        currentSpeed = speed;
        moving = true;

        game.entityManager.addMovingEntityToMap(this);
    }

    @Override
    public int magicCost() {
        return 2;
    }

    @Override
    protected void action() {
        if (!isMoving())
            game.entityManager.removeMovingEntityFromMap(this);
    }

    @Override
    public void entityInteraction(Entity entity) {
        if (entity instanceof Player) {
            currentSpeed = speed;
            moving = true;
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
