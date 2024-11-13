package byow.Core.engine.commands;

import byow.Core.Engine;

/**
 * Command for moving the hero up
 */
public class MoveUpCommand extends AbstractCommand {
    public MoveUpCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.moveUp();
    }
}
