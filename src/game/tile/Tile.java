package game.tile;

import Utility.DataStructures.Solid;
import Utility.UtilityTool;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Tile implements Solid {
    public final String name;
    public final int idx;
    public final BufferedImage image;
    public final boolean solid;

    public final CollisionInteraction collisionInteraction;
    public final AttackInteraction attackInteraction = null;

    public Tile(String name, BufferedImage image, boolean solid, int number, CollisionInteraction collisionInteraction) {
        this.name = name;
        this.image = image;
        this.solid = solid;

        idx = number;

        this.collisionInteraction = collisionInteraction;
    }

    public Tile(String name, BufferedImage image, boolean solid, int number) {
        this(name, image, solid, number, null);
    }

    @Override
    public boolean isSolid() {
        return solid;
    }
}
