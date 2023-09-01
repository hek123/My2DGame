package game.visual.animations;

import Utility.Vector2D;

import java.awt.*;

public interface Animation {
    void updateA();

    void drawA(Graphics2D g2d, Vector2D framePos);

    int getLayerLevel();

    boolean isVisible(Rectangle screenBounds);
}
