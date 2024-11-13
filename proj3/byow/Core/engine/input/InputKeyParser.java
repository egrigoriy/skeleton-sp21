package byow.Core.engine.input;

import byow.Core.Engine;
import byow.Core.engine.commands.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an input key parser using the logic of InputStringParser
 */
public class InputKeyParser extends InputStringParser {
    public InputKeyParser(Engine engine) {
        super(engine);
    }

    /**
     * Returns the list of commands needed for preparing seed reading
     * @return list of commands
     */
    @Override
    protected List<Command> prepareForSeeding() {
        List<Command> result = new ArrayList<>();
        result.add(new DisplayMenuForSeedCommand(this.engine));
        return result;
    }

    /**
     * Returns the list of commands needed for showing the current seed input
     * @return list of commands
     */
    @Override
    protected List<Command> handleSeed() {
        List<Command> result = new ArrayList<>();
        result.add(new DisplayMenuForSeedCommand(this.engine, this.seed));
        return result;
    }

    /**
     * Returns list of commands needed for quitting
     * @param input
     * @return list of commands
     */
    @Override
    protected List<Command> handleQuit(char input) {
        List<Command> result = super.handleQuit(input);
        if (isQuit(input)) {
            result.add(new QuitCommand(this.engine));
        }
        return result;
    }
}
