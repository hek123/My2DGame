package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class BaseKeyAdapter extends KeyAdapter {
    private final Set<Integer> keysPressed = new HashSet<>();
    private final Set<Integer> waitForKeyRelease = new HashSet<>();

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        keysPressed.add(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            Main.exit();
        }
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        keysPressed.remove(e.getKeyCode());
        waitForKeyRelease.remove(e.getKeyCode());
    }

    public synchronized boolean isKeyPressed(int keyCode) {
        return keysPressed.contains(keyCode);
    }

    public synchronized boolean isKeyClicked(int keyCode) {
        if (keysPressed.contains(keyCode) & !waitForKeyRelease.contains(keyCode)) {
            waitForKeyRelease.add(keyCode);
            return true;
        } else {
            return false;
        }
    }

    public void reset() {
        keysPressed.clear();
        waitForKeyRelease.clear();
    }
}
