package gitlet.commands;

import gitlet.RepositoryFacade;

/**
 *  Command used for adding file to the repository
 *  @author Grigoriy Emiliyanov
 */
public class AddCommand implements Command {
    private final String fileName;
    public AddCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        RepositoryFacade.addFile(fileName);
    }
}
