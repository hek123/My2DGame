package game.character;

import game.visual.ColorBar;

import java.awt.*;

import static game.main.GamePanel.FPS;

public class HPBar extends ColorBar {
    private boolean hpBarOn = false;
    private final int timer;
    private int hpBarCounter = 0;

    int offsetX = 0, offsetY = 0;

    public HPBar(int width, int height, double timer) {
        this.width = width;
        this.height = height;
        emptyColor = new Color(100, 100, 100, 200);
        fullColor = new Color(255, 0, 0, 200);

        this.timer = (int) Math.round(timer * FPS);
        if (timer == 0) hpBarOn = true;
    }

    public HPBar(int width, int height, double timer, int offsetX, int offsetY) {
        this(width, height, timer);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void draw(Graphics2D g2d, double progress, int x, int y) {
        if (hpBarOn) {
            this.x = x + offsetX;
            this.y = y + offsetY;

            super.draw(g2d, progress);

            hpBarCounter++;
            if (hpBarCounter >= timer) {
                hpBarOn = false;
            }
        }
    }

    public void makeVisible() {
        hpBarOn = true;
        hpBarCounter = 0;
    }
}
