package byow.Core.engine.commands;

import byow.Core.Engine;

/**
 * Command for saving the game
 */
public class SaveCommand extends AbstractCommand {
    public SaveCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.save();
    }
}
