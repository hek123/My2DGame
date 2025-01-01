package game.main;

import main.BaseKeyAdapter;
import main.Main;

import java.awt.event.KeyEvent;

import static game.main.GamePanel.game;


public class MyKeyHandler extends BaseKeyAdapter {
    private final int pauseKey = KeyEvent.VK_P;

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);

        switch (Game.gameState) {
            case PLAY -> {
                if (isKeyClicked(pauseKey)) Game.gameState = GameState.PAUSE;
                if (isKeyClicked(KeyEvent.VK_I)) Game.gameState = GameState.INVENTORY;
                if (isKeyClicked(KeyEvent.VK_S)) {
                    game.ui.setStatus();
                    Game.gameState = GameState.STATUS;
                }
            }
            case PAUSE -> {
                if (isKeyClicked(pauseKey)) Game.gameState = GameState.PLAY;
            }
            case DIALOGUE -> {
                if (isKeyPressed(KeyEvent.VK_ENTER)) Game.gameState = GameState.PLAY;
            }
            case STATUS -> {
                if (isKeyPressed(KeyEvent.VK_ENTER) | isKeyClicked(KeyEvent.VK_S))
                    Game.gameState = GameState.PLAY;
            }
            case INVENTORY -> {
                if (isKeyPressed(KeyEvent.VK_ENTER) | isKeyClicked(KeyEvent.VK_I)) {
                    Game.gameState = GameState.PLAY;
                }
            }
        }
        if (isKeyClicked(KeyEvent.VK_M)) {
            game.toggleOptions();
        }
        if (isKeyClicked(KeyEvent.VK_ESCAPE)) Main.goToMainMenu();
    }

    @Override
    public void reset() {
        super.reset();
    }
}
