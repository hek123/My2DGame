package game.visual.animations;

import Utility.Vector2D;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public interface Animation {
    @NotNull Animation updateA();

    void drawA(Graphics2D g2d, Vector2D framePos);

    int getLayerLevel();

    boolean isVisible(Rectangle screenBounds);
}
