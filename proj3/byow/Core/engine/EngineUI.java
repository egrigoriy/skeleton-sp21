package byow.Core.engine;

import byow.Core.Engine;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class EngineUI extends TERenderer {

    public EngineUI() {
        initialize(Engine.WIDTH, Engine.HEIGHT, 0, 0);
    }

    public void render(TETile[][] world) {
        renderFrame(world);
    }

    public void displayStartMenu() {
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

    private void drawHUD(String info) {
        int width = Engine.WIDTH;
        int height = Engine.HEIGHT;
        int paddingTop = 2;
        int paddingSide = 1;
        StdDraw.textLeft(paddingSide, height - paddingTop, "Round: ");
        StdDraw.text(width / 2.0, height - paddingTop, "Playing");
        StdDraw.textRight(width - paddingSide, height - paddingTop, info);
        int hudHeight = 3;
        StdDraw.line(0, height - hudHeight, width, height - hudHeight);
    }
}
