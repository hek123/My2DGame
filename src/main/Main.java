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

    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel cardPanel = new JPanel(cardLayout);

    private static final MainPanel mainPanel = new MainPanel();

    private static final JLayeredPane layeredPane = new JLayeredPane();
    public static final GamePanel gamePanel = new GamePanel();
    public static final OptionPanel optionPanel = new OptionPanel();

    private static final String game = "game", main = "main";

    Main() {
        super("2D Adventure");

        setMinimumSize(initialSize);

        cardPanel.setPreferredSize(initialSize);

        cardPanel.add(mainPanel, main);

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

        goToMainMenu();
    }

    private static void showPanel(String card_panel) {
        cardLayout.show(cardPanel, card_panel);
    }

    public static void goToMainMenu() {
        showPanel(main);
        mainPanel.requestFocus();

        gamePanel.stopGame();
    }

    public static void startGame() {
        showPanel(game);
        layeredPane.requestFocus();

        gamePanel.startNewGame();
    }

    public static void exit() {
        gamePanel.stopGame();

        System.out.println("exit");
        System.exit(0);
    }
}


class MainPanel extends JPanel {

    MainPanel() {
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setOpaque(true);
        setBackground(Color.yellow);
        addKeyListener(new BaseKeyAdapter());
        setFocusable(true);

        JLabel title = new JLabel("My 2D Adventure!", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setFocusable(false);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.insets = new Insets(50, 0, 0, 0);
        add(title, c);

        JComponent center = createCenterButtons();

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = .5;
        c.weighty = 1.;
        c.anchor = GridBagConstraints.CENTER;
        add(center, c);
    }

    JComponent createCenterButtons() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setFocusable(false);
        center.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        Button play = new MainButton("Play");
        play.addActionListener(e -> Main.startGame());
        Button settings = new MainButton("Settings");
        Button exit = new MainButton("Exit");
        exit.addActionListener(e -> Main.exit());
        c.gridx = 0;
        c.gridy = 0;
        center.add(play, c);
        c.gridy++;
        center.add(settings, c);
        c.gridy++;
        center.add(exit, c);
        c.gridy++;
        c.insets = new Insets(10, 0, 0, 0);
        center.add(new MainButton("TileEditor"), c);
        return center;
    }

    static class MainButton extends Button {
        MainButton(String text) {
            super(text);
            setFocusable(false);
        }
    }
}
