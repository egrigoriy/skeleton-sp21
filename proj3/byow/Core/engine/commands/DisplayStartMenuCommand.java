package byow.Core.engine.commands;

import byow.Core.Engine;

public class DisplayStartMenuCommand extends AbstractCommand {
    public DisplayStartMenuCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.displayStartMenu();
    }
}
