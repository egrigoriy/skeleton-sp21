package byow.Core.input;

import byow.Core.Engine;

public class AbstractInputParser {
    protected InputSource inputSource;
    protected Engine engine;


    public AbstractInputParser(InputSource source, Engine engine) {
        this.inputSource = source;
        this.engine = engine;
    }


    protected String getNextDigitOrSeedEnd() {
        String nextKey;
        do {
            nextKey = Character.toString(inputSource.getNextKey()).toLowerCase();
        }
        while (!(Character.isDigit(nextKey.charAt(0)) || isSeedEnd(nextKey)));
        return nextKey;
    }


    protected boolean isSeedEnd(String s) {
        return s.equals("s");
    }

    protected boolean isQuit(String s) {
        return s.equals("q");
    }

    protected String nextKeyLowerCase() {
        return Character.toString(inputSource.getNextKey()).toLowerCase();
    }
}
