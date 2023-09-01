package game.visual.animations;

import Utility.Vector2D;
import game.main.Game;
import org.jetbrains.annotations.NotNull;

import static game.main.GamePanel.*;

import java.awt.*;

public class Particle implements Animation {
    private final Color particleColor;
    private final int size;
    private final double lifeTime;

    private final double gravity = .1;
    private double vx, vy;

    private double x, y;

    int ctr = 0;

    public Particle(int size, Color color, double lifeTime) {
        this.size = size;
        this.particleColor = color;
        this.lifeTime = lifeTime;
    }
    private Particle(int x, int y, Particle generator, double direction, double speed) {
        this.size = generator.size;
        this.particleColor = generator.particleColor;
        this.lifeTime = generator.lifeTime;

        this.x = x;
        this.y = y;
        vx = speed * Math.cos(direction);
        vy = speed * Math.sin(direction);

        game.entityManager.addAnimationToMap(this);
    }

    public void generate(int x, int y, double direction, double speed) {
        new Particle(x, y, this, direction, speed);
    }

    @Override
    public @NotNull Animation updateA() {
        x += vx;
        y += vy;
        vy += gravity;

        if ((double) ctr / FPS > lifeTime)
            game.entityManager.removeAnimationFromMap(this);
        ctr++;
        return this;
    }

    @Override
    public void drawA(Graphics2D g2d, Vector2D framePos) {
        g2d.setColor(particleColor);
        g2d.fillRect((int) x - framePos.x, (int) y - framePos.y, size, size);
    }

    @Override
    public int getLayerLevel() {
        return game.tileManager.tileMap.getHeight() * tileSize;
    }

    @Override
    public boolean isVisible(Rectangle screenBounds) {
        return true;
    }
}
