package byow.Core.input;

import byow.Core.Engine;
import byow.Core.commands.*;

import java.util.ArrayList;
import java.util.List;

public class InputParser {
    private InputSource inputSource;
    private Engine engine;

    public InputParser(InputSource source, Engine engine) {
        this.inputSource = source;
        this.engine = engine;
    }

    public List<Command> parse() {
        List<Command> result = new ArrayList<>();
        while (inputSource.possibleNextInput()) {
            String lowerCaseInput = nextKeyLowerCase();
            switch (lowerCaseInput) {
                case "n":
                    engine.updateHistory(lowerCaseInput);
                    long seed = handleSeed();
                    result.add(new NewWorldCommand(engine, seed));
                    result.addAll(parse());
                    break;
                case "l":
                    String loadedHistory = engine.readHistory();
                    InputSource historySource = new StringInputDevice(loadedHistory);
                    InputParser historyParser = new InputParser(historySource, engine);
                    result.addAll(historyParser.parse());
                    break;
                case "w":
                    engine.updateHistory(lowerCaseInput);
                    result.add(new MoveUpCommand(engine));
                    result.addAll(parse());
                    break;
                case "a":
                    engine.updateHistory(lowerCaseInput);
                    result.add(new MoveLeftCommand(engine));
                    result.addAll(parse());
                    break;
                case "d":
                    engine.updateHistory(lowerCaseInput);
                    result.add(new MoveRightCommand(engine));
                    result.addAll(parse());
                    break;
                case "s":
                    engine.updateHistory(lowerCaseInput);
                    result.add(new MoveDownCommand(engine));
                    result.addAll(parse());
                    break;
                case ":":
                    result.addAll(handleSave());
                    break;
                default:
//                    throw new IllegalArgumentException("Input string is invalid");
            }
        }
        return result;
    }

    private List<Command> handleSave() {
        List<Command> result = new ArrayList<>();
        if (nextKeyLowerCase().equals("q")) {
            result.add(new SaveCommand(engine));
        }
        return result;
    }

    private long handleSeed() {
        String result = "";
        String nextKey = nextKeyLowerCase();
        engine.updateHistory(nextKey);
        while (!nextKey.equals("s")) {
            result += nextKey;
            nextKey = nextKeyLowerCase();
            engine.updateHistory(nextKey);
        }
        return Long.parseLong(result);
    }

    private String nextKeyLowerCase() {
        return Character.toString(inputSource.getNextKey()).toLowerCase();
    }

}



