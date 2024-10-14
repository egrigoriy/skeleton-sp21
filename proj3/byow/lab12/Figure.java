package byow.lab12;

import byow.TileEngine.TETile;

public interface Figure {
    /** Returns the width of the figure **/
    int getWidth();

    /** Returns the height of the figure **/
    int getHeight();

    /** Returns the position of the figure **/
    Posn getPosn();

    /** Returns the tile representation of the figure **/
    TETile[][] getTiles();
}
