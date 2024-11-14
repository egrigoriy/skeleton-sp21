package byow.Core.world.figures;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * Represents a hero with size 1x1 tiles
 */
public class Hero implements Figure {
    private final int width = 1;
    private final int height = 1;
    private Posn posn;
    private final TETile[][] tiles;

    public Hero(Posn posn) {
        this.posn = posn;
        this.tiles = fillTiles();
    }

    /**
     * Returns the initial tile representation
     * @return array of tiles
     */
    private TETile[][] fillTiles() {
        TETile[][] newTiles = new TETile[1][1];
        newTiles[0][0] = Tileset.AVATAR;
        return newTiles;
    }

    /**
     * Returns the width of the hero
     * @return width
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the hero
     * @return height
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Returns the position of this hero
     * @return position of this hero
     */
    @Override
    public Posn getPosn() {
        return posn;
    }

    /**
     * Sets the position for this hero
     * @param posn
     */
    @Override
    public void setPosn(Posn posn) {
        this.posn = posn;
    }

    /**
     * Returns the array of tiles corresponding to this hero
     * @return array of tiles
     */
    @Override
    public TETile[][] getTiles() {
        return tiles;
    }
}
