package game.ui.itemUI;

import game.item.Item;
import game.ui.Scalable;

import java.awt.*;

import static game.main.GamePanel.*;

public class ItemBar implements Scalable {
    public Color background = new Color(0, 0, 0, 200);
    public Color border = new Color(200, 200, 200);
    public Color foreground = Color.white;
    public Stroke borderWidth = new BasicStroke(2);
    public Font font = new Font("Arial", Font.BOLD, 4 * scale);

    private final int slotSize = tileSize;
    private final int nbSlots = 6;
    private final int width = 6 * slotSize, height = slotSize;
    private int x, y;


    public ItemBar() {
        scalableList.add(this);
    }

    public void draw(Graphics2D g2d) {
        int x = this.x, y = this.y;

        g2d.setColor(background);
        g2d.fillRect(x, y, width, height);

        g2d.setColor(border);
        g2d.setStroke(borderWidth);
        for (int i = 0; i < nbSlots; i++, x += slotSize) {
            // draw slot border
            g2d.drawRect(x, y, slotSize, height);

            // draw slot item
            if (game.player.inventory.peekSlot(i) != null) {
                g2d.drawImage(game.player.inventory.peekSlot(i).getSlotItemImage(),
                        x + (slotSize - Item.imageSize) / 2, y + (slotSize - Item.imageSize) / 2, null);

                g2d.setColor(foreground);
                g2d.setFont(font);
                g2d.drawString(String.valueOf(game.player.inventory.nbItems(i)), x + 3, y - 3 + slotSize);
            }
        }
    }

    @Override
    public void set(Dimension dimension) {
        x = (dimension.width - nbSlots * slotSize) / 2;
        y = dimension.height - slotSize;
    }
}
