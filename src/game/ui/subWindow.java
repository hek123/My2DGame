package game.ui;

import java.awt.*;
import java.util.Arrays;

class subWindow {
    Stroke stroke = new BasicStroke(4);
    Color background = new Color(0, 0, 0, 200);
    Color borderColor = Color.white;

    int x, y, width, height;
    int arc = 0, borderArc = 0, margin = 0;

    void draw(Graphics2D g2d) {
        if (background != null) {
            g2d.setColor(background);
            g2d.fillRoundRect(x, y, width, height, arc, arc);
        }

        if (borderColor != null) {
            g2d.setColor(borderColor);
            g2d.setStroke(stroke);
            g2d.drawRoundRect(x + margin, y + margin, width - 2*margin, height - 2*margin, borderArc, borderArc);
        }
    }
}
