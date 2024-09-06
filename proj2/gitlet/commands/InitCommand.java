package gitlet.commands;

import gitlet.Repository;
import gitlet.Error;

public class InitCommand implements Command {
    @Override
    public Error execute() {
        return Repository.init();
    }
}
