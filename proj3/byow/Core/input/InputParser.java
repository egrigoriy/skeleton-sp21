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
                    long seed = handleSeed();
                    result.add(new NewWorldCommand(engine, seed));
                    result.addAll(parse());
                    break;
                case "l":
                    String loadedHistory = handleLoad();
                    InputParser historyParser = new InputParser(new StringInputDevice(loadedHistory), engine);
                    result.addAll(historyParser.parse());
                    break;
                case "w":
                    result.add(new MoveUpCommand(engine));
                    result.addAll(parse());
                    break;
                case "a":
                    result.add(new MoveLeftCommand(engine));
                    result.addAll(parse());
                    break;
                case "d":
                    result.add(new MoveRightCommand(engine));
                    result.addAll(parse());
                    break;
                case "s":
                    result.add(new MoveDownCommand(engine));
                    result.addAll(parse());
                    break;
                case ":":
                    result.addAll(handleQuit());
                default:
//                    throw new IllegalArgumentException("Input string is invalid");
            }
        }
        return result;
    }

    private List<Command> handleQuit() {
        List<Command> result = new ArrayList<>();
        if (nextKeyLowerCase().equals("q")) {
            result.add(new QuitCommand(engine));
        }
        return result;
    }

    private String handleLoad() {
        //return "LDDD:Q"
        return "LWWWDDD";
    }

    private long handleSeed() {
        String result = "";
        String nextKey = nextKeyLowerCase();
        while (!nextKey.equals("s")) {
            result += nextKey;
            nextKey = nextKeyLowerCase();
        }
        return Long.parseLong(result);
    }

    private String nextKeyLowerCase() {
        return Character.toString(inputSource.getNextKey()).toLowerCase();
    }

}



