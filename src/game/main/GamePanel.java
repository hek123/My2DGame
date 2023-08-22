package game.main;

import Utility.UtilityTool;
import Utility.Vector2D;
import game.ui.MouseHandler;
import game.ui.Scalable;
import main.Sound;

import javax.crypto.Cipher;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeListener;


public class GamePanel extends JPanel implements Scalable {
    // SCREEN SETTINGS
    static public final int originalTileSize = 16; // 16x16 game.tile
    static public final int scale = 3;
    static public final int tileSize = originalTileSize * scale; // 48x48 game.tile

    static public final int FPS = 60;
    static public double fps;

    static public final Dimension initialSize = new Dimension(16 * tileSize, 12 * tileSize);

    // GRAPHICS
    private final JButton pauseButton;

    // BACKGROUND STUFF
    static public final MyKeyHandler keyHandler = new MyKeyHandler();
    static public final MouseHandler mouseHandler = new MouseHandler();

    static public Game game;

    public GamePanel() {
        super(true);  // Provides better rendering performance
        setLayout(null);

        setPreferredSize(initialSize);
        setMinimumSize(initialSize);

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

        pauseButton = new JButton(UtilityTool.loadIcon("/pauseButton.png"));
        System.out.println(pauseButton.getPreferredSize());
        pauseButton.setSize(24, 24);
        pauseButton.setBorder(null);
        pauseButton.addActionListener((e) -> game.toggleOptions());
        pauseButton.setFocusable(false);
        add(pauseButton);

//        JLabel lb = new JLabel();
//        lb.setOpaque(true);
//        lb.setBackground(Color.red);
//        lb.setBounds(100, 100, 200, 200);
//        add(lb);

        mouseHandler.gamePanel = this;

        Sound.loadSound();

        scalableList.add(this);
    }

    public void startNewGame() {
        assert game == null;
        game = new Game();
        game.startGame();
        System.out.println("started new game");

        Sound.playMusic(Sound.BBA);
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
        if (Game.gameState != GameState.FINISHED) {
            Graphics2D g2d = (Graphics2D) g;

            game.draw(g2d, new Vector2D(game.tileManager.framePosition));
            super.paintChildren(g);

            g2d.dispose(); // Good practice to save some memory
        }
    }

    @Override
    public void set(Dimension dimension) {
        pauseButton.setLocation(dimension.width - pauseButton.getWidth() - 5, 5);
    }
}
