package byow.Core.commands;

import byow.Core.Engine;

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
