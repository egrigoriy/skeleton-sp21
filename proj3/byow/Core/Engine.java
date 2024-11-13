package byow.Core;

import byow.Core.engine.EngineUI;
import byow.Core.engine.History;
import byow.Core.world.World;
import byow.Core.engine.input.*;
import byow.TileEngine.TETile;

/**
 * Represents the Engine
 */
public class Engine {
    private World world;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private EngineUI ui;

    private final History history = new History();

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputKeyParser inputKeyParser = new InputKeyParser(this);
        ui = new EngineUI();
        ui.displayStartMenu();
        while (true) {
            if (ui.hasNextKeyTyped()) {
                char keyInput = Character.toUpperCase(ui.nextKeyTyped());
                inputKeyParser.execute(keyInput);
                if (world != null) {
                    ui.render(world.getState());
                }
            }
            if (ui.isMousePressed()) {
                double x = ui.mouseX();
                double y = ui.mouseY();
                String description = world.getTileDescription(x, y);
                ui.render(world.getState(), description);
            }
            ui.pause(40);
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        history.clear();
        InputStringParser inputStringParser = new InputStringParser(this);
        inputStringParser.execute(input);
        return world.getState();
    }

    @Override
    public String toString() {
        if (world != null) {
            return world.toString();
        }
        return "empty world";
    }

    /**
     * Reads the history as a string of entries
     * @return string
     */
    public String readHistory() {
        return history.read();
    }

    /**
     * Updates the history with given action
     * @param action
     */
    public void updateHistory(String action) {
        history.update(action);
    }

    /**
     * Saves the history
     */
    public void save() {
        history.save();
    }

    /**
     * Quits the program
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * Creates a new world from given seed
     * @param seed
     */
    public void createNewWorld(long seed) {
        world = new World(WIDTH, HEIGHT, seed);
    }

    /**
     * Moves the hero up
     */
    public void moveUp() {
        if (world != null) {
            world.moveUp();
        }
    }

    /**
     * Moves the hero left
     */
    public void moveLeft() {
        if (world != null) {
            world.moveLeft();
        }
    }

    /**
     * Moves the hero right
     */
    public void moveRight() {
        if (world != null) {
            world.moveRight();
        }
    }

    /**
     * Moves the hero down
     */
    public void moveDown() {
        if (world != null) {
            world.moveDown();
        }
    }

    /**
     * Displays menu for seed with given seed
     * @param seed
     */
    public void displayMenuForSeed(String seed) {
        ui.displayMenuForSeed(seed);
    }

    /**
     * Displays menu for seed
     */
    public void displayMenuForSeed() {
        ui.displayMenuForSeed();
    }

    /**
     * Toggles the focus around the hero
     */
    public void toggleFocus() {
        if (world != null) {
            world.toggleFocus();
        }
    }
}
