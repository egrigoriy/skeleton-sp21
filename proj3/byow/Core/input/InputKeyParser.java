package byow.Core.input;

import byow.Core.Engine;

public class InputKeyParser extends AbstractInputParser {
    public InputKeyParser(InputSource inputSource, Engine engine) {
        super(inputSource, engine);
    }
    public String parse() {
        char nextKeyLowerCase = nextKeyLowerCase();
        switch (nextKeyLowerCase) {
            case 'n':
                engine.displayMenuForSeed();
                return handleSeedInput();
            case 'l':
                return engine.readHistory();
            case ':':
                handleQuitInput();
                break;
            case 'a':
            case 's':
            case 'd':
            case 'w':
                return Character.toString(nextKeyLowerCase);
            default:
                return null;
        }
        return null;
    }

    private void handleQuitInput() {
        if (inputSource.possibleNextInput()) {
            char nextInput = nextKeyLowerCase();
            if (isQuit(nextInput)) {
                engine.save();
                engine.quit();
            }
        }
    }

    private String handleSeedInput() {
        String seed = "";
        while (true) {
            char nextKey = getNextDigitOrSeedEnd();
            if (isSeedEnd(nextKey)) {
                break;
            }
            seed += nextKey;
            engine.displayMenuForSeed(seed);
        }
        return "n" + seed + "s";
    }


}
