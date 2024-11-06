package byow.Core.engine.commands;

import byow.Core.engine.Engine;

public class MoveRightCommand extends AbstractCommand {
    public MoveRightCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.moveRight();
    }
}
