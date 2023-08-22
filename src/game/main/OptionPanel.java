package game.main;

import main.Main;

import javax.swing.*;
import java.awt.*;

import static game.main.GamePanel.game;

public class OptionPanel extends JPanel {

    public boolean visible = false;

    public OptionPanel() {
        super();

        setLayout(new GridBagLayout());
        GridBagConstraints c0 = new GridBagConstraints();
        c0.insets = new Insets(50, 50, 50, 50);
        c0.anchor = GridBagConstraints.CENTER;

        JPanel frame = new JPanel(new GridBagLayout());
        frame.setOpaque(true);
        GridBagConstraints c = new GridBagConstraints();

        Button resume = new OptButton("Resume");
        resume.addActionListener(e -> game.toggleOptions());
        Button saveQuit = new OptButton("Save & Quit");
        saveQuit.addActionListener(e -> Main.goToMainMenu());
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        frame.add(resume, c);
        frame.add(saveQuit, c);

        add(frame, c0);

        setOpaque(false);

        setVisible(false);
        setFocusable(false);
    }

    public void reset() {
        visible = false;
        setVisible(false);
    }

    public void toggle() {
        visible = !visible;
        System.out.println("Set visible: " + visible);
        setVisible(visible);
    }

    static class OptButton extends Button {
        OptButton(String text) {
            super(text);
            setFocusable(false);
        }
    }
}
