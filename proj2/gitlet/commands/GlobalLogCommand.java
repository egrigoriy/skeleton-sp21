package gitlet.commands;

import gitlet.Repository;
import gitlet.Statuses;

public class GlobalLogCommand implements Command {
    @Override
    public Statuses execute() {
        return Repository.globalLog();
    }
}
