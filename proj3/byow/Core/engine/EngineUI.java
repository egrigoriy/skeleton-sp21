package byow.Core.engine;

import byow.Core.Engine;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class EngineUI extends TERenderer {
    private final int HUD_HEIGHT = 2;
    public EngineUI() {
        initialize(Engine.WIDTH, Engine.HEIGHT + HUD_HEIGHT, 0, 0);
    }

    public void drawWorld(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        Font font = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.YELLOW);
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x, y);
            }
        }
    }

    private void drawHUD(String info) {
        Font font = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        int width = Engine.WIDTH;
        int height = Engine.HEIGHT;
        double paddingTop = 1.0;
        double paddingSide = 1.0;
        StdDraw.textLeft(paddingSide, height + paddingTop, "Round: ");
        StdDraw.text(width / 2.0, height + paddingTop, "Playing");
        StdDraw.textRight(width - paddingSide, height + paddingTop, info);
        StdDraw.line(0, height, width, height);
    }

    public void render(TETile[][] world, String hoverText) {
        StdDraw.clear(Color.BLACK);
        drawWorld(world);
        drawHUD(hoverText);
        StdDraw.show();
    }

    public void render(TETile[][] world) {
        render(world, "");
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

    public boolean hasNextKeyTyped() {
        return StdDraw.hasNextKeyTyped();
    }

    public char nextKeyTyped() {
        return StdDraw.nextKeyTyped();
    }

    public boolean isMousePressed() {
        return StdDraw.isMousePressed();
    }

    public void pause(int i) {
        StdDraw.pause(i);
    }

    public double mouseX() {
        if (StdDraw.isMousePressed()) {
            StdDraw.pause(40);
            return StdDraw.mouseX();
        }
        return 0.0;
    }

    public double mouseY() {
        if (StdDraw.isMousePressed()) {
            StdDraw.pause(40);
            return StdDraw.mouseY();
        }
        return 0.0;
    }
}
