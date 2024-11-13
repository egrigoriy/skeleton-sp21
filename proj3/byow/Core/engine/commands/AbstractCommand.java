package byow.Core.engine.commands;

import byow.Core.Engine;

/**
 * Represents a command to engine
 */
public abstract class AbstractCommand implements Command {
    protected Engine engine;

    public AbstractCommand(Engine engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
