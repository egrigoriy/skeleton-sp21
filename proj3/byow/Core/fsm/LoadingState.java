package byow.Core.fsm;

import byow.Core.Engine;
import byow.Core.commands.Command;

import java.util.List;

public class LoadingState implements State {

    @Override
    public State handle(Engine engine, char input, List<Command> result) {
        String history = engine.readHistory();
        StringSM stringSM = new StringSM();
        stringSM.start(new StartingState());
        List<Command> commands = stringSM.transduce(engine, history.toCharArray());
        result.addAll(commands);
        return stringSM.getState();
    }
}
