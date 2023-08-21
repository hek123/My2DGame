package main;

import TileEditor.TileEditorPanel;
import game.main.GamePanel;
import game.main.OptionPanel;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;


public class Test extends JFrame implements SwitchScreen {
    private JPanel card_panel;
    private JPanel main_menu;
    public JPanel game_panel;
    private JLabel MainTitle;
    private JButton settingsButton;
    private JButton playButton;
    private JButton exitButton;
    private JPanel games;
    private JButton ngButton;
    private JPanel game_selection;
    private JButton tileEditorButton;
    private JPanel tile_editor;

    static public Test jFrame;

    public Test() {
        super();
        jFrame = this;

//        ss = this;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setUndecorated(true);

        setTitle("2D Adventure");
        setContentPane(card_panel);

        pack();
//        switchScreen(game);

        setLocationRelativeTo(null);
        setVisible(true);

        playButton.addActionListener(e -> switchScreen(game));
        exitButton.addActionListener(e -> {
            System.out.println("exit");
            System.exit(0);
        });
        main_menu.addKeyListener(new BaseKeyAdapter());

        tileEditorButton.addActionListener(e -> switchScreen(tileEditor));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        game_panel = new JPanel();
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        layeredPane.add(new GamePanel(), 0);
        layeredPane.add(new OptionPanel(), 1);

        game_panel.add(layeredPane);

        tile_editor = new TileEditorPanel();
    }

    public static void main(String[] args) {
        new Test();
    }

    @Override
    public void switchScreen(String screen) {
//        switch (screen) {
//            case game -> {
//                GamePanel.startNewGame();
//            }
//            case main, tileEditor -> {
//                if (GamePanel.game != null)
//                    GamePanel.stopGame();
//            }
//        }
//        ((CardLayout) card_panel.getLayout()).show(card_panel, screen);
//        switch (screen) {
//            case main -> main_menu.requestFocus();
//            case game -> game_panel.requestFocus();
//            case tileEditor -> tile_editor.requestFocus();
//        }
    }

    @Override
    public void fullScreen() {
        setUndecorated(true);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
    }

    @Override
    public void exitFullScreen() {
        setUndecorated(false);
        setExtendedState(MAXIMIZED_BOTH);
    }
}
