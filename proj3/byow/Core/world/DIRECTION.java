package byow.Core.world;

/**
 * Represents directions
 */
public enum DIRECTION {
    UP(2),
    DOWN(-2),
    LEFT(-1),
    RIGHT(1);

    private final int num;

    DIRECTION(int num) {
        this.num = num;
    }
}
