package byow.Core;

import byow.Core.commands.Command;
import byow.Core.input.InputParser;
import byow.Core.input.InputSource;
import byow.Core.input.KeyboardInputSource;
import byow.Core.input.StringInputDevice;
import byow.TileEngine.TETile;

import java.util.List;

public class Engine {
    private World world;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private EngineUI ui;

    private History history = new History();

    public String loadHistory() {
        return history.load();
    }

    public void updateHistory(String action) {
        history.update(action);
    }

    public void save() {
        history.save();
    }

    private void quit() {
        System.exit(0);
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputSource keyboardInputSource = new KeyboardInputSource();
        ui = new EngineUI();
        ui.displayMenu();
        while (keyboardInputSource.possibleNextInput()) {
            String validInput = getNextValidInput(keyboardInputSource);
            if (validInput != null) {
                TETile[][] worldState = interactWithInputString(validInput);
                ui.render(worldState);
            }
        }
    }

    private String getNextValidInput(InputSource inputSource) {
        String inputString = Character.toString(inputSource.getNextKey()).toLowerCase();
        switch (inputString) {
            case "n":
                ui.displayMenuForSeed();
                return handleSeedInput(inputSource);
            case "l":
                return loadHistory();
            case ":":
                handleQuitInput(inputSource);
                break;
            case "a":
            case "s":
            case "d":
            case "w":
                return inputString;
            default:
                return null;
        }
        return null;
    }

    private void handleQuitInput(InputSource inputSource) {
        if (inputSource.possibleNextInput()) {
            String nextInput = Character.toString(inputSource.getNextKey()).toLowerCase();
            if (nextInput.equals("q")) {
                save();
                quit();
            }
        }
    }

    private String handleSeedInput(InputSource inputSource) {
        String seed = "";
        boolean seedEnd = false;
        while (!seedEnd) {
            String nextKey;
            do {
                nextKey = Character.toString(inputSource.getNextKey()).toLowerCase();
            }
            while(!(Character.isDigit(nextKey.charAt(0)) || nextKey.equals("s")));
            seed += nextKey;
            ui.displayMenuForSeed(seed);
            seedEnd = nextKey.equals("s");
        }
        return "n" + seed + "s";
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
        InputSource inputSource = new StringInputDevice(input);
        InputParser parser = new InputParser(inputSource, this);
        List<Command> commands = parser.parse();
        for (Command command : commands) {
            command.execute();
        }
        TETile[][] finalWorldFrame = world.getState();
        return finalWorldFrame;
    }

    @Override
    public String toString() {
        return world.getState().toString();
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
}
