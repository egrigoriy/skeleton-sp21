package byow.Core.input;

import byow.Core.Engine;
import byow.Core.commands.Command;
import byow.Core.commands.QuitCommand;
import byow.Core.commands.SaveCommand;

import java.util.ArrayList;
import java.util.List;

public class BarKeyParser extends FooStringParser {
    public BarKeyParser(Engine engine) {
        super(engine);
    }

    @Override
    public List<Command> prepareForSeeding() {
        System.out.println("BAR");
        engine.displayMenuForSeed();
        List<Command> result = new ArrayList<>();
        return result;
    }

    protected List<Command> handleQuit(char input) {
        List<Command> result = super.handleQuit(input);
        result.add(new QuitCommand(engine));
        return result;
    }
}
