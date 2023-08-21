package game.main;

import game.character.monster.*;
import game.character.npc.OldMan;
import game.object.*;
import game.object.weapon.*;
import game.visual.Entity;

import java.awt.*;

import static game.main.GamePanel.*;

public class AssetSetter {
    Game game;

    AssetSetter(Game game) {
        this.game = game;
    }

    private void setEntity(int x, int y, Class<? extends Entity> cClass, Object... initArgs) {
        Entity.createEntity(x * tileSize, y * tileSize, cClass, initArgs);
    }

    public void setDefaultObjects() {
        for (int[] pos : new int[][]{{23, 7}, {23, 40}, {38, 9}})
            setEntity(pos[0], pos[1], ObjectKey.class);

        for (int[] pos: new int[][]{{10, 11}, {8, 28}, {12, 22}})
            setEntity(pos[0], pos[1], ObjectDoor.class);

        setEntity(10, 7, ObjectChest.class);
        setEntity(35, 41, ObjectBoots.class);

        setEntity(23, 22, Sword.class);
        setEntity(25, 23, Axe.class);
    }

    public void setNPC() {
        final int a = 3;
        setEntity(21, 21, OldMan.class, new Rectangle((23 - a) * tileSize, (21 - a) * tileSize, (2*a+1) * tileSize, (2*a+1) * tileSize));
    }

    public void setMonster() {
        final int a = 2;
        setEntity(22, 22, GreenSlime.class, new Rectangle((23 - a) * tileSize, (21 - a) * tileSize, (2*a+1) * tileSize, (2*a+1) * tileSize));
        setEntity(20, 36, GreenSlime.class);
        setEntity(11, 30, GreenSlime.class);

        setEntity(25, 20, SkeletonLord.class);
    }
}
