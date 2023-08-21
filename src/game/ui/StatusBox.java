package game.ui;

import Utility.UtilityTool;
import Utility.Vector2D;
import game.player.Player;
import game.tile.TileManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.function.Function;

import static game.main.GamePanel.*;

public class StatusBox extends subWindow {
    public static final int fontSize = (int) (.4 * tileSize);
    public static final Font dialogueFont = new Font("Arial", Font.PLAIN, fontSize);
    public Color foreground = Color.white;

    int marginY = (int) (.1 * tileSize);
    int baseWidth;

    final String[] fieldNames;
    final String[] values;

    public StatusBox() {
        background = new Color(100, 100, 0, 200);
        borderColor = new Color(150, 30, 30);
        arc = 20;
        borderArc = arc - 5;
        margin = 4;

        fieldNames = new String[]{"level", "health", "magic", "speed", "strength", "dexterity", "weapon", "shield", "spell"};
        values = new String[fieldNames.length];

        setDimensions();
    }

    public void init() {
        values[0] = Integer.toString(game.player.level);
        values[1] = game.player.getHealth() + " / " + game.player.getMaxHealth();
        values[2] = game.player.magic + " / " + game.player.maxMagic;
        values[3] = Double.toString(game.player.speed);
        values[4] = Integer.toString(game.player.strength);
        values[5] = Integer.toString(game.player.dexterity);
        values[6] = (game.player.weapon[0] == null) ? "NA" : game.player.weapon[0].getName();
        values[7] = (game.player.shield[0] == null) ? "NA" : game.player.shield[0].getName();
        values[8] = (game.player.magicSpell == null) ? "NA" : game.player.magicSpell.getName();

        width = baseWidth + getWidth(values);
        setPosition(game.player);
    }

    private void setDimensions() {
        height = (tileSize * (fieldNames.length + 1)) / 2;
        width = getWidth(fieldNames);
        width += 2 * tileSize / 3 + tileSize / 2;
        baseWidth = width;
    }

    private static int getWidth(String[] text) {
        Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR).getGraphics();
        g2d.setFont(dialogueFont);

        int width = 0;
        for (String s : text) {
            width = Math.max(width, UtilityTool.getTextWidth(s, g2d));
        }
        return width;
    }

    private void setPosition(Player player) {
        x = player.getX() - game.tileManager.framePosition.x - width - tileSize / 5;
        y = player.getY() - game.tileManager.framePosition.y - height / 2 + tileSize / 2;
    }

    public void draw(Graphics2D g2d) {
        super.draw(g2d);

        int x = this.x, y = this.y;

        g2d.setColor(foreground);
        g2d.setFont(dialogueFont);
        x += tileSize / 3;
        y += marginY;

        for (int i = 0; i < fieldNames.length; i++) {
            y = drawLine(g2d, fieldNames[i], values[i], x, y);
        }
    }

    private int drawLine(Graphics2D g2d, String line, String value, int x, int y) {
        y += tileSize / 2;
        g2d.drawString(line, x, y);
        x += baseWidth - 2 * tileSize / 3;
        g2d.drawString(value, x, y);
        return y;
    }
}
