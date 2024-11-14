package byow.Core.world;

import byow.Core.world.figures.Hero;
import byow.Core.world.figures.Figure;
import byow.Core.world.figures.Posn;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;

/**
 * Represents a map of tiles
 */
public class WorldMap {
    private final TETile[][] content;
    private final int width;
    private final int height;
    /* */
    private Hero focus = null;


    public WorldMap(int width, int height) {
        this.content = initialize(width, height);
        this.width = width;
        this.height = height;
    }

    /**
     * Returns an array of tiles with given width and height
     * @param w
     * @param h
     * @return array of tiles
     */
    private TETile[][] initialize(int w, int h) {
        TETile[][] state = new TETile[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                state[x][y] = Tileset.NOTHING;
            }
        }
        return state;
    }

    /**
     * Returns the content as array of tile of this map
     * @return array of tiles
     */
    public TETile[][] getContent() {
        if (focus != null) {
            return applyFocus();
        }
        return content;
    }

    /**
     * Returns a copy of this map with applied focus around the hero's position
     * @return array of tiles
     */
    private TETile[][] applyFocus() {
        TETile[][] newContent = new TETile[width][height];
        int focusRadius = 5;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (focus.getPosn().distanceTo(new Posn(x, y)) < focusRadius) {
                    newContent[x][y] = content[x][y];
                } else {
                    newContent[x][y] = Tileset.NOTHING;
                }
            }
        }
        return newContent;
    }

    /**
     * Adds all given figures to this map
     * @param figures
     */
    public void addFigures(List<Figure> figures) {
        for (Figure figure : figures) {
            addFigure(figure);
        }
    }

    /**
     * Adds given figure to this world.
     * Non-null figure tiles are copied to this world if inside it.
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

    /**
     * Places the given figure at random position
     * @param figure
     */
    public void placeAtRandomPosn(Figure figure) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                figure.setPosn(new Posn(x, y));
                if (canContain(figure) && content[x][y].equals(Tileset.FLOOR)) {
                    addFigure(figure);
                    return;
                }
            }
        }
    }

    /**
     * Moves given figure to given direction with 1 tile
     * @param figure
     * @param dir
     */
    public void moveFigure(Figure figure, DIRECTION dir) {
        Posn currentPosn = figure.getPosn();
        Posn neighbor = currentPosn.getNeighbor(dir);
        if (isFloorAt(neighbor)) {
            figure.setPosn(neighbor);
            addFigure(figure);
            makeFloor(currentPosn);
        }
    }

    /**
     * Returns true if given position is a floor tile
     * @param posn
     * @return boolean
     */
    private boolean isFloorAt(Posn posn) {
        int x = posn.getX();
        int y = posn.getY();
        return content[x][y].equals(Tileset.FLOOR);
    }

    /**
     * Sets a floor tile at given position
     * @param posn
     */
    private void makeFloor(Posn posn) {
        int x = posn.getX();
        int y = posn.getY();
        content[x][y] = Tileset.FLOOR;
    }

    /**
     * Toggles the focus around the given hero
     * @param hero
     */
    public void toggleFocus(Hero hero) {
        if (focus == null) {
            focus = hero;
        } else {
            focus = null;
        }
    }

    /**
     * Returns the description of the tile at given positions x and y.
     * @param x
     * @param y
     * @return tile description
     */
    public String getTileDescription(double x, double y) {
        int tileX = (int) x;
        int tileY = (int) y;
        if (x < width && y < height) {
            return content[tileX][tileY].description();
        }
        return "";
    }
}

