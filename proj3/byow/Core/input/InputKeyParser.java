package byow.Core.input;

import byow.Core.Engine;
import byow.Core.commands.Command;
import byow.Core.commands.NewWorldCommand;
import byow.Core.commands.UpdateHistoryCommand;

import java.util.ArrayList;
import java.util.List;

public class InputKeyParser extends AbstractInputParser {
    protected final int START_MENU = 0;
    protected final int SEED = 1;
    protected int state;
    public InputKeyParser(InputSource inputSource, Engine engine) {
        super(inputSource, engine);
        this.state = START_MENU;
    }

    @Override
    protected List<Command> handleNewGame() {
        List<Command> result = new ArrayList<>();
        switch (state) {
            case START_MENU:
                engine.displayStartMenu();

        }

        if (state == START_MENU) {
            engine.displayMenuForSeed();
        }
        String seed = handleSeedInput();
        System.out.println(seed);
        result.add(new UpdateHistoryCommand(engine, "n" + seed + "s"));
        result.add(new NewWorldCommand(engine, Long.parseLong(seed)));
        return result;
    }

    @Override
    protected List<Command> handleLoad() {
        return null;
    }

    @Override
    protected List<Command> handleMoveLeft(char nextKey) {
        return null;
    }

    @Override
    protected List<Command> handleMoveRight(char nextKey) {
        return null;
    }

    @Override
    protected List<Command> handleMoveUp(char nextKey) {
        return null;
    }

    @Override
    protected List<Command> handleMoveDown(char nextKey) {
        return null;
    }

    @Override
    protected List<Command> handleToggleFocus() {
        return null;
    }

    @Override
    protected List<Command> handleQuit() {
        return null;
    }

    public String parse1() {
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
            case 'f':
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
    private long handleSeedInput2() {
        String seed = "";
        while (true) {
            char nextKey = getNextDigitOrSeedEnd();
            if (isSeedEnd(nextKey)) {
                break;
            }
            seed += nextKey;
            engine.displayMenuForSeed(seed);
        }
        return Long.parseLong(seed);
    }
}
