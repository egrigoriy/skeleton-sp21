package byow.Core;

public enum DIRECTION {
    UP(2),
    DOWN(-2),
    LEFT(-1),
    RIGHT(1);

    private final int num;
    DIRECTION(int num) {
        this.num = num;
    }

    public boolean isOpposite(DIRECTION other) {
       return this.num + other.num == 0;
    }

    public DIRECTION getOpposite() {
        return switch (this) {
            case DOWN -> UP;
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
