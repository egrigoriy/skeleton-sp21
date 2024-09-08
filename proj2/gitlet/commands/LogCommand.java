package gitlet.commands;

import gitlet.Repository;

public class LogCommand implements Command {
    @Override
    public void execute() {
        Repository.log();
    }
}
