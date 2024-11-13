package byow.Core.engine.commands;

import byow.Core.Engine;

/**
 * Command for moving the hero left
 */
public class MoveLeftCommand extends AbstractCommand {
    public MoveLeftCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.moveLeft();
    }
}
