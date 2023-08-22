package game.projectile;

import Utility.UtilityTool;
import game.visual.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import static game.main.GamePanel.tileSize;

public class GreenFireBall extends Projectile {
    static private final BufferedImage image = UtilityTool.loadScaledImage(imageFolder + "GreenFireball.png");
    private double vx, vy;

    protected GreenFireBall(double speed, double direction) {
        super(new Rectangle(tileSize, tileSize));

        vx = speed * Math.cos(direction);
        vy = speed * Math.sin(direction);
    }

    @Override
    protected void entityInteraction(Entity entity) {

    }

    @Override
    protected void action() {

    }
}
