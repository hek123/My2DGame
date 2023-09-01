package game.object;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import Utility.Vector2D;
import game.character.Character;
import game.player.Player;

import java.awt.*;

import static game.main.GamePanel.*;


public class ObjectDoor extends SuperObject {
    static private final ImageAnchor image = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "door.png"));
    int life = 3;

    public ObjectDoor() {
        super(image);
    }

    @Override
    public void getObject(Player player) {
        if (player.inventory.getItem(ObjectKey.class.getName()) != null) {
            game.entityManager.removeEntityFromMap(this);
        } else {
            game.ui.dialogue("need a key to open door!", this.getBBox(), player.getDirection());
        }
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public void attackedBy(Character character) {
        if (character instanceof Player player) {
            if (player.weapon[0] != null && player.weapon[0].getName().equals("Axe")) {
                life--;
                shakeCtr = FPS / 2;
            }
        }
    }

    private int shakeCtr = 0;

    @Override
    public void updateA() {
        if (shakeCtr > 0) {
            if (shakeCtr % (FPS / 20) == 0) {
                image.anchor().x = (int) ((Math.random() - .5) * 4);
                image.anchor().y = (int) ((Math.random() - .5) * 4);
            }
            shakeCtr--;
        } else if (life <= 0) {
            game.entityManager.removeEntityFromMap(ObjectDoor.this);
        }
    }

    @Override
    public void drawA(Graphics2D g2d, Vector2D framePos) {
        drawImage(g2d, framePos, image);
    }
}
