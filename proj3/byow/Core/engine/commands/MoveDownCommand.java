package byow.Core.engine.commands;

import byow.Core.Engine;

/**
 * Command for moving the hero down
 */
public class MoveDownCommand extends AbstractCommand {
    public MoveDownCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.moveDown();
    }
}
