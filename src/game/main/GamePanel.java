package game.main;

import Utility.Vector2D;
import game.ui.MouseHandler;
import game.ui.Scalable;
import main.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class GamePanel extends JPanel {
    // SCREEN SETTINGS
    static public final int originalTileSize = 16; // 16x16 game.tile
    static public final int scale = 3;
    static public final int tileSize = originalTileSize * scale; // 48x48 game.tile

    static public final int FPS = 60;
    static public double fps;

    static public final Dimension initialSize = new Dimension(16 * tileSize, 12 * tileSize);

    // BACKGROUND STUFF
    static public final MyKeyHandler keyHandler = new MyKeyHandler();
    static public final MouseHandler mouseHandler = new MouseHandler();

    static public Game game;

    public GamePanel() {
        super(true);  // Provides better rendering performance

        setPreferredSize(initialSize);
        setMinimumSize(initialSize);
        setBackground(Color.black);

        setEnabled(true);

        addKeyListener(keyHandler);
        addMouseListener(mouseHandler);
//        addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                Scalable.scaleAllWindows(e.getComponent().getSize());
//            }
//        });

        setFocusable(false);

        mouseHandler.gamePanel = this;

        Sound.loadSound();
    }

    public void startNewGame() {
        assert game == null;
        game = new Game();
        game.startGame();
        System.out.println("started new game");

//        Sound.playMusic(0);
    }

    public void stopGame() {
        System.out.println("stop game");
        if (game != null) {
            game.stopGame();
            game = null;
        }
        Sound.stopMusic();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (Game.gameState != GameState.FINISHED) {
            Graphics2D g2d = (Graphics2D) g;

            game.draw(g2d, new Vector2D(game.tileManager.framePosition));

            g2d.dispose(); // Good practice to save some memory
        }
    }
}
