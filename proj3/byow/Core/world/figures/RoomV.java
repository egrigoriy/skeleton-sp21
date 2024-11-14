package byow.Core.world.figures;

/**
 * Represents a vertical hallway
 */
public class RoomV extends Room {
    private static final int WIDTH = 3;
    public RoomV(int height, Posn posn) {
        super(WIDTH, height, posn);
    }
}
