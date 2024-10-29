package byow.Core.input;

import byow.Core.Engine;

public class AbstractInputParser {
    protected InputSource inputSource;
    protected Engine engine;


    public AbstractInputParser(InputSource source, Engine engine) {
        this.inputSource = source;
        this.engine = engine;
    }



    protected char getNextDigitOrSeedEnd() {
        char nextKey;
        do {
            nextKey = nextKeyLowerCase();
        }
        while (!(Character.isDigit(nextKey) || isSeedEnd(nextKey)));
        return nextKey;
    }


    protected boolean isSeedEnd(char c) {
        return c == 's';
    }

    protected boolean isQuit(char c) {
        return c == 'q';
    }

    protected char nextKeyLowerCase() {
        return Character.toLowerCase(inputSource.getNextKey());
    }
}
