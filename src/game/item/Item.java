package game.item;

import game.main.GamePanel;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public interface Item extends Named {
    int imageSize = (int) (.8 * GamePanel.tileSize);

    @Override
    default @NotNull String getName() {
        return this.getClass().getName();
    }

    default int getSlotCapacity() {
        return 16;
    }

    BufferedImage getItemImage();
}
