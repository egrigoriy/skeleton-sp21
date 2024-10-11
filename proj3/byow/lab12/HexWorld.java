package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    public static void main(String[] args) {
        // initialize TERenderer
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // build the world
        World world = new World(WIDTH, HEIGHT);
        world.addHexagon(3, new Posn(WIDTH/2, HEIGHT/2));

        // render the world
        ter.renderFrame(world.getContent());
    }
}
