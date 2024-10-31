package byow.Core.input;

import byow.Core.Engine;
import byow.Core.commands.*;

import java.util.ArrayList;
import java.util.List;

public class InputStringParser extends AbstractInputParser {
    protected final int STARTING = 0;
    protected final int SEEDING = 1;
    protected final int LOADING = 2;
    protected final int PLAYING = 3;
    protected int state;


    public InputStringParser(InputSource source, Engine engine) {
        super(source, engine);
    }


    @Override
    public List<Command> parse() {
        state = STARTING;
        List<Command> result = new ArrayList<>();
        String seed = "";
        while (inputSource.possibleNextInput()) {
            char nextKey = nextKeyLowerCase();
            switch (state) {
                case STARTING:
                    switch (nextKey) {
                        case 'n':
                            state = SEEDING;
                            break;
                        case 'l':
                            state = LOADING;
                            result.addAll(handleLoad());
                            break;
                        case ':':
                            result.addAll(handleQuit());
                            break;
                        default:

                    }
                    break;
                case SEEDING:
                    if (Character.isDigit(nextKey)) {
                        seed += nextKey;
                        state = SEEDING;
                    }
                    if (nextKey == 's') {
                        result.add(new NewWorldCommand(engine, Long.parseLong(seed)));
                        state = PLAYING;
                    }
                    break;
                case PLAYING:
                    switch (nextKey) {
                        case 'w':
                            result.addAll(handleMoveUp(nextKey));
                            break;
                        case 'a':
                            result.addAll(handleMoveLeft(nextKey));
                            break;
                        case 'd':
                            result.addAll(handleMoveRight(nextKey));
                            break;
                        case 's':
                            result.addAll(handleMoveDown(nextKey));
                            break;
                        case ':':
                            result.addAll(handleQuit());
                            break;
                        case 'f':
                            result.addAll(handleToggleFocus());
                            break;
                        default:
                    }
                    break;
                default:

            }
//            switch (nextKey) {
//                case 'n':
//                    result.addAll(handleNewGame());
//                    break;
//                case 'l':
//                    result.addAll(handleLoad());
//                    break;
//                case 'w':
//                    result.addAll(handleMoveUp(nextKey));
//                    break;
//                case 'a':
//                    result.addAll(handleMoveLeft(nextKey));
//                    break;
//                case 'd':
//                    result.addAll(handleMoveRight(nextKey));
//                    break;
//                case 's':
//                    result.addAll(handleMoveDown(nextKey));
//                    break;
//                case ':':
//                    result.addAll(handleQuit());
//                    break;
//                case 'f':
//                    result.addAll(handleToggleFocus());
//                    break;
//                default:
////                    throw new IllegalArgumentException("Input string is invalid");
//            }
        }
        return result;
    }

    @Override
    protected List<Command> handleNewGame() {
        List<Command> result = new ArrayList<>();
        long seed = handleSeedString();
        result.add(new UpdateHistoryCommand(engine, "n" + seed + "s"));
        result.add(new NewWorldCommand(engine, seed));
        result.addAll(parse());
        return result;
    }

    @Override
    protected List<Command> handleLoad() {
        List<Command> result = new ArrayList<>();
        String loadedHistory = engine.readHistory();
        InputSource historySource = new StringInputDevice(loadedHistory);
        InputStringParser historyParser = new InputStringParser(historySource, engine);
        result.addAll(historyParser.parse());
        return result;
    }

    @Override
    protected List<Command> handleMoveLeft(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveLeftCommand(engine));
        result.addAll(parse());
        return result;
    }

    @Override
    protected List<Command> handleMoveRight(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveRightCommand(engine));
        result.addAll(parse());
        return result;
    }

    @Override
    protected List<Command> handleMoveUp(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveUpCommand(engine));
        result.addAll(parse());
        return result;
    }

    @Override
    protected List<Command> handleMoveDown(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveDownCommand(engine));
        result.addAll(parse());
        return result;
    }

    @Override
    protected List<Command> handleToggleFocus() {
        List<Command> result = new ArrayList<>();
        result.add(new ToggleFocusCommand(engine));
        return result;
    }

    @Override
    protected List<Command> handleQuit() {
        List<Command> result = new ArrayList<>();
        if (isQuit(nextKeyLowerCase())) {
            result.add(new SaveCommand(engine));
        }
        return result;
    }

//    public List<Command> parse() {
//        List<Command> result = new ArrayList<>();
//        while (inputSource.possibleNextInput()) {
//            char nextKey = nextKeyLowerCase();
//            switch (nextKey) {
//                case 'n':
//                    long seed = handleSeedString();
//                    result.add(new UpdateHistoryCommand(engine, "n" + seed + "s"));
//                    result.add(new NewWorldCommand(engine, seed));
//                    result.addAll(parse());
//                    break;
//                case 'l':
//                    String loadedHistory = engine.readHistory();
//                    InputSource historySource = new StringInputDevice(loadedHistory);
//                    InputStringParser historyParser = new InputStringParser(historySource, engine);
//                    result.addAll(historyParser.parse());
//                    break;
//                case 'w':
//                    result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
//                    result.add(new MoveUpCommand(engine));
//                    result.addAll(parse());
//                    break;
//                case 'a':
//                    result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
//                    result.add(new MoveLeftCommand(engine));
//                    result.addAll(parse());
//                    break;
//                case 'd':
//                    result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
//                    result.add(new MoveRightCommand(engine));
//                    result.addAll(parse());
//                    break;
//                case 's':
//                    result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
//                    result.add(new MoveDownCommand(engine));
//                    result.addAll(parse());
//                    break;
//                case ':':
//                    result.addAll(handleQuitString());
//                    break;
//                case 'f':
//                    result.add(new ToggleFocusCommand(engine));
//                    break;
//                default:
////                    throw new IllegalArgumentException("Input string is invalid");
//            }
//        }
//        return result;
//    }

    private List<Command> handleQuitString() {
        List<Command> result = new ArrayList<>();
        if (isQuit(nextKeyLowerCase())) {
            result.add(new SaveCommand(engine));
        }
        return result;
    }

    private long handleSeedString() {
        String seed = "";
        while (true) {
            char nextKey = getNextDigitOrSeedEnd();
            if (isSeedEnd(nextKey)) {
                break;
            }
            seed += nextKey;
        }
        return Long.parseLong(seed);
    }
}



