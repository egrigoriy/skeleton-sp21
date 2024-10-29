package byow.Core.input;

import byow.Core.Engine;
import byow.Core.commands.*;

import java.util.ArrayList;
import java.util.List;

public class InputStringParser extends AbstractInputParser {
    public InputStringParser(InputSource source, Engine engine) {
        super(source, engine);
    }

    public List<Command> parse() {
        List<Command> result = new ArrayList<>();
        while (inputSource.possibleNextInput()) {
            char nextKeyLowerCase = nextKeyLowerCase();
            switch (nextKeyLowerCase) {
                case 'n':
                    long seed = handleSeedString();
                    result.add(new UpdateHistoryCommand(engine, "n" + seed + "s"));
                    result.add(new NewWorldCommand(engine, seed));
                    result.addAll(parse());
                    break;
                case 'l':
                    String loadedHistory = engine.readHistory();
                    InputSource historySource = new StringInputDevice(loadedHistory);
                    InputStringParser historyParser = new InputStringParser(historySource, engine);
                    result.addAll(historyParser.parse());
                    break;
                case 'w':
                    result.add(new UpdateHistoryCommand(engine, Character.toString(nextKeyLowerCase)));
                    result.add(new MoveUpCommand(engine));
                    result.addAll(parse());
                    break;
                case 'a':
                    result.add(new UpdateHistoryCommand(engine, Character.toString(nextKeyLowerCase)));
                    result.add(new MoveLeftCommand(engine));
                    result.addAll(parse());
                    break;
                case 'd':
                    result.add(new UpdateHistoryCommand(engine, Character.toString(nextKeyLowerCase)));
                    result.add(new MoveRightCommand(engine));
                    result.addAll(parse());
                    break;
                case 's':
                    result.add(new UpdateHistoryCommand(engine, Character.toString(nextKeyLowerCase)));
                    result.add(new MoveDownCommand(engine));
                    result.addAll(parse());
                    break;
                case ':':
                    result.addAll(handleQuitString());
                    break;
                case 'f':
                    result.add(new ToggleFocusCommand(engine));
                default:
//                    throw new IllegalArgumentException("Input string is invalid");
            }
        }
        return result;
    }

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



