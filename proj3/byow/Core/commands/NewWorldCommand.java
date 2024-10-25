package byow.Core.commands;

import byow.Core.Engine;

public class NewWorldCommand extends AbstractCommand {
    private final long seed;

    public NewWorldCommand(Engine engine, long seed) {
        super(engine);
        this.seed = seed;
    }

    @Override
    public void execute() {
        engine.createNewWorld(seed);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + seed;
    }
}
