package byow.lab12;

import byow.TileEngine.TERenderer;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    public static void main(String[] args) {
        // initialize TERenderer
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // build the world
        World world = new World(WIDTH, HEIGHT);
        int hexSize = 4;
        int nbrRings = 4;
        Posn centralPosn = new Posn(WIDTH/2 - hexSize, HEIGHT/2 - hexSize);
        world.addHexMosaic(hexSize, centralPosn, nbrRings);

        // render the world
        ter.renderFrame(world.getState());
    }
}
