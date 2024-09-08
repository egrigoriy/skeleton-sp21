package gitlet.commands;

import gitlet.Repository;

public class StatusCommand implements Command {
    @Override
    public void execute() {
        Repository.status();
    }
}
