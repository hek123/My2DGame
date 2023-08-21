package TileEditor;

import main.BaseKeyAdapter;

import javax.swing.*;

public class TileEditorPanel extends JPanel {
    public TileEditorPanel() {
        super(true);

        addKeyListener(new BaseKeyAdapter());
    }
}
