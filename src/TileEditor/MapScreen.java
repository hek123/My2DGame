package TileEditor;

import Utility.Vector2D;
import game.tile.TileMap;
import main.BaseKeyAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import static game.main.GamePanel.tileSize;

public class MapScreen extends JPanel {
    static private final String mapsFolder = "customMaps/";
    private final int scale = 3;

    String file;
    TileMap tileMap;

    MapScreen() {
        super(new GridBagLayout(), true);
//        setBackground(Color.black);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        c.weightx = c.weighty = .8;
        c.fill = GridBagConstraints.BOTH;

        add(new MapEdit(), c);

        JButton saveButton = new TEButton("Save");
        saveButton.addActionListener(e -> System.out.println("save"));
        c.gridx = 1;
        c.weightx = .2;
        c.weighty = .8;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets.top = 20;
        add(saveButton, c);

        JPanel tilePanel = new TilePanel();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.;
        c.weighty = .2;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets.top = 0;
        add(tilePanel, c);

        addKeyListener(new BaseKeyAdapter());
    }

    public void newMap(String name, int width, int height) {
        System.out.println("Create new Map '" + name + "' with dimensions " + width + "x" + height);

        file = name + ".txt";

        tileMap = TileMap.emptyTileMap(width, height);
    }

    public void loadMap(String file) throws IOException {
        this.file = file;
        tileMap = TileMap.loadTileMap(file);
    }

    public void saveMap() throws IOException {
        TileMap.saveJSONTileMap(file, tileMap);
    }

    class MapEdit extends JPanel {
        Rectangle visible = new Rectangle();
        final Vector2D selectedTile = new Vector2D();
        final Stroke stroke = new BasicStroke(3);

        MapEdit() {
            super(null);
            setFocusable(true);
            setEnabled(true);
            setVisible(true);

            setBackground(Color.cyan);

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    Dimension dimension = e.getComponent().getSize();
                    visible.width = dimension.width;
                    visible.height = dimension.height;
                    System.out.println("map dim = " + dimension);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            tileMap.paintWindow(g2d, visible);

            g2d.setStroke(stroke);
            if (hasFocus()) {
                g2d.setColor(Color.black);
                g2d.drawRect(0, 0, visible.width, visible.height);
            }

            g2d.setColor(Color.blue);
            g2d.drawRect(selectedTile.x * tileSize - visible.x, selectedTile.y * tileSize - visible.y, tileSize, tileSize);
        }
    }

    class TilePanel extends JPanel {
        TilePanel() {
            super(null);
            setBackground(Color.orange);
        }
    }
}