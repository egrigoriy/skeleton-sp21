package byow.Core;

import byow.Core.commands.Command;
import byow.Core.commands.NewWorldCommand;
import byow.Core.input.InputParser;
import byow.Core.input.InputSource;
import byow.Core.input.KeyboardInputSource;
import byow.Core.input.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.List;

public class Engine {
    TERenderer ter = new TERenderer();
    private World world;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;


    public Engine() {
    }

    public void start(InputSource inputSource) {
        ter.initialize(WIDTH, HEIGHT);
        // display menu
        ter.renderFrame(interactWithInputString("N999SDDDDWWWSSSSDDD"));
        while (inputSource.possibleNextInput()) {
            String s = Character.toString(inputSource.getNextKey());
            ter.renderFrame(interactWithInputString(s));
        }
    }

    public void displayWorld() {
//        while (inputSource.possibleNextInput()) {
//            // List<EngineCommand> commands = parser.parse()
//            // for (Command command : commands) {
//            //          command.execute();
//            // }
////        ter.renderFrame(world.getState());
//        }

    }

    public void quit() {
        System.exit(0);
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputSource keyboardInputSource = new KeyboardInputSource();
        start(keyboardInputSource);
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
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

//        ter.initialize(WIDTH, HEIGHT);
        InputSource inputSource = new StringInputDevice(input);
        InputParser parser = new InputParser(inputSource, this);
        List<Command> commands = parser.parse();
        for (Command command : commands) {
            command.execute();
        }
        TETile[][] finalWorldFrame = world.getState();
//        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }


    public void createNewWorld(long seed) {
        world = new World(WIDTH, HEIGHT, seed);
    }


    public String toString() {
        return world.toString();
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
