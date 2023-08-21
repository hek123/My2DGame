package game.main;

import Utility.Vector2D;
import game.ui.MouseHandler;
import game.ui.Scalable;
import main.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;


public class GamePanel extends JPanel implements Runnable {
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
    static private GamePanel gamePanel;


    public GamePanel() {
        super(true);  // Provides better rendering performance

        setPreferredSize(initialSize);
        setMinimumSize(initialSize);
        setBackground(Color.black);

        setEnabled(true);

        addKeyListener(keyHandler);
        addMouseListener(mouseHandler);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Scalable.scaleAllWindows(e.getComponent().getSize());
            }
        });

        setFocusable(false);

        mouseHandler.gamePanel = this;

        Sound.loadSound();

        gamePanel = this;
    }

    public void startNewGame() {
        game = new Game();
        game.startGame(gamePanel);

//        Sound.playMusic(0);
    }

    public void stopGame() {
        Sound.stopMusic();
        game.stopGame();
    }

    @Override
    public void run() {
//        final long drawInterval = 1_000_000_000 / FPS;  // nanoseconds
//        long nextDrawTime = System.nanoTime() + drawInterval;
//        long remainingTime;
//
//        long oldTimer = nextDrawTime;
//        int ctr = 1;
//
//        while (Game.gameState != GameState.FINISHED) {
            // 1) UPDATE: update information such as character position
            game.update();
            if (!OptionPanel.optionPanel.visible)
                if (keyHandler.isKeyClicked(KeyEvent.VK_M))
                    OptionPanel.optionPanel.showO();

            // 2) DRAW: draw the screen with the updated information
            repaint();

//            // 3) Timer
//            try {
//                remainingTime = nextDrawTime - System.nanoTime(); // nanoseconds
//
//                if (ctr > FPS / 2) {
//                    fps = (FPS / 2) * 1e9 / (nextDrawTime - oldTimer);
//                    ctr = 0;
//                    oldTimer = nextDrawTime;
//                }
//                ctr++;
//
//                if (remainingTime < 0) {
//                    nextDrawTime += drawInterval - remainingTime;
//                } else {
//                    Thread.sleep(remainingTime / 1_000_000); // milliseconds
//                    nextDrawTime += drawInterval;
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        game = null;
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
