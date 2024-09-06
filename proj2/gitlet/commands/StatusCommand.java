package gitlet.commands;

import gitlet.Repository;
import gitlet.Error;

public class StatusCommand implements Command {
    @Override
    public Error execute() {
        return Repository.status();
    }
}
