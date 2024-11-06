package byow.Core.input;

import byow.Core.Engine;
import byow.Core.commands.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FooStringParser {
    protected Engine engine;
    private String seed = "";
    protected final int STARTING = 0;
    protected final int SEEDING = 1;
    protected final int PLAYING = 3;
    protected final int QUITING = 4;
    protected int state;

    public FooStringParser(Engine engine) {
        this.engine = engine;
    }

    public List<Command> parse(char input) {
        List<Command> result = new ArrayList<>();
        switch (state) {
            case STARTING:
                switch (input) {
                    case 'n':
                        result.addAll(prepareForSeeding());
                        state = SEEDING;
                        break;
                    case 'l':
                        result.addAll(handleLoad());
                        state = PLAYING;
                        break;
                    case ':':
                        state = QUITING;
                        break;
                    default:

                }
                break;
            case QUITING:
                result.addAll(handleQuit(input));
                break;
            case SEEDING:
                if (Character.isDigit(input)) {
                    seed += input;
                    state = SEEDING;
                }
                if (isSeedEnd(input)) {
                    result.addAll(handleNewGame());
                    state = PLAYING;
                }
                break;
            case PLAYING:
                switch (input) {
                    case 'w':
                        result.addAll(handleMoveUp(input));
                        break;
                    case 'a':
                        result.addAll(handleMoveLeft(input));
                        break;
                    case 'd':
                        result.addAll(handleMoveRight(input));
                        break;
                    case 's':
                        result.addAll(handleMoveDown(input));
                        break;
                    case ':':
                        state = QUITING;
                        break;
                    case 'f':
                        result.addAll(handleToggleFocus());
                        break;
                    default:
                }
                break;
            default:

        }
        return result;
    }

    protected Collection<Command> prepareForSeeding() {
        List<Command> result = new ArrayList<>();
        return result;
    }

    protected List<Command> handleNewGame() {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, "n" + seed + "s"));
        result.add(new NewWorldCommand(engine, Long.parseLong(seed)));
        return result;
    }

    protected List<Command> handleLoad() {
        List<Command> result = new ArrayList<>();
        String loadedHistory = engine.readHistory();
        FooStringParser foo = new FooStringParser(engine);
        for (char c : loadedHistory.toCharArray()) {
            result.addAll(foo.parse(c));
        }
        return result;
    }

    protected List<Command> handleMoveLeft(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveLeftCommand(engine));
        return result;
    }

    protected List<Command> handleMoveRight(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveRightCommand(engine));
        return result;
    }

    protected List<Command> handleMoveUp(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveUpCommand(engine));
        return result;
    }

    protected List<Command> handleMoveDown(char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveDownCommand(engine));
        return result;
    }

    protected List<Command> handleToggleFocus() {
        List<Command> result = new ArrayList<>();
        result.add(new ToggleFocusCommand(engine));
        return result;
    }

    protected List<Command> handleQuit(char input) {
        List<Command> result = new ArrayList<>();
        if (isQuit(input)) {
            result.add(new SaveCommand(engine));
        }
        return result;
    }


    protected boolean isSeedEnd(char c) {
        return c == 's';
    }

    protected boolean isQuit(char c) {
        return c == 'q';
    }
}
