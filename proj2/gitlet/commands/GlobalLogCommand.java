package gitlet.commands;

import gitlet.RepositoryFacade;

/**
 *  Command used for showing all commits the repository
 *  @author Grigoriy Emiliyanov
 */
public class GlobalLogCommand implements Command {
    @Override
    public void execute() {
        RepositoryFacade.globalLog();
    }
}
