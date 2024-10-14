package byow.lab12;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posn posn = (Posn) o;
        return getX() == posn.getX() && getY() == posn.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
