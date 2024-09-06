package gitlet.commands;

import gitlet.Repository;
import gitlet.Statuses;

public class LogCommand implements Command {
    @Override
    public Statuses execute() {
        return Repository.log();
    }
}
