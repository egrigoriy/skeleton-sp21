package gitlet.commands;

import gitlet.Repository;
import gitlet.Error;

public class GlobalLogCommand implements Command {
    @Override
    public Error execute() {
        return Repository.globalLog();
    }
}
