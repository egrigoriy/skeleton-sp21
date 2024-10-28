package byow.Core.input;

import byow.Core.Engine;

public class InputKeyParser extends AbstractInputParser {
    public InputKeyParser(InputSource inputSource, Engine engine) {
        super(inputSource, engine);
    }
    public String parse() {
        String inputString = nextKeyLowerCase();
        switch (inputString) {
            case "n":
                engine.displayMenuForSeed();
                return handleSeedInput();
            case "l":
                return engine.readHistory();
            case ":":
                handleQuitInput();
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

    private void handleQuitInput() {
        if (inputSource.possibleNextInput()) {
            String nextInput = nextKeyLowerCase();
            if (isQuit(nextInput)) {
                engine.save();
                engine.quit();
            }
        }
    }

    private String handleSeedInput() {
        String seed = "";
        boolean seedEnd = false;
        while (!seedEnd) {
            String nextKey = getNextDigitOrSeedEnd();
            seed += nextKey;
            engine.displayMenuForSeed(seed);
            seedEnd = isSeedEnd(nextKey);
        }
        return "n" + seed + "s";
    }
}
