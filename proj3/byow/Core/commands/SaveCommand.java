package byow.Core.commands;

import byow.Core.Engine;

public class QuitCommand extends AbstractCommand {
    public QuitCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.quit();
    }
}
