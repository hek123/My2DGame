package main;

import game.main.GamePanel;
import game.main.OptionPanel;
import game.ui.Scalable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static game.main.GamePanel.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        new Main();
    }

    CardLayout cardLayout;
    JPanel cardPanel;

    JLayeredPane layeredPane;
    GamePanel gamePanel;
    OptionPanel optionPanel;

    String game = "game", main = "main";

    Main() {
        super("2D Adventure");

        setMinimumSize(initialSize);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setPreferredSize(initialSize);

        MainPanel mainPanel = new MainPanel();
        cardPanel.add(mainPanel, main);

        layeredPane = new JLayeredPane();
        layeredPane.addKeyListener(keyHandler);
        layeredPane.addMouseListener(mouseHandler);
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension dimension = e.getComponent().getSize();
                gamePanel.setSize(dimension);
                Scalable.scaleAllWindows(dimension);
            }
        });
        layeredPane.setFocusable(true);

        gamePanel = new GamePanel();
        optionPanel = new OptionPanel();
        gamePanel.setFocusable(false);
        optionPanel.setFocusable(false);

        layeredPane.add(gamePanel, 0);
        layeredPane.add(optionPanel, 1);

        cardPanel.add(layeredPane, game);

        add(cardPanel);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setEnabled(true);
        setVisible(true);

        showPanel(game);
        layeredPane.requestFocus();

        gamePanel.startNewGame();
    }

    void showPanel(String card_panel) {
        cardLayout.show(cardPanel, card_panel);
    }
}


class MainPanel extends JPanel {

    MainPanel() {
        super();

        setOpaque(true);
        setBackground(Color.yellow);
    }
}
