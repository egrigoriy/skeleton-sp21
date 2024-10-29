package byow.Core.commands;

import byow.Core.Engine;

public class ToggleFocusCommand extends AbstractCommand {
    public ToggleFocusCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.toggleFocus();
    }
}
