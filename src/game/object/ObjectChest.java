package game.object;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import Utility.Vector2D;
import game.player.Player;


public class ObjectChest extends SuperObject {
    static private final ImageAnchor image = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "chest.png"), new Vector2D(0, 0));

    public ObjectChest() {
        super(image);
    }

    @Override
    public void getObject(Player player) {}

    @Override
    public boolean isSolid() {
        return true;
    }
}
