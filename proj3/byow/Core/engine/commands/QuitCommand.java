package byow.Core.engine.commands;

import byow.Core.engine.Engine;

public class QuitCommand extends AbstractCommand {
    public QuitCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.quit();
    }
}
