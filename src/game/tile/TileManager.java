package game.tile;

import Utility.Vector2D;
import com.opencsv.exceptions.CsvValidationException;
import game.main.Game;
import game.ui.Scalable;

import java.awt.*;
import java.io.IOException;

import static game.main.GamePanel.*;


public class TileManager implements Scalable {
    public final TileMap tileMap;

    public Vector2D framePosition = new Vector2D();
    static private final Vector2D screenCenter = new Vector2D();

    static public final Rectangle visible = new Rectangle();

    public TileManager(Dimension maxDim) {
        scalableList.add(this);
        tileMap = new TileMap();
        try {
            tileMap.loadTileMap("/maps/world01.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        int x = game.player.getX() - screenCenter.x;
        int y = game.player.getY() - screenCenter.y;

        if (x < 0) x = 0;
        if (x > Game.maxWorldCol * tileSize - visible.width)
            x = Game.maxWorldCol * tileSize - visible.width;
        if (y < 0) y = 0;
        if (y > Game.maxWorldRow * tileSize - visible.height)
            y = Game.maxWorldRow * tileSize - visible.height;

        visible.x = framePosition.x = x;
        visible.y = framePosition.y = y;
    }

    public void draw(Graphics2D g2d, Vector2D framePosition) {
        int colStart = Math.max(0, visible.x / tileSize), colEnd = Math.min(Game.maxWorldCol - 1, (visible.x + visible.width) / tileSize);
        int rowStart = Math.max(0, visible.y / tileSize), rowEnd = Math.min(Game.maxWorldRow - 1, (visible.y + visible.height) / tileSize);

        for (int col = colStart; col <= colEnd; col++) {
            int x = col * tileSize - framePosition.x;
            for (int row = rowStart; row <= rowEnd; row++) {
                int y = row * tileSize - framePosition.y;
                try {
                    g2d.drawImage(tileMap.getTile(col, row).image, x, y, null);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.out.println("row, col = " + row + ", " + col);
                    System.exit(-1);
                }
            }
        }

        // draw Grid
        if (game.ui.debugInfo) {
            g2d.setColor(new Color(180, 180, 180, 200));

            for (int col = colStart; col <= colEnd; col++) {
                int x = col * tileSize - visible.x;
                g2d.drawLine(x, 0, x, visible.height);
            }
            for (int row = rowStart; row <= rowEnd; row++) {
                int y = row * tileSize - visible.y;
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
