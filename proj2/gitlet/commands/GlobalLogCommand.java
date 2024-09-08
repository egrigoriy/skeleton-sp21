package gitlet.commands;

import gitlet.Repository;

public class GlobalLogCommand implements Command {
    @Override
    public void execute() {
        Repository.globalLog();
    }
}
