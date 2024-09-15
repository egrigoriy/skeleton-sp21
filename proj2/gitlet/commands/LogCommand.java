package gitlet.commands;

import gitlet.RepositoryFacade;

/**
 *  Command used for showing the history of active commit in the repository
 *  @author Grigoriy Emiliyanov
 */
public class LogCommand implements Command {
    @Override
    public void execute() {
        RepositoryFacade.log();
    }
}
