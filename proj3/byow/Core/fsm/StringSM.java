package byow.Core.fsm;

import byow.Core.Engine;
import byow.Core.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class StringSM {
    private State state;

    public void start(State startingState) {
        this.state = startingState;
    }

    public State getState() {
        return state;
    }
    public List<Command> step(Engine engine, char input) {
        List<Command> result = new ArrayList<>();
        state = state.handle(engine, input, result);
        return result;
    }

    public List<Command> transduce(Engine engine, char[] inputs) {
        List<Command> result = new ArrayList<>();
        for (char input : inputs) {
            result.addAll(step(engine, input));
        }
        return result;
    }
}
