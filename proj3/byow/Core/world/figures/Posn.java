package byow.Core.world.figures;

import byow.Core.world.DIRECTION;

import java.util.Objects;

/**
 * Represents a position in 2D plane
 */
public class Posn {
    private final int x;
    private final int y;
    public Posn(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate
     * @return x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a new position translated by given a and b away from this position
     * @param a
     * @param b
     * @return translated point
     */
    public Posn translate(int a, int b) {
        return new Posn(this.x + a, this.y + b);
    }

    /**
     * Returns the neighbor position in given direction
     * @param dir
     * @return neighbor position
     */
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

    /**
     * Returns the distance from this position to the given one
     * @param other
     * @return distance
     */
    public double distanceTo(Posn other) {
        return Math.sqrt(Math.pow(other.getX() - this.getX(), 2)
                + Math.pow(other.getY() - this.getY(), 2));
    }

    /**
     * Returns the string representation of this point
     * @return string representation
     */
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
