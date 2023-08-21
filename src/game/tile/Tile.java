package game.tile;

import Utility.DataStructures.Solid;
import Utility.UtilityTool;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Tile implements Solid {
    static private final String tileFolder = "/tiles/";
    public static final Map<Integer, Tile> tiles = new HashMap<>();


    public final BufferedImage image;
    public boolean solid = false;

    public final CollisionInteraction collisionInteraction;
    public final AttackInteraction attackInteraction = null;

    Tile(String tile, int number, CollisionInteraction collisionInteraction) {
        image = UtilityTool.loadScaledImage(tileFolder + tile + ".png");

        assert !tiles.containsKey(number);
        tiles.put(number, this);

        this.collisionInteraction = collisionInteraction;
    }

    Tile(String tile, int number, CollisionInteraction collisionInteraction, boolean solid) {
        this(tile, number, collisionInteraction);
        this.solid = solid;
    }

    Tile(String tile, int number, boolean solid) {
        this(tile, number, null);
        this.solid = solid;
    }

    @Override
    public boolean isSolid() {
        return solid;
    }
}
