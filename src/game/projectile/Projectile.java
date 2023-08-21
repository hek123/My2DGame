package game.projectile;

import game.visual.MovingEntity;

import static game.main.GamePanel.game;

import java.awt.*;

public abstract class Projectile extends MovingEntity {
    static protected String imageFolder = "/effects/";

    protected Projectile(Rectangle BBox) {
        super(BBox);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    protected void invisibleAction() {
        game.entityManager.removeMovingEntityFromMap(this);
    }
}
