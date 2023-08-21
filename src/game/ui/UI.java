package game.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import Utility.UtilityTool;
import game.character.Direction;
import game.main.Game;
import game.main.GameState;
import game.tile.TileManager;
import game.ui.itemUI.ItemBar;
import game.ui.itemUI.ItemGrabHandler;
import game.visual.ColorBar;

import static game.main.GamePanel.*;


public class UI {
    private final Font pauseFont = new Font("Arial", Font.PLAIN, 80);

    public boolean debugInfo = false;

    private final Dialogue dialogue;
    private final ScrollingMessage message;
    private final StatusBox statusBox;
    private final ItemBar itemBar;
    private final InventoryScreen inventoryScreen;
    private final PlayerXPBar XPBar;
    private final Info info;
    private final MagicBar magicBar;

    public final ItemGrabHandler itemGrabHandler;

    BufferedImage heart_full, heart_half, heart_blank;
    BufferedImage coinImage;

    final int heartSize = 2 * tileSize / 3;
    final int coinSize = 2 * tileSize / 3;

    public UI() {
        itemGrabHandler = new ItemGrabHandler();
        dialogue = new Dialogue();
        itemBar = new ItemBar();
        inventoryScreen = new InventoryScreen(itemGrabHandler);
        message = new ScrollingMessage();
        XPBar = new PlayerXPBar();
        info = new Info();
        statusBox = new StatusBox();
        magicBar = new MagicBar();

        heart_full = UtilityTool.loadScaledImage("/object/heart_full.png", heartSize, heartSize);
        heart_half = UtilityTool.loadScaledImage("/object/heart_half.png", heartSize, heartSize);
        heart_blank = UtilityTool.loadScaledImage("/object/heart_blank.png", heartSize, heartSize);

        coinImage = UtilityTool.loadScaledImage("/object/coin_bronze.png", coinSize, coinSize);
    }

    public void draw(Graphics2D g2d) {
        itemBar.draw(g2d);
        XPBar.draw(g2d);
        if (Game.gameState == GameState.INVENTORY) {
            inventoryScreen.draw(g2d);
            itemGrabHandler.drawGrabbedItem(g2d);
        } else {
            if (debugInfo) info.draw(g2d);
            else drawDecorations(g2d);
            if (Game.gameState == GameState.PLAY)
                message.draw(g2d);
            else if (Game.gameState == GameState.PAUSE)
                drawPauseScreen(g2d);
            else if (Game.gameState == GameState.DIALOGUE)
                dialogue.draw(g2d);
            else if (Game.gameState == GameState.STATUS) {
                statusBox.draw(g2d);
            }
        }
    }

    public void dialogue(String text, Rectangle source, Direction playerDirection) {
        Game.gameState = GameState.DIALOGUE;
        dialogue.setDialogue(text, source, playerDirection);
    }

    public void scrollingMessage(String text) {
        message.addMessage(text);
    }

    public void setStatus() {
        statusBox.init();
    }

    private void drawPauseScreen(Graphics2D g2d) {
        g2d.setFont(pauseFont);

        String text = "PAUSED";
        int x = UtilityTool.getXForCenteredText(text, g2d);
        g2d.drawString(text, x, TileManager.visible.height / 2);
    }

    private void drawDecorations(Graphics2D g2d) {
        // Player health
        int x = heartSize / 2, y = heartSize / 2;
        int tmp = game.player.getHealth();
        for (int i = 0; i < game.player.getMaxHealth() / 2; i++) {
            BufferedImage image;
            switch (tmp) {
                case 0 -> image = heart_blank;
                case 1 -> {
                    image = heart_half;
                    tmp -= 1;
                }
                default -> {
                    image = heart_full;
                    tmp -= 2;
                }
            }
            g2d.drawImage(image, x, y, null);
            x += heartSize;
        }

        // Player Magic
        magicBar.draw(g2d);

        // Player Coin
        x = heartSize / 3;
        y = 3 * heartSize / 2 + magicBar.getHeight() + tileSize / 5;
        g2d.drawImage(coinImage, x, y, null);

        final int fontSize = 20;
        x += coinSize - 3;
        y += coinSize - 8;
        g2d.setFont(new Font("Arial", Font.PLAIN, fontSize));
        g2d.setColor(Color.white);
        g2d.drawString("x " + game.player.coin, x, y);
    }

    public void update() {
        if (keyHandler.isKeyClicked(KeyEvent.VK_A))
            game.ui.debugInfo = !game.ui.debugInfo;
        if (Game.gameState == GameState.INVENTORY) itemGrabHandler.update();
    }

    static class MagicBar extends ColorBar {
        MagicBar() {
            width = 2 * tileSize;
            height = tileSize / 5;

            x = tileSize / 3;
            y = tileSize + tileSize / 10;

            stroke = new BasicStroke(1);
            borderColor = Color.white;

            fullColor = new Color(4, 128, 196);
        }

        public void draw(Graphics2D g2d) {
            super.draw(g2d, (double) game.player.magic / game.player.maxMagic);
        }
    }

    static class PlayerXPBar extends ColorBar implements Scalable {
        private final Font levelFont = new Font("arial", Font.BOLD, tileSize / 3);
        private final Font xpFont;

        PlayerXPBar() {
            scalableList.add(this);

            width = 6 * tileSize;
            height = tileSize / 5;

            stroke = new BasicStroke(1);
            fullColor = new Color(0xBCFF48);
            borderColor = Color.white;

            xpFont = new Font("arial", Font.PLAIN, height);
        }

        @Override
        public void set(Dimension dimension) {
            x = (dimension.width - 6 * tileSize) / 2;
            y = dimension.height - tileSize - height - tileSize / 10;
        }

        public void draw(Graphics2D g2d) {
            super.draw(g2d, (double) game.player.XP / game.player.nextLevelXP);

            g2d.setColor(fullColor);
            g2d.setFont(levelFont);
            g2d.drawString("" + game.player.level, x + width / 2, y - 2);

            g2d.setColor(Color.white);
            g2d.setFont(xpFont);
            g2d.drawString("" + game.player.XP + "/" + game.player.nextLevelXP, x + width / 2, y + height - 1);
        }
    }

    class Info {
        private final Font font = new Font("Arial", Font.PLAIN, 20);

        private final int x = tileSize / 4, y = tileSize / 2;


        public void draw(Graphics2D g2d) {
            g2d.setColor(Color.white);
            g2d.setFont(font);

            int y = this.y;
            g2d.drawString("health: " + game.player.getHealth() + " / " + game.player.getMaxHealth(), x, y);
            y += 20;
            g2d.drawString("magic: " + game.player.magic + " / " + game.player.maxMagic, x, y);
            y += 20;
            g2d.drawString("coin: " + game.player.coin, x, y);
            y += 30;
            g2d.drawString("Pos: " + game.player.getX() / tileSize + " " + game.player.getY() / tileSize, x, y);
            y += 20;
            g2d.drawString("fps: " + fps, x, y);
            y += 20;
        }
    }
}
