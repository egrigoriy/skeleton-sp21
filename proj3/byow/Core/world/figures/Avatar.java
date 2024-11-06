package byow.Core.world.figures;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Avatar implements Figure {
    private final int width = 1;
    private final int height = 1;
    private Posn posn;
    private final TETile[][] tiles;

    public Avatar(Posn posn) {
        this.posn = posn;
        this.tiles = fillTiles();
    }

    private TETile[][] fillTiles() {
        TETile[][] newTiles = new TETile[1][1];
        newTiles[0][0] = Tileset.AVATAR;
        return newTiles;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Posn getPosn() {
        return posn;
    }

    @Override
    public void setPosn(Posn posn) {
        this.posn = posn;
    }

    @Override
    public TETile[][] getTiles() {
        return tiles;
    }
}
