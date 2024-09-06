package gitlet.commands;

import gitlet.Repository;
import gitlet.Error;

public class LogCommand implements Command {
    @Override
    public Error execute() {
        return Repository.log();
    }
}
