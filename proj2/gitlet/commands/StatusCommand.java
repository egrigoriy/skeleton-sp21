package gitlet.commands;

import gitlet.RepositoryFacade;

/**
 *  Command used for showing the status of the working directory
 *  @author Grigoriy Emiliyanov
 */
public class StatusCommand implements Command {
    @Override
    public void execute() {
        RepositoryFacade.status();
    }
}
