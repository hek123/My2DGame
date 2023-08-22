package Utility;

import game.main.GamePanel;
import game.tile.TileManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class UtilityTool {
    static public BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return scaledImage;
    }

    static public BufferedImage loadImage(String path) throws IOException {
        Class<UtilityTool> classLoader = UtilityTool.class;
        // load the image
        return ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(path)));
    }

    static public BufferedImage loadScaledImage(String path) {
        return loadScaledImage(path, GamePanel.tileSize, GamePanel.tileSize);
    }
    static public BufferedImage loadScaledImage(String path, int width, int height) {
        try {
            // load and scale the image to the correct format
            return UtilityTool.scaleImage(loadImage(path), width, height);
        } catch (IOException e) {
            System.out.println(path);
            throw new RuntimeException(e);
        }
    }
    static public BufferedImage loadAutoScaledImage(String path) {
        try {
            // load the image
            BufferedImage image = loadImage(path);
            int width = image.getWidth(), height = image.getHeight();
            // and scale it to the correct format
            image = UtilityTool.scaleImage(image, width * GamePanel.scale, height * GamePanel.scale);
            return image;
        } catch (IOException e) {
            System.out.println(path);
            throw new RuntimeException(e);
        }
    }

    static public ImageIcon loadIcon(String path) {
        URL imgURL = UtilityTool.class.getResource(path);
        return new ImageIcon(Objects.requireNonNull(imgURL));
    }
    static public ImageIcon loadIcon(String path, int width, int height) {
        return new ImageIcon(loadScaledImage(path, width, height));
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

    static public BufferedImage rotateImage(BufferedImage image, double angle) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        Graphics2D g2d = (Graphics2D) out.getGraphics();
        int centerX = image.getWidth() / 2, centerY = image.getHeight() / 2;
        g2d.rotate(angle, centerX, centerY);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return out;
    }
}
