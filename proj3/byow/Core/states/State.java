package byow.Core.states;

import byow.Core.Engine;
import byow.Core.commands.Command;

import java.util.List;

public interface State {
    List<Command> handle(Engine engine, char input);
}
