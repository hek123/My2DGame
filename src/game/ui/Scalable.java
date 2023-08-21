package game.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public interface Scalable {
    List<Scalable> scalableList = new ArrayList<>();

    static void scaleAllWindows(Dimension dimension) {
        System.out.println("rescale");
        for (Scalable s: scalableList) {
            s.set(dimension);
        }
    }

    void set(Dimension dimension);
}
