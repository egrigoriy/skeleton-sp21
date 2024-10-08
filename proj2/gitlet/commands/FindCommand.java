package gitlet.commands;

import gitlet.RepositoryFacade;

/**
 *  Command used for finding a commit with particular message the repository
 *  @author Grigoriy Emiliyanov
 */
public class FindCommand implements Command {
    private final String message;
    public FindCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        RepositoryFacade.find(message);
    }
}
