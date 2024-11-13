package byow.Core.engine.commands;

import byow.Core.Engine;

/**
 * Command for toggling focus
 */
public class ToggleFocusCommand extends AbstractCommand {
    public ToggleFocusCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
        engine.toggleFocus();
    }
}
