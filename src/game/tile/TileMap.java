package game.tile;

import Utility.UtilityTool;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import game.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static game.main.GamePanel.tileSize;


// TODO: interactive tiles in new framework
public class TileMap {
    static private final ArrayList<Tile> tiles = getTiles();

    static private ArrayList<Tile> getTiles() {
        try {
            Reader reader = new InputStreamReader(Objects.requireNonNull(TileMap.class.getResourceAsStream("/tiles/tileProperties.csv")));
            CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(new CSVParserBuilder().withSeparator(' ').build()).build();
            String[] metaInf = csvReader.readNext();
            assert Objects.equals(metaInf[0], "nbTiles");
            int nbTiles = Integer.parseInt(metaInf[1]);
            System.out.println(metaInf[0] + " = " + nbTiles);

            ArrayList<Tile> out = new ArrayList<>(nbTiles);
            System.out.println("Fields: " + Arrays.toString(csvReader.readNext()));
            for (int i = 0; i < nbTiles; i++) {
                String[] tileInfo = csvReader.readNext();
                String fileName = tileInfo[0], tileName = tileInfo[1];
                boolean solid = Boolean.parseBoolean(tileInfo[2]);

                BufferedImage tileImage = UtilityTool.loadScaledImage("/tiles/" + fileName + ".png");
                out.add(new Tile(tileName, tileImage, solid, i));
            }
            return out;
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    static public TileMap emptyTileMap(int width, int height) {
        TileMap out = new TileMap();
        out.setEmptyTileMap(width, height);
        return out;
    }
    static public TileMap loadTileMap(String file) throws IOException {
        if (file.endsWith(".json")) {
            return loadJsonMap(file);
        } else {
            TileMap out = new TileMap();
            MyLoader loader;
            if (file.endsWith(".txt"))
                loader = new TXTLoader();
            else if (file.endsWith(".csv"))
                loader = new CSVLoader();
            else
                throw new IOException("Invalid file format");

            loader.loadFile(file);

            String[] format = loader.getNextLine();

            if (format.length == 2) out.loadFormattedMap(loader, format);
            else out.loadLegacyMap(loader, format);

            loader.close();

            return out;
        }
    }
    static private TileMap loadJsonMap(String file) throws IOException {
        JsonReader jsonReader = new JsonReader(new FileReader(file));
        TileMap out = (new Gson()).fromJson(jsonReader, TileMap.class);
        jsonReader.close();
        return out;
    }

    static public void saveTileMap(String file, TileMap tileMap) throws IOException {
        tileMap.saveFormattedMap(file);
    }
    static public void saveJSONTileMap(String file, TileMap tileMap) throws IOException {
        JsonWriter jsonWriter = new JsonWriter(new FileWriter(file));
        new Gson().toJson(tileMap, TileMap.class, jsonWriter);
        jsonWriter.close();
    }

    private int[][] tileMap;
    private int width, height;

    private void setEmptyTileMap(int width, int height) {
        assert tileMap == null;

        this.width = width;
        this.height = height;

        tileMap = new int[width][height];
        for (int[] x : tileMap) {
            Arrays.fill(x, -1);
        }
    }

    private void loadFormattedMap(MyLoader loader, String[] format) throws IOException {
        assert tileMap == null;

        width = Integer.parseInt(format[0]);
        height = Integer.parseInt(format[1]);
        tileMap = new int[width][height];

        for (int j = 0; j < height; j++) {
            String[] row = loader.getNextLine();
            assert row.length == width;
            for (int i = 0; i < width; i++) {
                tileMap[i][j] = Integer.parseInt(row[i]);
            }
        }
    }
    private void loadLegacyMap(MyLoader loader, String[] firstRow) throws IOException {
        assert tileMap == null;

        ArrayList<int[]> tmp = new ArrayList<>();
        width = 0;
        String[] row = firstRow;
        while (row != null) {
            if (width == 0) width = row.length;
            else assert width == row.length;

            tmp.add(Arrays.stream(row).mapToInt(Integer::parseInt).toArray());
            row = loader.getNextLine();
        }
        height = tmp.size();
        tileMap = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tileMap[i][j] = tmp.get(j)[i];
            }
        }
    }

    private void saveFormattedMap(String file) throws IOException {
        assert tileMap != null;
        CSVWriter csvWriter = new CSVWriter(new FileWriter(file));

        String[] format = new String[]{Integer.toString(width), Integer.toString(height)};
        csvWriter.writeNext(format);

        String[] row = new String[width];
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                row[i] = Integer.toString(tileMap[i][j]);
            }
            csvWriter.writeNext(row);
        }
        csvWriter.close();
    }

    interface MyLoader {
        void loadFile(String file) throws IOException;

        String[] getNextLine() throws IOException;

        void close();
    }

    static class TXTLoader implements MyLoader {
        BufferedReader bufferedReader;

        @Override
        public void loadFile(String file) throws IOException {
            bufferedReader = new BufferedReader(new FileReader(file));
        }

        @Override
        public String[] getNextLine() throws IOException {
            String line =  bufferedReader.readLine();
            if (line == null) return null;
            else return line.split(" ");
        }

        @Override
        public void close() {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class CSVLoader implements MyLoader {
        private CSVReader csvReader;

        @Override
        public void loadFile(String file) throws IOException {
            csvReader = new CSVReader(new FileReader(file));
        }

        @Override
        public String[] getNextLine() throws IOException {
            try {
                return csvReader.readNext();
            } catch (CsvValidationException e) {
                throw new IOException(e);
            }
        }

        @Override
        public void close() {
            try {
                csvReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public final Tile getTile(int i, int j) {
        assert tileMap != null;
        int idx = tileMap[i][j];
        if (idx == -1) return null;
        Tile out = tiles.get(idx);
        assert out.idx == idx;
        return out;
    }
    public void setTile(int i, int j, Tile tile) {
        assert tiles.get(tile.idx) == tile;
        tileMap[i][j] = tile.idx;
    }

    public final int getWidth() {
        return width;
    }
    public final int getHeight() {
        return height;
    }

    public final void paintWindow(Graphics2D g2d, Rectangle window) {
        int colStart = Math.max(0, window.x / tileSize), colEnd = Math.min(Game.maxWorldCol - 1, (window.x + window.width) / tileSize);
        int rowStart = Math.max(0, window.y / tileSize), rowEnd = Math.min(Game.maxWorldRow - 1, (window.y + window.height) / tileSize);

        for (int col = colStart; col <= colEnd; col++) {
            int x = col * tileSize - window.x;
            for (int row = rowStart; row <= rowEnd; row++) {
                int y = row * tileSize - window.y;
                try {
                    Tile tile = getTile(col, row);
                    if (tile == null) drawEmptyTile(g2d, col, row);
                    else g2d.drawImage(tile.image, x, y, null);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.out.println("row, col = " + row + ", " + col);
                    System.exit(-1);
                }
            }
        }
    }

    protected void drawEmptyTile(Graphics2D g2d, int x, int y) {
        final int width = 4;
        g2d.setColor(Color.darkGray);
        g2d.drawRoundRect(x, y, tileSize - 2*width, tileSize - 2*width, width, width);
    }
}
