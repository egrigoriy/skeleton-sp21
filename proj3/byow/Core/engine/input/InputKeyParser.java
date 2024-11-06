package byow.Core.engine.input;

import byow.Core.engine.Engine;
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
        result.add(new DisplayMenuForSeedCommand(engine));
        return result;
    }

    @Override
    protected List<Command> handleSeed() {
        List<Command> result = new ArrayList<>();
        result.add(new DisplayMenuForSeedCommand(engine, this.seed));
        return result;
    }

    @Override
    protected List<Command> handleQuit(char input) {
        List<Command> result = super.handleQuit(input);
        result.add(new QuitCommand(engine));
        return result;
    }


}
