package game.tile;

import Utility.DataStructures.ImmutableMap2D;
import Utility.DataStructures.Solid;
import Utility.Vector2D;
import game.main.Game;
import game.object.ObjectKey;
import game.player.Player;
import game.ui.Scalable;
import game.visual.MovingEntity;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import static game.main.GamePanel.*;


public class TileManager implements ImmutableMap2D<Vector2D>, Scalable {
    static {
        final boolean[] solid = {false, true, true, false, true, false, true};
        for (int i = 0; i <= 6; i++) {
            new Tile(String.valueOf(i), i, solid[i]);
        }
        // door
        new Tile("7", 7,
                (MovingEntity entity, int i, int j) -> {
                    if (entity instanceof Player player) {
                        if (player.inventory.getItem(ObjectKey.class.getName()) != null) {
                            game.tileManager.tileMap[i][j] = TileManager.grass;
                        } else {
                            game.ui.dialogue("need a key to open door!", new Rectangle(i * tileSize, j * tileSize, tileSize, tileSize), player.direction);
                        }
                    }
                },
                true);
    }

    static final public int grass = 0, wall = 1, water = 2, dirt = 3, tree = 4, sand = 5, pine_tree = 6, door = 7;

    public int[][] tileMap;

    public Vector2D framePosition = new Vector2D();
    static private final Vector2D screenCenter = new Vector2D();

    static public final Rectangle visible = new Rectangle();

    public TileManager(Dimension maxDim) {
        scalableList.add(this);
        tileMap = new int[maxDim.width][maxDim.height];

        getTileMap("/maps/world01.txt");
    }

    private void getTileMap(String filePath) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(filePath);
            assert inputStream != null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            for (int row = 0; row < Game.maxWorldRow; row++) {
                String line = bufferedReader.readLine();
                String[] numbers = line.split(" ");
                for (int col = 0; col < Game.maxWorldCol; col++) {
                    int number = Integer.parseInt(numbers[col]);
                    tileMap[col][row] = number;
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
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
                    g2d.drawImage(getElement(col, row).image, x, y, null);
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
    public Tile getElement(int i, int j) {
        return Objects.requireNonNull(Tile.tiles.get(tileMap[i][j]));
    }

    @Override
    public Solid getElement(Vector2D pos) {
        return Objects.requireNonNull(Tile.tiles.get(tileMap[pos.x][pos.y]));
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
