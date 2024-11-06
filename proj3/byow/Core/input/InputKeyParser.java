package byow.Core.input;

import byow.Core.Engine;
import byow.Core.commands.Command;
import byow.Core.commands.QuitCommand;

import java.util.List;

public class InputKeyParser extends InputStringParser {
    public InputKeyParser(Engine engine) {
        super(engine);
    }

    @Override
    public void prepareForSeeding() {
        engine.displayMenuForSeed();
    }

    @Override
    protected List<Command> handleQuit(char input) {
        List<Command> result = super.handleQuit(input);
        result.add(new QuitCommand(engine));
        return result;
    }

    @Override
    protected void handleSeed(String seed) {
        engine.displayMenuForSeed(seed);
    }
}
