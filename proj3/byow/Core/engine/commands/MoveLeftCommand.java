package byow.Core.engine.commands;

import byow.Core.engine.Engine;

public class MoveLeftCommand extends AbstractCommand {
    public MoveLeftCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.moveLeft();
    }
}
