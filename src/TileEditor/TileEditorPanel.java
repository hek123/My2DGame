package TileEditor;

import main.BaseKeyAdapter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class TileEditorPanel extends JPanel {
    CardLayout cardLayout = new CardLayout();

    JPanel startScreen = new StartScreen();
    JPanel newMapScreen = new NewMapScreen();
    MapScreen mapScreen = new MapScreen();

    public TileEditorPanel() {
        super(false);
        setLayout(cardLayout);

        add(startScreen, "start");
        add(newMapScreen, "new");
        add(mapScreen, "tileEditor");

        try {
            mapScreen.loadMap("resources/maps/world01.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cardLayout.show(this, "tileEditor");
        mapScreen.requestFocus();

        addKeyListener(new BaseKeyAdapter());
    }

    private class StartScreen extends JPanel {
        StartScreen() {
            super(new GridBagLayout());
            setFocusable(false);
            GridBagConstraints c = new GridBagConstraints();

            JLabel title = new JLabel("Welcome to my Tile Editor");
            title.setFont(new Font("Arial", Font.BOLD, 20));
            c.gridx = 0;
            c.gridy = 0;
            add(title, c);

            JButton newMap = new TEButton("Create New Map");
            newMap.addActionListener(e -> cardLayout.show(TileEditorPanel.this, "new"));
            c.gridy = 1;
            add(newMap, c);
        }
    }

    private class NewMapScreen extends JPanel {
        static private final Color invalidFieldColor = Color.orange;
        private int width, height;
        private String name;

        JFormattedTextField widthField, heightField;
        JTextField nameField;

        NewMapScreen() {
            super(new GridBagLayout());
            setFocusable(false);
            GridBagConstraints c = new GridBagConstraints();

            NumberFormat intFormat = NumberFormat.getNumberInstance();
            intFormat.setParseIntegerOnly(true);

            JPanel center = new JPanel(new GridBagLayout());
            center.setOpaque(true);
            center.setBackground(Color.yellow);

            c.gridx = 0;
            c.gridy = 0;
            JLabel nameLabel = new JLabel("Name: ");
            JLabel widthLabel = new JLabel("Width: ");
            JLabel heightLabel = new JLabel("Height: ");
            center.add(nameLabel, c);
            c.gridy = GridBagConstraints.RELATIVE;
            center.add(widthLabel, c);
            center.add(heightLabel, c);

            c.gridx = 1;
            c.gridy = 0;
            nameField = new JTextField();
            nameField.setColumns(10);
            widthField = new JFormattedTextField(intFormat);
            widthField.setColumns(5);
            widthField.setValue(50);
            heightField = new JFormattedTextField(intFormat);
            heightField.setColumns(5);
            heightField.setValue(50);
            center.add(nameField, c);
            c.gridy = GridBagConstraints.RELATIVE;
            center.add(widthField, c);
            center.add(heightField, c);

            JButton createButton = new TEButton("Create");
            createButton.addActionListener(e -> {
                if (validateInput() ) {
                    mapScreen.newMap(name, width, height);
                    cardLayout.show(TileEditorPanel.this, "tileEditor");
                    mapScreen.requestFocus();
                } else {
                    System.err.println("Invalid input");
                }
            });
            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 2;
            c.insets = new Insets(10, 0, 0, 0);
            center.add(createButton, c);

            c.gridx = 0;
            c.gridy = 0;
            add(center, c);
        }

        private boolean validateInput() {
            boolean isValid = true;

            nameField.setBackground(Color.white);
            name = nameField.getText();
            if (name.isEmpty()) {
                nameField.setBackground(invalidFieldColor);
                isValid = false;
            }

            try {
                widthField.setBackground(Color.white);
                widthField.commitEdit();
                width = ((Number)widthField.getValue()).intValue();
            } catch (ParseException e) {
                widthField.setBackground(invalidFieldColor);
                widthField.setValue(widthField.getValue());
                isValid = false;
            }

            try {
                heightField.setBackground(Color.white);
                heightField.commitEdit();
                height = ((Number) heightField.getValue()).intValue();
            } catch (ParseException e) {
                heightField.setBackground(invalidFieldColor);
                heightField.setValue(heightField.getValue());
                isValid = false;
            }

            return isValid;
        }
    }
}

class TEButton extends JButton {
    TEButton(String text) {
        super(text);
        setFocusable(false);
    }
}
