package game.main;

import javax.swing.*;
import java.awt.*;

public class OptionPanel extends JPanel {
    public boolean visible;

    public OptionPanel() {
        super();

        setLayout(null);

        setOpaque(true);
        setBackground(Color.lightGray);
        setBounds(10, 10, 100, 100);

        setVisible(false);
        setFocusable(false);
    }

    synchronized public void showO() {
        if (!visible) {
            visible = true;
            System.out.println("Set visible: " + visible);
            setVisible(visible);
            try {
                Thread.sleep(10);
                repaint();
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    synchronized public void hideO() {
        if (visible) {
            visible = false;
            System.out.println("Set visible: " + visible);
            setVisible(visible);
            notify();
        }
    }
}
