package game.main;

import main.BaseKeyAdapter;

import java.awt.event.KeyEvent;

import static game.main.GamePanel.game;
import static main.Main.optionPanel;


public class MyKeyHandler extends BaseKeyAdapter {
    private final int pauseKey = KeyEvent.VK_P;
    public final int attackKey = KeyEvent.VK_F;
    public final int upKey = KeyEvent.VK_UP, downKey = KeyEvent.VK_DOWN, leftKey = KeyEvent.VK_LEFT, rightKey = KeyEvent.VK_RIGHT;

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
        if (optionPanel.visible) {
            if (isKeyClicked(KeyEvent.VK_M))
                optionPanel.hideO();
        }
    }
}
