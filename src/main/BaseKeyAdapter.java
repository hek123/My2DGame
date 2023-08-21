package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class BaseKeyAdapter extends KeyAdapter {
    private final SwitchScreen ss;
    private final Set<Integer> keysPressed = new HashSet<>();
    private final Set<Integer> waitForKeyRelease = new HashSet<>();

    public BaseKeyAdapter(SwitchScreen switchScreen) {
        super();
        ss = switchScreen;
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        keysPressed.add(e.getKeyCode());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Q -> {
//                int option = JOptionPane.showConfirmDialog(null, "Are you sure?", "Exit?", JOptionPane.YES_NO_OPTION);
//                if (option == 0) {
                    System.out.println("exit");
                    System.exit(0);
//                }
            }
            case KeyEvent.VK_ESCAPE -> {
                ss.switchScreen(ss.main);
            }
//            case KeyEvent.VK_F -> {
//                ss.fullScreen();
//            }
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
}
