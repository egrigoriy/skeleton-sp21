package gitlet.commands;

import gitlet.Repository;
import gitlet.Statuses;

public class InitCommand implements Command {
    @Override
    public Statuses execute() {
        return Repository.init();
    }
}
