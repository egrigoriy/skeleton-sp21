package byow.Core.engine.input;

import byow.Core.Engine;
import byow.Core.engine.commands.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents input string parser containing the logic of the game
 */
public class InputStringParser {
    protected Engine engine;
    protected String seed = "";
    private final int STARTING = 0;
    private final int SEEDING = 1;
    private final int PLAYING = 2;
    private final int QUITING = 3;
    protected int state;

    public InputStringParser(Engine engine) {
        this.engine = engine;
    }

    /**
     * Returns a list of commands corresponding to a given input
     * @param input
     * @return list of commands
     */
    private List<Command> parse(char input) {
        List<Command> result = new ArrayList<>();
        input = Character.toUpperCase(input);
        switch (state) {
            case STARTING:
                result.addAll(handleStarting(input));
                break;
            case QUITING:
                result.addAll(handleQuit(input));
                break;
            case SEEDING:
                result.addAll(handleSeeding(input));
                break;
            case PLAYING:
                result.addAll(handlePlaying(input));
                break;
            default:

        }
        return result;
    }

    /**
     * Returns list of commands corresponding to a given input when state is STARTING
     * @param input
     * @return list of commands
     */
    private List<Command> handleStarting(char input) {
        List<Command> result = new ArrayList<>();
        switch (input) {
            case 'N':
                result.addAll(prepareForSeeding());
                state = SEEDING;
                break;
            case 'L':
                result.addAll(handleLoad());
                state = PLAYING;
                break;
            case ':':
                state = QUITING;
                break;
            default:
        }
        return result;
    }

    /**
     * A template method to be overridden by key input parser
     * @return list of commands
     */
    protected List<Command> prepareForSeeding() {
        return new ArrayList<>();
    }


    /**
     * Returns list of commands corresponding to a given input when state is SEEDING
     * @param input
     * @return list of commands
     */
    private List<Command> handleSeeding(char input) {
        List<Command> result = new ArrayList<>();
        if (Character.isDigit(input)) {
            seed += input;
            result.addAll(handleSeed());
            state = SEEDING;
        }
        if (isSeedEnd(input)) {
            result.addAll(handleNewGame());
            state = PLAYING;
        }
        return result;
    }

    /**
     * Returns list of commands corresponding to a given input when state is PLAYING
     * @param input
     * @return list of commands
     */
    private List<Command> handlePlaying(char input) {
        List<Command> result = new ArrayList<>();
        switch (input) {
            case ':':
                state = QUITING;
                break;
            case 'W':
                result.addAll(handleMoveUp(input));
                break;
            case 'A':
                result.addAll(handleMoveLeft(input));
                break;
            case 'D':
                result.addAll(handleMoveRight(input));
                break;
            case 'S':
                result.addAll(handleMoveDown(input));
                break;
            case 'F':
                result.addAll(handleToggleFocus());
                break;
            default:
        }
        return result;
    }


    /**
     * Execute parsing over given input char
     * @param input
     */
    public void execute(char input) {
        List<Command> commands = parse(input);
        for (Command command : commands) {
            command.execute();
        }
    }

    /**
     * Execute parsing over given input string
     * @param input
     */
    public void execute(String input) {
        for (char c : input.toCharArray()) {
            execute(c);
        }
    }

    /**
     * A template method to be overridden by key input parser
     * @return list of commands
     */
    protected List<Command> handleSeed() {
        return new ArrayList<>();
    }

    /**
     * Returns list with commands relative to a new game starting
     * @return
     */
    protected List<Command> handleNewGame() {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, "N" + seed + "S"));
        result.add(new NewWorldCommand(engine, Long.parseLong(seed)));
        return result;
    }

    /**
     * Returns list of commands when history must be loaded
     * @return list of commands
     */
    protected List<Command> handleLoad() {
        List<Command> result = new ArrayList<>();
        String loadedHistory = engine.readHistory();
        InputStringParser historyParser = new InputStringParser(engine);
        for (char c : loadedHistory.toCharArray()) {
            result.addAll(historyParser.parse(c));
        }
        return result;
    }

    /**
     * Returns the list of commands when the hero is moved to left
     * @param nextKey
     * @return list of commands
     */
    protected List<Command> handleMoveLeft(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveLeftCommand(engine));
        return result;
    }

    /**
     * Returns the list of commands when the hero is moved to right
     * @param nextKey
     * @return list of commands
     */
    protected List<Command> handleMoveRight(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveRightCommand(engine));
        return result;
    }

    /**
     * Returns the list of commands when the hero is moved to up
     * @param nextKey
     * @return list of commands
     */
    protected List<Command> handleMoveUp(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveUpCommand(engine));
        return result;
    }

    /**
     * Returns the list of commands when the hero is moved to down
     * @param nextKey
     * @return list of commands
     */
    protected List<Command> handleMoveDown(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveDownCommand(engine));
        return result;
    }

    /**
     * Returns list of commands when focus around the hero is toggled
     * @return list of commands
     */
    protected List<Command> handleToggleFocus() {
        List<Command> result = new ArrayList<>();
        result.add(new ToggleFocusCommand(engine));
        return result;
    }

    /**
     * Returns the list of commands in case of possible quit
     * @param input
     * @return
     */
    protected List<Command> handleQuit(char input) {
        List<Command> result = new ArrayList<>();
        if (isQuit(input)) {
            result.add(new SaveCommand(engine));
        }
        return result;
    }

    /**
     * Returns true if the given char is one recognized as end of seed, otherwise false
     * @param c
     * @return
     */
    protected boolean isSeedEnd(char c) {
        return Character.toUpperCase(c) == 'S';
    }

    /**
     * Returns true if the given char is one recognized as quit, otherwise false
     * @param c
     * @return
     */
    protected boolean isQuit(char c) {
        return Character.toUpperCase(c) == 'Q';
    }
}
