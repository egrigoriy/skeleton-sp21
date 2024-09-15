package gitlet.commands;

import gitlet.RepositoryFacade;

/**
 *  Command used for committing to the repository
 *  @author Grigoriy Emiliyanov
 */
public class CommitCommand implements Command {
    private final String message;
    public CommitCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        RepositoryFacade.commit(message);
    }
}
