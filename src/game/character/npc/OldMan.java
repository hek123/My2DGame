package game.character.npc;

import Utility.ImageAnchor;
import game.character.movementAI.RandomWalk;
import game.player.Player;
import game.visual.Entity;

import java.awt.*;

import static game.main.GamePanel.*;

public class OldMan extends NPC {
    private static final ImageAnchor[][] spriteImages = loadSpriteImages(imageFolder, "oldman", 2);

    private final String[] dialogues = new String[10];
    private int dialogueIdx = 0;

    public OldMan(int x, int y, Rectangle territory) {
        super(new Rectangle(6, 8, 36, 39));
//        animation = new MovingSprite(spriteImages, .3, 2);
        super.spriteImages = spriteImages;
        nbSprites = 2;
        setSpriteUpdatePeriod(.3);

        setDialogue();
        speed = .5;

        setPosition(x, y);
        game.entityManager.addMovingEntityToMap(this);

        movementAI = new RandomWalk(this, 2., true, territory);
    }
    public OldMan(int x, int y) {
        this(x, y, null);
    }
    public OldMan() {
        this(0, 0, null);
        game.entityManager.removeMovingEntityFromMap(this);
    }

    @Override
    public void entityInteraction(Entity entity) {}

    public void setDialogue() {
        dialogues[0] = "Hello, lad.";
        dialogues[1] = "So you've come to this island to\nfind the treasure?";
    }

    @Override
    public void speak(Player player) {
        super.speak(player);

        dialogueIdx %= 2;
        game.ui.dialogue(dialogues[dialogueIdx], this.getBBox(), player.getDirection());
        dialogueIdx++;
    }
}
