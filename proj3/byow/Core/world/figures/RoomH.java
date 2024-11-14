package byow.Core.world.figures;

/**
 * Represents a horizontal hallway
 */
public class RoomH extends Room {
    private static final int HEIGHT = 3;
    public RoomH(int width, Posn posn) {
        super(width, HEIGHT, posn);
    }
}
