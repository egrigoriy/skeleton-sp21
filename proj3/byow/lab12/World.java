package byow.lab12;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class World {
    private final int width;
    private final int height;
    private final TETile[][] content;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.content = initialize(width, height, Tileset.FLOWER);
    }

    private TETile[][] initialize(int width, int height, TETile background) {
        TETile[][] content = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                content[x][y] = background;
            }
        }
        return content;
    }

    public TETile[][] getContent() {
        return content;
    }

    public void addHexagon(int size, Posn posn) {
        Hexagon hexagon = new Hexagon(size);
        for (int x = 0; x < hexagon.getWidth(); x++) {
            for (int y = 0; y < hexagon.getHeight(); y++) {
                if (hexagon.isOccupied(x, y)) {
                    content[posn.getX() + x][posn.getY() + y] = Tileset.AVATAR;
                }
            }
        }
    }
}
