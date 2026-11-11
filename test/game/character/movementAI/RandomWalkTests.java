package game.character.movementAI;

import org.junit.Test;
import game.character.movementAI.RandomWalk;
import game.character.monster.GreenSlime;

import java.awt.*;

import static org.junit.Assert.assertEquals;


public class RandomWalkTests {
    @Test
    public void updateAI() {
        var character = new GreenSlime(0, 0, new Rectangle(10, 10));
        var rw = new RandomWalk(character, .5, false);

        assertEquals();
    }
}
