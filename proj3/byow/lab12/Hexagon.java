package byow.lab12;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Arrays;

public class Hexagon {
    private final int size;
    private final int width;
    private final int height;


    public Hexagon(int size) {
        this.size = size;
        this.width = 3 * size -2;
        this.height = 2 * size;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isOccupied(int x, int y) {
        int minX;
        if (y < size) {
            minX = size - 1 - y;
        } else {
            minX = y - size;
        }
        int maxX = width - minX;
        return (x >= minX)  && (x < maxX);
    }
}
