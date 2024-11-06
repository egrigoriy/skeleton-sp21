package byow.Core.input;

import byow.Core.Engine;
import byow.Core.commands.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInputParser {
    protected InputSource inputSource;
    protected Engine engine;
    protected final int STARTING = 0;
    protected final int SEEDING = 1;
    protected final int PLAYING = 3;
    protected int state;


    protected AbstractInputParser(InputSource source, Engine engine) {
        this.inputSource = source;
        this.engine = engine;
    }

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
                            result.addAll(prepareForSeeding());
                            state = SEEDING;
                            break;
                        case 'l':
                            result.addAll(handleLoad());
                            state = PLAYING;
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
                        result.addAll(handleNewGame(seed));
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
        }
        return result;
    }

    protected abstract List<Command> prepareForSeeding();


    protected abstract List<Command> handleNewGame(String seed);
    protected abstract List<Command> handleLoad();
    protected abstract List<Command> handleMoveLeft(char nextKey);
    protected abstract List<Command> handleMoveRight(char nextKey);
    protected abstract List<Command> handleMoveUp(char nextKey);
    protected abstract List<Command> handleMoveDown(char nextKey);
    protected abstract List<Command> handleToggleFocus();
    protected abstract List<Command> handleQuit();


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
