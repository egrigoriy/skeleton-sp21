package byow.Core.commands;

import byow.Core.Engine;

public class SaveCommand extends AbstractCommand {
    public SaveCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.save();
    }
}
