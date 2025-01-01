package game.projectile;

import Utility.ImageAnchor;
import Utility.UtilityTool;
import Utility.Vector2D;
import game.character.Character;
import game.character.Direction;
import game.player.Player;
import game.visual.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import static game.main.GamePanel.*;

public class Arrow extends Projectile {
    static private final ImageAnchor[][] images = new ImageAnchor[4][1];
    static {
        BufferedImage image0 = UtilityTool.loadScaledImage(imageFolder + "arrow.png");
        BufferedImage image = new BufferedImage(tileSize, tileSize, image0.getType());

        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.drawImage(image0, 0, 2, null);
        g2d.dispose();

        double[] angles = {0, Math.PI / 2, Math.PI, 3 * Math.PI / 2};
        for (int i = 0; i < 4; i++) {
            images[i][0] = new ImageAnchor(new BufferedImage(tileSize, tileSize, image.getType()));
            g2d = (Graphics2D) images[i][0].image().getGraphics();
            g2d.rotate(angles[i], (double) tileSize / 2, (double) tileSize / 2);
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
        }
    }

    public final int damage;
    public static final double speed = 5.5;

    private int ctr = 0;
    private Character target = null;
    private final Vector2D anchor = new Vector2D();

    static private Rectangle getBBox(Direction dir) {
        Rectangle rect = null;
        switch (dir) {
            case RIGHT -> rect = new Rectangle(2 * scale, 20, 10 * scale, 8);
            case LEFT -> rect = new Rectangle(4 * scale, 20, 10 * scale, 8);
            case DOWN -> rect = new Rectangle(20, 2*scale, 8, 10*scale);
            case UP -> rect = new Rectangle(20, 4*scale, 8, 10*scale);
        }
        return rect;
    }

    public Arrow(Character source) {
        super(getBBox(source.getDirection()), source, speed, source.getExactDirection());
        spriteImages = images;
        nbSprites = 1;

        damage = source.strength;
    }

    @Override
    protected void action() {
        if (!isMoving()) {
            if (target != null) {
                setPosition(target.getX() + anchor.x, target.getY() + anchor.y);
            }
            ctr++;
            if (ctr > FPS/2)
                game.entityManager.removeMovingEntityFromMap(this);
        }
    }

    @Override
    public void entityInteraction(Entity entity) {
        if (entity instanceof Player) {
            setCurrentSpeed(speed);
            moving = true;
        } else if (entity instanceof Character character) {
            target = character;
            anchor.x = getX() - target.getX();
            anchor.y = getY() - target.getY();
            character.receiveDamageFrom(damage, source);
        }
    }
}
