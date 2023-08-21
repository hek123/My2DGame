package game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    public boolean mousePressed = false;
    public Point pressedLocation, releasedLocation;

    public JPanel gamePanel;

    @Override
    public void mousePressed(MouseEvent e) {
        pressedLocation = e.getPoint();
        mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        releasedLocation = e.getPoint();
        mousePressed = false;
    }

    public Point getMousePosition() {
        return gamePanel.getMousePosition();
    }
}
