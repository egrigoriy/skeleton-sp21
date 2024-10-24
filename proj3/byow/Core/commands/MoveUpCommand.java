package byow.Core.commands;

import byow.Core.Engine;

public class MoveUpCommand extends AbstractCommand {
    public MoveUpCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.moveUp();
    }
}
