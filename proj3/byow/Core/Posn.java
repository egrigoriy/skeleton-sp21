package byow.Core;

import java.util.Objects;

public class Posn {
    private final int x;
    private final int y;
    public Posn(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Posn translate(int a, int b) {
        return new Posn(this.x + a, this.y + b);
    }

    public Posn getNeighbor(DIRECTION dir) {
        switch (dir) {
            case UP:
                return translate(0, 1);
            case DOWN:
                return translate(0, -1);
            case RIGHT:
                return translate(1, 0);
            case LEFT:
                return translate(-1, 0);
            default:
                return translate(0, 0);
        }
    }
    @Override
    public String toString() {
        return "<" + x + "," + y + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Posn posn = (Posn) o;
        return getX() == posn.getX() && getY() == posn.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
