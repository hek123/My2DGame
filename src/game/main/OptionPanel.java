package game.main;

import main.Main;
import main.Sound;

import javax.swing.*;
import java.awt.*;

import static game.main.GamePanel.game;

// TODO: keybindings
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
        frame.setBackground(Color.lightGray);
        GridBagConstraints c = new GridBagConstraints();

        Button resume = new OptButton("Resume");
        resume.addActionListener(e -> game.toggleOptions());
        Button saveQuit = new OptButton("Save & Quit");
        saveQuit.addActionListener(e -> Main.goToMainMenu());
        c.gridx = 0;
        c.gridy = 1;
        frame.add(resume, c);
        c.gridy++;
        frame.add(saveQuit, c);

        JLabel volumeText = new JLabel("Music: ");
        c.gridx = 0;
        c.gridy = 0;
        frame.add(volumeText, c);
        JSlider volumeSlider = new OptSlider(0, 100, 87);
        volumeSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            Sound.setVolume(source.getValue());
        });
        c.gridx = 1;
        frame.add(volumeSlider, c);

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
    static class OptSlider extends JSlider {
        OptSlider(int min, int max, int val) {
            super(min, max, val);
            setFocusable(false);
        }
    }
}
