package game.event;

import java.awt.*;
import java.util.List;

public abstract class Event {
    static List<Event> eventList;

    Rectangle eventRect = new Rectangle(23, 23, 2, 2);
    int x, y;

    boolean oneTime = false;

    Event(int x, int y) {
        eventList.add(this);
        this.x = x;
        this.y = y;
    }

    abstract public void action();
}
