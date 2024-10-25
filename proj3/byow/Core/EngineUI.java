package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class EngineUI extends TERenderer {

    public EngineUI() {
        initialize(Engine.WIDTH, Engine.HEIGHT);
    }

    public void render(TETile[][] worldState) {
        renderFrame(worldState);
    }

    public void displayMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.YELLOW);
        setMenu();
        StdDraw.show();
    }

    public void setMenu() {
        int width = Engine.WIDTH;
        int height = Engine.HEIGHT;
        StdDraw.text(width / 2, height / 2, "(N)ew game");
        StdDraw.text(width / 2, height / 2 - 2, "(L)oad");
        StdDraw.text(width / 2, height / 2 - 4, "(Q)uit");

    }

    public void setMenuWithSeed() {
        setMenu();
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 2 - 6, "Enter seed and press (S):");
    }
    public void displayMenuWithSeed(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.YELLOW);
        setMenu();
        setMenuWithSeed();
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 2 - 8, s);
        StdDraw.show();
    }

    public void displayMenuWithSeed() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.YELLOW);
        setMenu();
        setMenuWithSeed();
        StdDraw.show();
    }
}
