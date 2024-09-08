package gitlet.commands;

import gitlet.GitletException;
import gitlet.Repository;

public class InitCommand implements Command {
    @Override
    public void execute() throws GitletException {
        Repository.init();
    }
}
