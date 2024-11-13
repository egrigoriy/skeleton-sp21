package byow.Core.engine.commands;

import byow.Core.Engine;

/**
 * Command for quiting the game
 */
public class QuitCommand extends AbstractCommand {
    public QuitCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.quit();
    }
}
