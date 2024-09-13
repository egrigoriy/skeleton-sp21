package gitlet.commands;

import gitlet.Repository;

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
        Repository.addFile(fileName);
    }
}
