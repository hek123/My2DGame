package game.tile;

import Utility.Vector2D;
import game.main.Game;
import game.ui.Scalable;

import java.awt.*;
import java.io.IOException;

import static game.main.GamePanel.*;


public class TileManager implements Scalable {
    static private final Color gridLineColor = new Color(180, 180, 180, 200);
    static private final Stroke gridLineStroke = new BasicStroke(1);

    public final TileMap tileMap;

    public Vector2D framePosition = new Vector2D();
    static private final Vector2D screenCenter = new Vector2D();

    static public final Rectangle visible = new Rectangle();

    public TileManager(String map) {
        scalableList.add(this);
        try {
            tileMap = TileMap.loadTileMap(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        int x = game.player.getX() - screenCenter.x;
        int y = game.player.getY() - screenCenter.y;

        if (x < 0) x = 0;
        if (x > tileMap.getWidth() * tileSize - visible.width)
            x = tileMap.getWidth() * tileSize - visible.width;
        if (y < 0) y = 0;
        if (y > tileMap.getHeight() * tileSize - visible.height)
            y = tileMap.getHeight() * tileSize - visible.height;

        visible.x = framePosition.x = x;
        visible.y = framePosition.y = y;
    }

    public void draw(Graphics2D g2d, Vector2D framePosition) {
        tileMap.paintWindow(g2d, visible, tileSize);

        // draw Grid
        if (game.ui.debugInfo) {
            g2d.setColor(gridLineColor);
            g2d.setStroke(gridLineStroke);

            for (int x = tileSize - visible.x % tileSize; x < visible.width; x += tileSize) {
                g2d.drawLine(x, 0, x, visible.height);
            }
            for (int y = tileSize - visible.y % tileSize; y < visible.height; y += tileSize) {
                g2d.drawLine(0, y, visible.width, y);
            }
        }
    }

    @Override
    public void set(Dimension dimension) {
        visible.width = dimension.width;
        visible.height = dimension.height;

        screenCenter.x = dimension.width / 2 - tileSize / 2;
        screenCenter.y = dimension.height / 2 - tileSize / 2;

        update();
    }
}
