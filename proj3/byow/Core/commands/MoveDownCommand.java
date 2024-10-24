package byow.Core.commands;

import byow.Core.Engine;

public class MoveDownCommand extends AbstractCommand {
    public MoveDownCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.moveDown();
    }
}
