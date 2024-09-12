package gitlet.commands;

import gitlet.GitletException;
import gitlet.Repository;

/**
 *  Command used for initializing the repository
 *  @author Grigoriy Emiliyanov
 */
public class InitCommand implements Command {
    @Override
    public void execute() throws GitletException {
        Repository.init();
    }
}
