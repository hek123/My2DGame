package game.object;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import Utility.Vector2D;
import game.character.Character;
import game.player.Player;
import game.visual.animations.Animation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static game.main.GamePanel.*;


public class ObjectDoor extends SuperObject {
    static private final ImageAnchor image = new ImageAnchor(UtilityTool.loadScaledImage(imageFolder + "door.png"), new Vector2D(0, 0));

    int life = 3;

    @Override
    public ImageAnchor getImage() {
        return image;
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
                animation = new Shaking();
                life--;
            }
        }
    }

    protected class Shaking extends EntityAnimation {
        private int ctr = 0;

        private int dx, dy;

        @Override
        public @NotNull Animation updateA() {
            if (ctr % (FPS / 20) == 0) {
                dx = (int) ((Math.random() - .5) * 4);
                dy = (int) ((Math.random() - .5) * 4);
            }
            if (ctr > FPS / 2) {
                if (life > 0) {
                    animation = new ShowObject(image);
                }
                else game.entityManager.removeEntityFromMap(ObjectDoor.this);
            }
            ctr++;

            return animation;
        }

        @Override
        public void drawA(Graphics2D g2d, Vector2D framePos) {
            g2d.drawImage(image.image(), getX() - image.anchor().x - framePos.x + dx, getY() - image.anchor().y - framePos.y + dy, null);
        }
    }
}
