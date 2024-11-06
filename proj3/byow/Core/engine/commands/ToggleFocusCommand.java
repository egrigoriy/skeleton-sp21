package byow.Core.engine.commands;

import byow.Core.engine.Engine;

public class ToggleFocusCommand extends AbstractCommand {
    public ToggleFocusCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.toggleFocus();
    }
}
