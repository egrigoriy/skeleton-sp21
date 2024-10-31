package byow.Core;

import byow.Core.commands.Command;
import byow.Core.input.*;
import byow.TileEngine.TETile;

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
        ui = new EngineUI();
//        ui.displayStartMenu();
        InputSource keyboardInputSource = new KeyboardInputSource();
        InputKeyParser inputKeyParser = new InputKeyParser(keyboardInputSource, this);
        List<Command> commands = inputKeyParser.parse();
        for (Command command : commands) {
            command.execute();
        }
//        while (keyboardInputSource.possibleNextInput()) {
//
//            String validInput = inputKeyParser.parse();
//            if (validInput != null) {
//                TETile[][] worldState = interactWithInputString(validInput);
//                ui.render(worldState);
//            }
//        }
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
        InputSource inputSource = new StringInputDevice(input);
        InputStringParser parser = new InputStringParser(inputSource, this);
        List<Command> commands = parser.parse();
        for (Command command : commands) {
            command.execute();
        }
        TETile[][] finalWorldFrame = world.getState();
        return finalWorldFrame;
    }

    @Override
    public String toString() {
        return world.toString();
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
        world.moveUp();
    }

    public void moveLeft() {
        world.moveLeft();
    }

    public void moveRight() {
        world.moveRight();
    }

    public void moveDown() {
        world.moveDown();
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
