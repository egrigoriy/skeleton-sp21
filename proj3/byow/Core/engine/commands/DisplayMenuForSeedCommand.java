package byow.Core.engine.commands;

import byow.Core.Engine;

public class DisplayMenuForSeedCommand extends AbstractCommand {
    private String seed = null;
    public DisplayMenuForSeedCommand(Engine engine) {
        super(engine);
    }

    public DisplayMenuForSeedCommand(Engine engine, String seed) {
        super(engine);
        this.seed = seed;
    }

    @Override
    public void execute() {
        if (seed == null) {
            engine.displayMenuForSeed();
        } else {
            engine.displayMenuForSeed(seed);
        }
    }
}
