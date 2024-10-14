package byow.lab12;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

/**
 * World represents the world
 */

public class World {
    private final int width;
    private final int height;
    private final TETile[][] state;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.state = initialize(width, height, Tileset.NOTHING);
    }

    /**
     * Returns a 2D array with given width and height filled with given tile.
     *
     * @param width
     * @param height
     * @param background
     * @return 2D array filled with given tile
     */
    private TETile[][] initialize(int width, int height, TETile background) {
        TETile[][] state = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                state[x][y] = background;
            }
        }
        return state;
    }

    /**
     * Returns the current state of this world
     *
     * @return current state of this world
     */
    public TETile[][] getState() {
        return state;
    }

    /**
     * Adds given figures to this world
     *
     * @param figures
     */
    public void addFigures(Set<Figure> figures) {
        for (Figure figure : figures) {
            addFigure(figure);
        }
    }

    /**
     * Adds given figure to this world.
     * Non-null figure tiles are copied to this world if inside it.
     *
     * @param figure
     */
    public void addFigure(Figure figure) {
        int figX = figure.getPosn().getX();
        int figY = figure.getPosn().getY();
        TETile[][] figureTiles = figure.getTiles();
        for (int x = 0; x < figure.getWidth(); x++) {
            for (int y = 0; y < figure.getHeight(); y++) {
                int tileX = figX + x;
                int tileY = figY + y;
                if (isInside(tileX, tileY) && figureTiles[x][y] != null) {
                    state[tileX][tileY] = figureTiles[x][y];
                }
            }
        }
    }


    /**
     * Returns true if given x and y are both inside this world, otherwise false.
     * @param x
     * @param y
     * @return boolean
     */
    private boolean isInside(int x, int y) {
        return (0 < x && x < width) && (0 < y && y < height);
    }

    /**
     * Adds mosaic of hexagons with given size at given position and given number of rings around.
     *
     * @param size
     * @param centralPosn
     * @param radius
     */
    public void addHexMosaic(int size, Posn centralPosn, int radius) {
        Hexagon centralHexagon = new Hexagon(size, centralPosn);
        Set<Figure> hexagons = centralHexagon.getHexMosaic(radius);
        addFigures(hexagons);
    }
}
