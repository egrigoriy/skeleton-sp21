package gitlet.commands;

import gitlet.Repository;
import gitlet.Statuses;

public class StatusCommand implements Command {
    @Override
    public Statuses execute() {
        return Repository.status();
    }
}
