package byow.Core.engine.input;

import byow.Core.Engine;
import byow.Core.engine.commands.*;

import java.util.ArrayList;
import java.util.List;

public class InputKeyParser extends InputStringParser {
    public InputKeyParser(Engine engine) {
        super(engine);
    }

    @Override
    protected List<Command> prepareForSeeding() {
        List<Command> result = new ArrayList<>();
        result.add(new DisplayMenuForSeedCommand(this.engine));
        return result;
    }

    @Override
    protected List<Command> handleSeed() {
        List<Command> result = new ArrayList<>();
        result.add(new DisplayMenuForSeedCommand(this.engine, this.seed));
        return result;
    }

    @Override
    protected List<Command> handleQuit(char input) {
        List<Command> result = super.handleQuit(input);
        if (isQuit(input)) {
            result.add(new QuitCommand(this.engine));
        }
        return result;
    }
}
