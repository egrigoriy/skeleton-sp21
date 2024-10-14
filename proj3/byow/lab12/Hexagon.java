package byow.lab12;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Represents a hexagonal figure with given size at given position.
 * The shape for different sizes looks like:
 *
 *      s=2       s=3         s=4               s-5
 *
 *      aa        aaa         aaaa             aaaaa
 *     aaaa      aaaaa       aaaaaa           aaaaaaa
 *     aaaa     aaaaaaa     aaaaaaaa         aaaaaaaaa
 *      aa      aaaaaaa    aaaaaaaaaa       aaaaaaaaaaa
 *               aaaaa     aaaaaaaaaa      aaaaaaaaaaaaa
 *                aaa       aaaaaaaa       aaaaaaaaaaaaa
 *                           aaaaaa         aaaaaaaaaaa
 *                            aaaa           aaaaaaaaa
 *                                            aaaaaaa
 *                                             aaaaa
 *
 *  The hexagon is seen as a single rectangular figure having position Posn(x, y) at the bottom left corner:
 *        _______
 *       |  aaa  |
 *       | aaaaa |
 *       |aaaaaaa|
 *       |aaaaaaa|
 *       | aaaaa |
 *       |  aaa  |
 *  Posn(x,y)____
 */
public class Hexagon implements Figure {
    private static final Random RANDOM = new Random(123456);
    private final int size;
    private final int width;
    private final int height;

    private final Posn posn;

    public Hexagon(int size, Posn posn) {
        this.size = size;
        this.posn = posn;
        this.width = 3 * size -2;
        this.height = 2 * size;
    }

    /**
     * Returns the width of this hexagon
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of this hexagon
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the position of this hexagon
     * @return position
     */
    public Posn getPosn() {
        return posn;
    }

    @Override
    public TETile[][] getTiles() {
        TETile[][] tiles = new TETile[width][height];
        TETile tile = getRandomTile();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (isOccupied(x, y)) {
                    tiles[x][y] = tile;
                }
            }
        }
        return tiles;
    }

    /**
     * Returns true if the given (x, y) position inside the hexagon is occupied, otherwise false.
     * There is a relation between the row number and start position of symbols:
     * if y < size: startX = (size - 1) - y;
     * if y > size: startX = y - size;
     * End position is always:  (width - 1) - start
     *
     * @param x
     * @param y
     * @return boolean
     */
    private boolean isOccupied(int x, int y) {
        int startX;
        if (y < size) {
            startX = (size - 1) - y;
        } else {
            startX = y - size;
        }
        int endX = (width - 1) - startX;
        return (x >= startX)  && (x <= endX);
    }

    /**
     * Returns the hexagons that are near neighbors of this hexagons
     * @return a set of figures
     */
    public Set<Figure> getNeighbors() {
        int x = posn.getX();
        int y = posn.getY();
        Set<Figure> neighbors = new HashSet<>();
        // bottom
        neighbors.add(new Hexagon(size, new Posn(x, y - height)));
        // top
        neighbors.add(new Hexagon(size, new Posn(x, y + height)));
        // bottom left
        neighbors.add(new Hexagon(size, new Posn(x - (height - 1) , y - size)));
        // bottom right
        neighbors.add(new Hexagon(size, new Posn(x + (height - 1) , y - size)));
        // top left
        neighbors.add(new Hexagon(size, new Posn(x - (height - 1), y + size)));
        // top left
        neighbors.add(new Hexagon(size, new Posn(x + (height - 1), y + size)));
        return neighbors;
    }

    /**
     * Returns the hexagons that are on a given radius around this hexagon including.
     * If radius is 0, then only this hexagon is returned.
     *
     * @param radius
     * @return a set of hexagons
     */
    public Set<Figure> getHexMosaic(int radius) {
        Set<Figure> result = new HashSet<>();
        result.add(this);
        if (radius == 0) {
           return result;
        }
        for (Figure neighbor : getNeighbors()) {
            result.addAll(((Hexagon)neighbor).getHexMosaic(radius - 1));
        }
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hexagon hexagon = (Hexagon) o;
        return size == hexagon.size && Objects.equals(getPosn(), hexagon.getPosn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, getPosn());
    }


    /**
     * Returns a random Tile
     * @return a tile
     */
    private TETile getRandomTile() {
        int choice = RANDOM.nextInt(7);
        switch (choice) {
            case 0: return Tileset.FLOWER;
            case 1: return Tileset.FLOOR;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.WATER;
            case 5: return Tileset.SAND;
            case 6: return Tileset.WALL;
            default: return Tileset.TREE;
        }
    }
}
