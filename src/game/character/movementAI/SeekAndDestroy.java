//package game.character.movementAI;
//
//import Utility.AStarPath;
//import Utility.DataStructures.Tree;
//import Utility.Vector2D;
//import game.character.Character;
//
//import java.awt.*;
//
//import static game.main.GamePanel.game;
//
//public class SeekAndDestroy implements MovementAI {
//    private final Character character;
//    private Character target;
//
//    private AStarPath<Vector2D> pathFinder;
//
//    public SeekAndDestroy(Character character) {
//        this.character = character;
//
//        pathFinder = new AStarPath<>(game.collisionChecker,
//                new Vector2D(character.getX(), character.getY()), new Vector2D(target.getX(), target.getY()),
//                (Tree<Vector2D>.Node> v1, Tree<Vector2D>.Node v2) -> Vector2D.ManhattanDistance(v1.data, v2.data));
//
//        pathFinder.findPath();
//    }
//
//    @Override
//    public void updateAI() {
//        if (target == null)
//            findTarget();
//
//        pathFinder.updateTarget(new Vector2D(target.getX(), target.getY()));
//        Vector2D nextPos = pathFinder.step();
//
//        character.setPosition(nextPos.x, nextPos.y);
//    }
//
//    /**
//     * determine the target to find, default implementation points to the player
//     */
//    protected void findTarget() {
//        target = game.player;
//    }
//}
