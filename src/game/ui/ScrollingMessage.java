package game.ui;

import game.main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ScrollingMessage {
    public int fontSize = (int) (.4 * GamePanel.tileSize);
    public Font messageFont = new Font("Arial", Font.PLAIN, fontSize);
    public Stroke stroke = new BasicStroke(3);
    public Color shadow = Color.black;
    public Color foreground = Color.white;


    private final List<String> dialogue = new ArrayList<>();
    private final List<Integer> msgCounter = new ArrayList<>();

    int x = GamePanel.tileSize / 2, y = 3 * GamePanel.tileSize / 2;

    public void addMessage(String message) {
        dialogue.add(message);
        msgCounter.add(0);
    }

    public void draw(Graphics2D g2d) {
        // Dialogue Window
        g2d.setStroke(stroke);
        g2d.setFont(messageFont);

        int i = 0;
        int y = this.y;
        while (i < dialogue.size()) {
            y += GamePanel.tileSize / 2;
            // shadow
            g2d.setColor(shadow);
            g2d.drawString(dialogue.get(i), x + 2, y + 2);
            g2d.setColor(foreground);
            g2d.drawString(dialogue.get(i), x, y);

            msgCounter.set(i, msgCounter.get(i) + 1);

            if (msgCounter.get(i) >= 180) {
                dialogue.remove(i);
                msgCounter.remove(i);
            } else {
                i++;
            }
        }
    }
}
