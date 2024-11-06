package byow.Core.engine.commands;

import byow.Core.Engine;

public class MoveRightCommand extends AbstractCommand {
    public MoveRightCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.moveRight();
    }
}
