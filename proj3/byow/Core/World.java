package byow.Core;

import byow.TileEngine.TETile;

public class World {
    private TETile[][] state;
    private final int width;
    private final int height;

    public World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.state = new MapGenerator(width, height).generate(seed).getContent();
    }


    public TETile[][] getState() {
        return state;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int y = height - 1; y <=0; y--) {
            for (int x = 0; x < width; x++) {
                result.append(state[x][y].character());
            }
            result.append("\n");
        }
        return result.toString();
    }
}
