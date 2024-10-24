package byow.Core;

import byow.TileEngine.TETile;

public interface Figure {
    /** Returns the width of the figure **/
    int getWidth();

    /** Returns the height of the figure **/
    int getHeight();

    /** Returns the position of the figure **/
    Posn getPosn();

    /** Sets the position of the figure */
    public void setPosn(Posn posn);

    /** Returns the tile representation of the figure **/
    TETile[][] getTiles();

    void setTile(int x, int y);

    default void print() {
        System.out.println(getPosn());
        for (int x = getWidth() - 1; x >= 0; x--) {
            for (int y = getHeight() - 1; y >= 0; y--) {
                System.out.print(getTiles()[x][y].character());
            }
            System.out.println();
        }
    }
}
