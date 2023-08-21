package game.character.movementAI;

import Utility.UtilityTool;
import game.character.Character;
import game.character.Direction;

import java.awt.*;
import java.util.Random;

import static game.main.GamePanel.FPS;

public class RandomWalk implements MovementAI, TerritoryBound {
    private final Character character;

    int actionCounter = 0, actionUpdatePeriod = 0;

    final double lambda;
    private final Random randomDirection = new Random();

    private final int dirStates;

    private final Rectangle territory;

    public RandomWalk(Character character, double lambda, boolean still, Rectangle territory) {
        this.character = character;

        this.lambda = lambda * FPS;
        actionUpdatePeriod = 0;

        dirStates = still ? 5 : 4;

        this.territory = territory;
        if (territory != null) assert territory.contains(this.character.getBBox());
    }
    public RandomWalk(Character character, double lambda, boolean still) {
        this(character, lambda, still, null);
    }

    @Override
    public void updateAI() {
        if (character.moving) character.setSpeed(character.speed);
        if (actionCounter >= actionUpdatePeriod) {
            character.moving = true;
            actionCounter = 0;
            actionUpdatePeriod = UtilityTool.getRandomPoissonSample(lambda);
            int i = randomDirection.nextInt(dirStates);
            switch (i) {
                case 1 -> character.direction = Direction.UP;
                case 2 -> character.direction = Direction.DOWN;
                case 3 -> character.direction = Direction.LEFT;
                case 0 -> character.direction = Direction.RIGHT;
                case 4 -> {
                    System.out.println("stand still");
                    character.moving = false;
                    character.setSpeed(0);
                }
            }
        }
        actionCounter++;

        if (territory != null) {
            if (!territory.contains(character.getNextBBox())) {
                switch (character.direction) {
                    case DOWN -> character.direction = Direction.UP;
                    case UP -> character.direction = Direction.DOWN;
                    case RIGHT -> character.direction = Direction.LEFT;
                    case LEFT -> character.direction = Direction.RIGHT;
                }
                assert territory.contains(character.getNextBBox());
                actionCounter = 0;
            }
        }
    }

    @Override
    public Rectangle getTerritory() {
        return territory;
    }
}
