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
        addMenu();
        StdDraw.show();
    }

    public void displayMenuForSeed() {
        StdDraw.clear(Color.BLACK);
        addMenuWithSeed();
        StdDraw.show();
    }

    public void displayMenuForSeed(String s) {
        StdDraw.clear(Color.BLACK);
        addMenuWithSeed();
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 2 - 8, s);
        StdDraw.show();
    }

    private void addMenu() {
        int width = Engine.WIDTH;
        int height = Engine.HEIGHT;
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(width / 2, height / 2 + 10, "CS61B: THE GAME");
        font = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(font);
        StdDraw.text(width / 2, height / 2, "(N)ew game");
        StdDraw.text(width / 2, height / 2 - 1, "(L)oad");
        StdDraw.text(width / 2, height / 2 - 2, "(Q)uit");
    }

    private void addMenuWithSeed() {
        addMenu();
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 2 - 6, "Enter seed and press (S):");
    }
}
