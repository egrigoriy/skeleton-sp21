package gitlet.commands;

import gitlet.Statuses;

public class NoSuchCommand implements Command {
    @Override
    public Statuses execute() {
        return Statuses.ERR_NO_SUCH_COMMAND;
    }
}
