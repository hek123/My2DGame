package game.event;

import game.character.Direction;
import game.main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static game.main.GamePanel.game;

public class EventHandler {
    public EventHandler() {
        setEvents();
    }

    private void setEvents() {
        Event.eventList = new ArrayList<>();
        new DamagePit(35, 27);
    }

    public void checkEvent() {
        List<Integer> removed = new ArrayList<>();
        for (int i = 0; i < Event.eventList.size(); i++) {
            Event event = Event.eventList.get(i);
            if (hit(event, Direction.LEFT)) {
                event.action();
                if (event.oneTime) removed.add(i);
            }
        }
        for (int i : removed) {
            Event.eventList.remove(i);
        }
    }

    private boolean hit(Event event, Direction reqDirection) {
        Rectangle playerBox = game.player.getBBox();
        Rectangle eventBox = new Rectangle(event.eventRect);
        eventBox.x += event.x * GamePanel.tileSize;
        eventBox.y += event.y * GamePanel.tileSize;

        if (playerBox.intersects(eventBox)) {
            return game.player.getDirection() == reqDirection;
        }

        return false;
    }
}
