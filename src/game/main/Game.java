package game.main;

import Utility.Vector2D;
import game.player.Player;
import game.event.EventHandler;
import game.tile.TileManager;
import game.ui.Scalable;
import game.ui.UI;
import game.visual.EntityManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.*;

import static main.Main.optionPanel;


public class Game {
    private final GamePanel gamePanel;

    public TileManager tileManager;
    public Player player;
    public UI ui;
    public EntityManager entityManager;
    public EventHandler eventHandler;

    public final ArrayList<DebugInfo> debugInfoArrayList = new ArrayList<>();

    private ScheduledThreadPoolExecutor gameThread;
    private ScheduledFuture<?> scheduledFuture;
    public long nanoTime;
    private int fpsUpdateCtr = 0;

    // GAME STATE
    static public @NotNull GameState gameState = GameState.FINISHED;
    static public long gameCounter;

    // Global variable
    static public Game game = null;

    public Game(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        System.out.println("Create Game object");

        assert game == null;
        game = this;

        initGame();
    }

    private void initGame() {
        System.out.println("Init game");

        tileManager = new TileManager("resources/maps/world01.txt");
        entityManager = new EntityManager();
        eventHandler = new EventHandler(this);
        player = new Player(gamePanel.keyHandler);
        ui = new UI(gamePanel);

        AssetSetter.setDefaultObjects();
        AssetSetter.setNPC();
        AssetSetter.setMonster();
    }

    public void startGame() {
        System.out.println("Start game");

        System.out.println(gamePanel.getSize());
        Scalable.scaleAllWindows(gamePanel.getSize());

        gamePanel.keyHandler.reset();

        gameState = GameState.PLAY;
        gameCounter = 0;
        tileManager.update();

        gameThread = new ScheduledThreadPoolExecutor(1);
        startGameLoop();
    }

    private void startGameLoop() {
        scheduledFuture = gameThread.scheduleAtFixedRate(this::gameLoop, 0, 1_000_000_000 / GamePanel.FPS, TimeUnit.NANOSECONDS);
    }
    private void stopGameLoop() {
        scheduledFuture.cancel(false);
        try {
            scheduledFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } catch (CancellationException e) {
            System.out.println("stopped GameLoop");
        }
    }

    public void stopGame() {
        assert game != null;

        entityManager.clearMap();

        stopGameLoop();

        gameState = GameState.FINISHED;

        game = null;
    }

    public void pauseGame(boolean pause) {
        if (pause) {
            assert !scheduledFuture.isCancelled();
            stopGameLoop();
        } else {
            assert scheduledFuture.isCancelled();
            startGameLoop();
        }
    }

    public void gameLoop() {
//        System.out.println("Game loop is Running!");
        // 1) UPDATE: update information such as character position
        update();

        // 2) DRAW: draw the screen with the updated information
        gamePanel.repaint();
    }

    void update() {
        if (fpsUpdateCtr == GamePanel.FPS / 2) {
            long time = System.nanoTime();
            GamePanel.fps = ((double) fpsUpdateCtr) / ((time - nanoTime) * 1e-9);
            fpsUpdateCtr = 0;
            nanoTime = time;
        }
        fpsUpdateCtr++;

        if (gameState != GameState.FINISHED) {
            if (gameState == GameState.PLAY) {
                player.update();

                tileManager.update();

                entityManager.updateMovingEntities();
                entityManager.updateAnimations();

                eventHandler.checkEvent();

                gameCounter++;
            }

            ui.update();
        }
    }

    void draw(Graphics2D g2d, Vector2D framePos) {
//        System.out.println("Paint");
        tileManager.draw(g2d, framePos);

        entityManager.drawAnimations(g2d, framePos);

        if (ui.debugInfo) {
            for (DebugInfo info : debugInfoArrayList) {
                info.drawDebugInfo(g2d, framePos);
            }
//            player.drawDebugInfo(g2d, framePos);
//            entityManager.drawEntityDebugInfo(g2d, framePos);
        }

        ui.draw(g2d);
    }

    public void toggleOptions() {
        pauseGame(!optionPanel.visible);
        optionPanel.toggle();
    }
}
