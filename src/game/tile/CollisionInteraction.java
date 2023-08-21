package game.tile;

import game.visual.MovingEntity;


// @TODO: interactive tiles, also update the collisionChecker for attack to check tiles as well
public interface CollisionInteraction {
    void entityInteraction(MovingEntity entity, int i, int j);
}
