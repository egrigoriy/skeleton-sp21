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
        switch (this) {
            case DOWN: {
                return UP;
            }
            case UP: {
                return DOWN;
            }
            case LEFT: {
                return RIGHT;
            }
            case RIGHT: {
                return LEFT;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
