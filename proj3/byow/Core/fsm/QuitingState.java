package byow.Core.fsm;

import byow.Core.Engine;
import byow.Core.commands.Command;
import byow.Core.commands.SaveCommand;

import java.util.List;

public class QuitingState implements State {
    @Override
    public State handle(Engine engine, char input, List<Command> result) {
        if (isQuit(input)) {
            new SaveCommand(engine);
        }
        return new PlayingState();
    }
    private boolean isQuit(char c) {
        return c == 'q';
    }
}
