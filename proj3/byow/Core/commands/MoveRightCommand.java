package byow.Core.commands;

import byow.Core.Engine;

public class MoveRightCommand extends AbstractCommand {
    public MoveRightCommand(Engine engine) {
        super(engine);
    }

    @Override
    public void execute() {
       engine.moveRight();
    }
}
