package game.projectile;

import Utility.Vector2D;
import game.character.Character;
import game.visual.MovingEntity;

import static game.main.GamePanel.game;

import java.awt.*;
import java.util.function.Supplier;

public abstract class Projectile extends MovingEntity {
    static protected String imageFolder = "/effects/";

    protected Character source;
    protected final double speed;

    private boolean isFired = false;

    protected Projectile(Rectangle BBox, Character source, double speed) {
        super(BBox);
        this.source = source;
        this.speed = speed;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    protected void invisibleAction() {
        game.entityManager.removeMovingEntityFromMap(this);
    }

    protected void fire(double direction) {
        assert !isFired;

        // UnCollide
        double vx_n = Math.cos(direction), vy_n = Math.sin(direction);
        setPosition(source.getX(), source.getY());

        Supplier<Boolean> hasUnCollided = () -> {
            Rectangle nextBBox = getBBox(vx_n, vy_n);
            return !source.getBBox().intersects(nextBBox);
        };

        while (!hasUnCollided.get()) {
            move(vx_n, vy_n);
        }

        setDirection(direction);
        setCurrentSpeed(speed);
        moving = true;
        collisionExceptions.add(source);

        game.entityManager.addMovingEntityToMap(this);
        isFired = true;
    }

    // ### UTILS ###
    static public double angleTowards(Character source, Character target) {
        Vector2D vec = Vector2D.sub(target.getCenterPos(), source.getCenterPos());
        return Math.atan2(vec.y, vec.x);
    }
}
