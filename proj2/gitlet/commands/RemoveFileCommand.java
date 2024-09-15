package gitlet.commands;

import gitlet.RepositoryFacade;

/**
 *  Command used for removing a given file from tracking in the repository
 *  @author Grigoriy Emiliyanov
 */
public class RemoveFileCommand implements Command {
    private final String fileName;
    public RemoveFileCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        RepositoryFacade.removeFile(fileName);
    }
}
