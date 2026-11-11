package game.projectile;

import Utility.Vector2D;
import game.character.Character;
import game.event.Damage;
import game.visual.Entity;
import game.visual.MovingEntity;

import static game.main.Game.game;

import java.awt.*;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class Projectile extends MovingEntity {
    static protected String imageFolder = "/effects/";

    protected Character source;
    protected Damage damage;

    protected Projectile(Rectangle BBox, Character source, double speed, double direction) {
        super(BBox);
        this.source = source;
        setCurrentSpeed(speed);
        setDirection(direction);
        this.damage = new Damage(source, projectileDamage(source), 0);

        // UnCollide with source
        double vx_n = Math.cos(direction), vy_n = Math.sin(direction);
        setPosition(source.getX(), source.getY());

        Supplier<Boolean> hasUnCollided = () -> {
            Rectangle nextBBox = getBBox(vx_n, vy_n);
            return !source.getBBox().intersects(nextBBox);
        };

        while (!hasUnCollided.get()) {
            move(vx_n, vy_n);
        }

        moving = true;
        collisionExceptions.add(source);

        game.entityManager.addMovingEntityToMap(this);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    abstract protected int projectileDamage(Character source);

    @Override
    protected void invisibleAction() {
        game.entityManager.removeMovingEntityFromMap(this);
    }

    protected void bounce(Character character) {
        setDirection(getExactDirection() + Math.PI);
        if (!Objects.isNull(character)) {
            collisionExceptions.remove(source);
            collisionExceptions.add(character);
            source = character;
            damage = new Damage(character, damage.physical_damage() + projectileDamage(character), 0);
        }
    }

    // ### UTILS ###
    static public double getAngle(Character source, Character target) {
        Vector2D vec = Vector2D.sub(target.getCenterPos(), source.getCenterPos());
        return Math.atan2(vec.y, vec.x);
    }

    @Override
    protected void entityInteraction(Entity entity) {
        if (collisionExceptions.contains(entity)) {
            System.err.println("Projectile hit Source");
        } else if (entity instanceof Character character) {
            character.receiveDamage(damage);
        }
    }
}
