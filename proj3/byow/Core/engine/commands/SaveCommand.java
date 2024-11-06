package byow.Core.engine.commands;

import byow.Core.engine.Engine;

public class SaveCommand extends AbstractCommand {
    public SaveCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.save();
    }
}
