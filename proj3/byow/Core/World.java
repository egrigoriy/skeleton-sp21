package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class World {
    private final TETile[][] state;
    private final int width;
    private final int height;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.state = initialize(width, height);
    }

    private TETile[][] initialize(int width, int height) {
        TETile[][] state = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                state[x][y] = Tileset.AVATAR;
            }
        }
        return state;
    }

    public TETile[][] getState() {
        return state;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                result.append(state[x][y].character());
            }
            result.append("\n");
        }
        return result.toString();
    }
}
