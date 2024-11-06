package byow.Core.engine.commands;

import byow.Core.engine.Engine;

public class MoveUpCommand extends AbstractCommand {
    public MoveUpCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.moveUp();
    }
}
