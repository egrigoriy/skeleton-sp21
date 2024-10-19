package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;

public class Map {
    private final TETile[][] content;
    private final int width;
    private final int height;


    public Map(int width, int height) {
        this.content = initialize(width, height);
        this.width = width;
        this.height = height;
    }

    private TETile[][] initialize(int w, int h) {
        TETile[][] state = new TETile[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                state[x][y] = Tileset.NOTHING;
            }
        }
        return state;
    }

    public TETile[][] getContent() {
        return content;
    }

    public void addFigures(List<Figure> figures) {
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
        if (!canContain(figure)) {
            return;
        }
        int figX = figure.getPosn().getX();
        int figY = figure.getPosn().getY();
        TETile[][] figureTiles = figure.getTiles();
        for (int x = 0; x < figure.getWidth(); x++) {
            for (int y = 0; y < figure.getHeight(); y++) {
                int tileX = figX + x;
                int tileY = figY + y;
                if (figureTiles[x][y] != null) {
                    content[tileX][tileY] = figureTiles[x][y];
                }
            }
        }
    }


    /**
     * Returns true if given figure is inside this world, otherwise false.
     * @param figure
     * @return
     */
    public boolean canContain(Figure figure) {
        int figX = figure.getPosn().getX();
        int figY = figure.getPosn().getY();
        return canContain(figX, figY)
                && canContain(figX + figure.getWidth(), figY + figure.getHeight());
    }


    /**
     * Returns true if given x and y are both inside this world, otherwise false.
     * @param x
     * @param y
     * @return boolean
     */
    private boolean canContain(int x, int y) {
        return (0 <= x && x <= width - 1) && (0 <= y && y <= height - 1);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                result.append(content[x][y].character());
            }
            result.append("\n");
        }
        return result.toString();
    }
}

