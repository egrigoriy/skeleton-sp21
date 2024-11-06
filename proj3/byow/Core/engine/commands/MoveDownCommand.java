package byow.Core.engine.commands;

import byow.Core.engine.Engine;

public class MoveDownCommand extends AbstractCommand {
    public MoveDownCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.moveDown();
    }
}
