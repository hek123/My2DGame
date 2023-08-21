package game.main;

import Utility.Vector2D;
import game.player.Player;
import game.event.EventHandler;
import game.tile.TileManager;
import game.ui.Scalable;
import game.ui.UI;
import game.visual.EntityManager;
import main.Main;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static game.main.GamePanel.*;
import static main.Main.gamePanel;
import static main.Main.optionPanel;


public class Game {
    // WORLD SETTINGS
    static public final int maxWorldCol = 50, maxWorldRow = 50;

    public TileManager tileManager;
    public Player player;
    public UI ui;
    public EntityManager entityManager;
    public EventHandler eventHandler;

    private AssetSetter assetSetter;

    private ScheduledThreadPoolExecutor gameThread;
    public long nanoTime;
    private int fpsUpdateCtr = 0;

    // GAME STATE
    static public @NotNull GameState gameState = GameState.FINISHED;
    static public long gameCounter;

    private void initGame() {
        tileManager = new TileManager(new Dimension(maxWorldCol, maxWorldRow));
        entityManager = new EntityManager();
        eventHandler = new EventHandler();
        player = new Player();
        ui = new UI();

        assetSetter = new AssetSetter(this);
        assetSetter.setDefaultObjects();
        assetSetter.setNPC();
        assetSetter.setMonster();
    }

    public void startGame() {
        initGame();

        System.out.println(gamePanel.getSize());
        Scalable.scaleAllWindows(gamePanel.getSize());

        keyHandler.reset();

        gameState = GameState.PLAY;
        gameCounter = 0;
        tileManager.update();

        gameThread = new ScheduledThreadPoolExecutor(1);
        gameThread.scheduleAtFixedRate(this::gameLoop, 0, 1_000_000_000 / FPS, TimeUnit.NANOSECONDS);
    }

    public void stopGame() {
        entityManager.clearMap();

        gameThread.shutdown();

        gameState = GameState.FINISHED;
    }

    public void gameLoop() {
//        System.out.println("Game loop is Running!");
        // 1) UPDATE: update information such as character position
        update();
        if (!optionPanel.visible)
            if (keyHandler.isKeyClicked(KeyEvent.VK_M))
                optionPanel.showO();
        if (keyHandler.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            System.out.println("esc");
            Main.goToMainMenu();
        }

        // 2) DRAW: draw the screen with the updated information
        gamePanel.repaint();
    }

    void update() {
        if (fpsUpdateCtr == FPS / 2) {
            long time = System.nanoTime();
            fps = ((double) fpsUpdateCtr) / ((time - nanoTime) * 1e-9);
            fpsUpdateCtr = 0;
            nanoTime = time;
        }
        fpsUpdateCtr++;

        if (gameState != GameState.FINISHED) {
            if (gameState == GameState.PLAY) {
                game.player.update();

                game.tileManager.update();

                entityManager.updateMovingEntities();
                entityManager.updateAnimations();

                eventHandler.checkEvent();

                gameCounter++;
            }

            ui.update();
        }
    }

    void draw(Graphics2D g2d, Vector2D framePos) {
        tileManager.draw(g2d, framePos);

        entityManager.drawAnimations(g2d, framePos);

        if (ui.debugInfo) {
            player.drawDebugInfo(g2d, framePos);
            entityManager.drawEntityDebugInfo(g2d, framePos);
        }

        ui.draw(g2d);
    }
}
