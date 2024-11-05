package byow.Core.fsm;

import byow.Core.Engine;
import byow.Core.commands.Command;

import java.util.List;

public interface State {
    State handle(Engine engine, char input, List<Command> result);
}
