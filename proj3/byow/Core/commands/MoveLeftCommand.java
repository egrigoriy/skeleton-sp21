package byow.Core.commands;

import byow.Core.Engine;

public class MoveLeftCommand extends AbstractCommand {
    public MoveLeftCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
       engine.moveLeft();
    }
}
