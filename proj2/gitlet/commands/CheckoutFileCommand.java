package gitlet.commands;

import gitlet.Repository;

/**
 *  Command used for checking out a file from the repository
 *  @author Grigoriy Emiliyanov
 */
public class CheckoutFileCommand implements Command {
    private final String fileName;
    private final String commitId;
    public CheckoutFileCommand(String fileName) {
        this(fileName, null);
    }

    public CheckoutFileCommand(String fileName, String commitId) {
        this.fileName = fileName;
        this.commitId = commitId;
    }

    @Override
    public void execute() {
        if (commitId == null) {
            Repository.checkoutFileFromActiveCommit(fileName);
        } else {
            Repository.checkoutFileFromCommit(fileName, commitId);
        }
    }
}
