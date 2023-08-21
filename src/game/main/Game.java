package game.main;

import Utility.Vector2D;
import game.player.Player;
import game.event.EventHandler;
import game.tile.TileManager;
import game.ui.Scalable;
import game.ui.UI;
import game.visual.EntityManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static game.main.GamePanel.game;
//import static game.main.GamePanel.jFrame;


public class Game {
    // WORLD SETTINGS
    static public final int maxWorldCol = 50, maxWorldRow = 50;

    public TileManager tileManager;
    public Player player;
    public UI ui;
    public EntityManager entityManager;
    public EventHandler eventHandler;

    private AssetSetter assetSetter;

    Thread gameThread;

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

    public void startGame(GamePanel gamePanel) {
        initGame();

        System.out.println(gamePanel.getSize());
        Scalable.scaleAllWindows(gamePanel.getPreferredSize());

        gameState = GameState.PLAY;
        gameCounter = 0;
        tileManager.update();

        gameThread = new Thread(gamePanel);

        gameThread.start();
    }

    public void stopGame() {
        entityManager.clearMap();

        gameThread = null;
        gameState = GameState.FINISHED;
    }

    public void update() {
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

    public void draw(Graphics2D g2d, Vector2D framePos) {
        tileManager.draw(g2d, framePos);

        entityManager.drawAnimations(g2d, framePos);

        if (ui.debugInfo) {
            player.drawDebugInfo(g2d, framePos);
            entityManager.drawEntityDebugInfo(g2d, framePos);
        }

        ui.draw(g2d);
    }
}
