package byow.Core.fsm;

import byow.Core.Engine;
import byow.Core.commands.Command;

import java.util.List;

public class StartingState implements State {
    @Override
    public State handle(Engine engine, char input, List<Command> result) {
        switch (input) {
            case 'n':
                return new SeedingState();
            case 'l':
                return new LoadingState();
            case ':':
                return new QuitingState();
            default:

        }
        return null;
    }

}
