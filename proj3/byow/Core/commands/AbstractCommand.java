package byow.Core.commands;

import byow.Core.Engine;

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
