package byow.Core.fsm;

import byow.Core.Engine;
import byow.Core.commands.Command;
import byow.Core.commands.NewWorldCommand;

import java.util.ArrayList;
import java.util.List;

public class SeedingState implements State {
    private String seed = "";
    @Override
    public State handle(Engine engine, char input, List<Command> result) {
        if (isSeedEnd(input)) {
            result.add(new NewWorldCommand(engine, Long.parseLong(seed)));
            return new PlayingState();
        }
        seed += input;
        return this;
    }

    protected boolean isSeedEnd(char c) {
        return c == 's';
    }
}
