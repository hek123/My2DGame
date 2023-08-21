package Utility;

import game.main.GamePanel;
import game.tile.TileManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class UtilityTool {
    static public BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return scaledImage;
    }

    static public BufferedImage loadScaledImage(String path) {
        return loadScaledImage(path, GamePanel.tileSize, GamePanel.tileSize);
    }

    static public BufferedImage loadScaledImage(String path, int width, int height) {
        BufferedImage image = null;
        try {
            Class<UtilityTool> classLoader = UtilityTool.class;
            // load the image
            image = ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(path)));
            // and scale it to the correct format
            image = UtilityTool.scaleImage(image, width, height);
        } catch (IOException e) {
            System.out.println(path);
            e.printStackTrace();
        }
        return image;
    }

    static public BufferedImage loadAutoScaledImage(String path) {
        BufferedImage image = null;
        try {
            Class<UtilityTool> classLoader = UtilityTool.class;
            // load the image
            image = ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(path)));
            int width = image.getWidth(), height = image.getHeight();
            // and scale it to the correct format
            image = UtilityTool.scaleImage(image, width * GamePanel.scale, height * GamePanel.scale);
        } catch (IOException e) {
            System.out.println(path);
            e.printStackTrace();
        }
        return image;
    }

    static public int getTextWidth(String text, Graphics2D g2d) {
        return (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
    }

    static public int getXForCenteredText(String text, Graphics2D g2d) {
        return TileManager.visible.width / 2 - getTextWidth(text, g2d) / 2;
    }

    static public int getRandomPoissonSample(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.;
        int k = 0;
        do {
            k++;
            p *= Math.random();
        } while (p > L);
        return k - 1;
    }
}
