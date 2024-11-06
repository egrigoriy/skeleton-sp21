package byow.Core;

import byow.Core.engine.EngineUI;
import byow.Core.engine.History;
import byow.Core.world.World;
import byow.Core.engine.commands.Command;
import byow.Core.engine.input.*;
import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;

public class Engine {
    private World world;
    /* Feel free to change the width and height. */
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
        InputSource keyboardInputSource = new KeyboardInputSource();
        ui = new EngineUI();
        ui.displayStartMenu();
        while (true) {
            if (keyboardInputSource.possibleNextInput()) {
                char keyInput = Character.toLowerCase(keyboardInputSource.getNextKey());
                List<Command> commands = new ArrayList<>();
                commands.addAll(inputKeyParser.parse(keyInput));
                for (Command command : commands) {
                    command.execute();
                }
                if (world != null) {
                    ui.render(world.getState());
                }
            }
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
        List<Command> commands = new ArrayList<>();
        for (char c : input.toCharArray()) {
            commands.addAll(inputStringParser.parse(c));
        }
        for (Command command : commands) {
            command.execute();
        }
        TETile[][] finalWorldFrame = world.getState();
        return finalWorldFrame;
    }

    @Override
    public String toString() {
        if (world != null) {
            return world.toString();
        }
        return "empty world";
    }

    public String readHistory() {
        return history.read();
    }

    public void updateHistory(String action) {
        history.update(action);
    }

    public void save() {
        history.save();
    }

    public void quit() {
        System.exit(0);
    }

    public void createNewWorld(long seed) {
        world = new World(WIDTH, HEIGHT, seed);
    }

    public void moveUp() {
        if (world != null) {
            world.moveUp();
        }
    }

    public void moveLeft() {
        if (world != null) {
            world.moveLeft();
        }
    }

    public void moveRight() {
        if (world != null) {
            world.moveRight();
        }
    }

    public void moveDown() {
        if (world != null) {
            world.moveDown();
        }
    }

    public void displayMenuForSeed(String seed) {
        ui.displayMenuForSeed(seed);
    }

    public void displayMenuForSeed() {
        ui.displayMenuForSeed();
    }

    public void toggleFocus() {
        if (world != null) {
            world.toggleFocus();
        }
    }

    public void displayStartMenu() {
        ui.displayStartMenu();
    }
}
