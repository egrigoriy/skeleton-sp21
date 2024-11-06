package byow.Core.input;

import byow.Core.Engine;
import byow.Core.commands.*;

import java.util.ArrayList;
import java.util.List;

public class InputStringParser extends AbstractInputParser {

    public InputStringParser(InputSource source, Engine engine) {
        super(source, engine);
    }

    @Override
    protected List<Command> prepareForSeeding() {
        return null;
    }

//    @Override
//    public List<Command> parse() {
//        state = STARTING;
//        List<Command> result = new ArrayList<>();
//        String seed = "";
//        while (inputSource.possibleNextInput()) {
//            char nextKey = nextKeyLowerCase();
//            switch (state) {
//                case STARTING:
//                    switch (nextKey) {
//                        case 'n':
//                            state = SEEDING;
//                            break;
//                        case 'l':
//                            result.addAll(handleLoad());
//                            state = PLAYING;
//                            break;
//                        case ':':
//                            result.addAll(handleQuit());
//                            break;
//                        default:
//
//                    }
//                    break;
//                case SEEDING:
//                    if (Character.isDigit(nextKey)) {
//                        seed += nextKey;
//                        state = SEEDING;
//                    }
//                    if (nextKey == 's') {
//                        result.addAll(handleNewGame(seed));
//                        state = PLAYING;
//                    }
//                    break;
//                case PLAYING:
//                    switch (nextKey) {
//                        case 'w':
//                            result.addAll(handleMoveUp(nextKey));
//                            break;
//                        case 'a':
//                            result.addAll(handleMoveLeft(nextKey));
//                            break;
//                        case 'd':
//                            result.addAll(handleMoveRight(nextKey));
//                            break;
//                        case 's':
//                            result.addAll(handleMoveDown(nextKey));
//                            break;
//                        case ':':
//                            result.addAll(handleQuit());
//                            break;
//                        case 'f':
//                            result.addAll(handleToggleFocus());
//                            break;
//                        default:
//                    }
//                    break;
//                default:
//
//            }
//        }
//        return result;
//    }

    @Override
    protected List<Command> handleNewGame(String seed) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, "n" + seed + "s"));
        result.add(new NewWorldCommand(engine, Long.parseLong(seed)));
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
        return result;
    }

    @Override
    protected List<Command> handleMoveRight(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveRightCommand(engine));
        return result;
    }

    @Override
    protected List<Command> handleMoveUp(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveUpCommand(engine));
        return result;
    }

    @Override
    protected List<Command> handleMoveDown(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveDownCommand(engine));
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



