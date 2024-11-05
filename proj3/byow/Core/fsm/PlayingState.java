package byow.Core.fsm;

import byow.Core.Engine;
import byow.Core.commands.*;

import java.nio.file.LinkPermission;
import java.util.ArrayList;
import java.util.List;

public class PlayingState implements State {
    @Override
    public State handle(Engine engine, char input, List<Command> result) {
        switch (input) {
            case 'w':
                result.addAll(handleMoveUp(engine, input));
                return this;
            case 'a':
                result.addAll(handleMoveLeft(engine, input));
                return this;
            case 'd':
                result.addAll(handleMoveRight(engine, input));
                return this;
            case 's':
                result.addAll(handleMoveDown(engine, input));
                return this;
            case ':':
                return new QuitingState();
            case 'f':
                result.addAll(handleToggleFocus(engine));
                return this;
            default:
        }
        return this;
    }


    protected List<Command> handleMoveLeft(Engine engine, char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveLeftCommand(engine));
        return result;
    }

    protected List<Command> handleMoveRight(Engine engine, char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveRightCommand(engine));
        return result;
    }

    protected List<Command> handleMoveUp(Engine engine, char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveUpCommand(engine));
        return result;
    }

    protected List<Command> handleMoveDown(Engine engine, char nextKey) {
        List<Command> result = new ArrayList<>();
        result.add(new UpdateHistoryCommand(engine, Character.toString(nextKey)));
        result.add(new MoveDownCommand(engine));
        return result;
    }

    protected List<Command> handleToggleFocus(Engine engine) {
        List<Command> result = new ArrayList<>();
        result.add(new ToggleFocusCommand(engine));
        return result;
    }

}
