package game.ui;

import Utility.Vector2D;
import game.character.Direction;
import game.tile.TileManager;

import static game.main.GamePanel.*;

import java.awt.*;

public class Dialogue extends subWindow {
    public int fontSize = (int) (.4 * tileSize);
    public Font dialogueFont = new Font("Arial", Font.PLAIN, fontSize);
    public Color foreground = Color.white;

    public double aspectRation = 3. / 2.;

    private String[] dialogue;

    Dialogue() {
        arc = 20;
        borderArc = arc - 5;
        margin = 4;
    }

    public void setDialogue(String dialogue, Rectangle source, Direction playerDirection) {
        this.dialogue = dialogue.split("\n");
        height = (tileSize * (this.dialogue.length + 1)) / 2;
        width = (int) (aspectRation * height);
        for (String text : this.dialogue) {
            width = Math.max(width, text.length() * tileSize / 4);
        }
        Vector2D pos = new Vector2D(source.x, source.y);
        pos.translate(-game.tileManager.framePosition.x, -game.tileManager.framePosition.y);
        switch (playerDirection) {
            case LEFT -> pos.translate(-width + tileSize / 2, -height);
            case UP, RIGHT -> pos.translate(tileSize / 2, -height);
            case DOWN -> pos.translate(2 * tileSize / 3, -height + tileSize / 3);
        }
        x = pos.x;
        y = pos.y;
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);

        int x = this.x, y = this.y;

        g2d.setFont(dialogueFont);
        g2d.setColor(foreground);
        x += (int) (.4 * tileSize);
        y += (int) (.1 * tileSize);

        for (String line : dialogue) {
            y += tileSize / 2;
            g2d.drawString(line, x, y);
        }
    }
}
