package byow.Core.engine.commands;

import byow.Core.Engine;

/**
 * Command for updating the history
 */
public class UpdateHistoryCommand extends AbstractCommand {
    private final String action;

    public UpdateHistoryCommand(Engine engine, String action) {
        super(engine);
        this.action = action;
    }

    @Override
    public void execute() {
        engine.updateHistory(action);
    }
}
