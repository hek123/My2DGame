package game.visual;

import java.awt.*;


public class ColorBar {
    protected Color emptyColor, fullColor, borderColor;
    protected Stroke stroke;

    protected int x, y;
    protected int width, height;

    public int getX() {return x;}
    public int getY() {return y;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    public void draw(Graphics2D g2d, double progress) {
        if (emptyColor != null) {
            g2d.setColor(emptyColor);
            g2d.fillRoundRect(x, y, width, height, height, height);
        }
        g2d.setColor(fullColor);
        g2d.fillRoundRect(x, y, (int) (width * progress), height, height, height);
        if (stroke != null) {
            assert borderColor != null;
            g2d.setStroke(stroke);
            g2d.setColor(borderColor);
            g2d.drawRoundRect(x, y, width, height, height, height);
        }
    }
}
