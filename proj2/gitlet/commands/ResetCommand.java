package gitlet.commands;

import gitlet.Repository;

/**
 *  Command used for the head of active branch to given commit from the repository
 *  @author Grigoriy Emiliyanov
 */
public class ResetCommand implements Command {
    private final String commitId;
    public ResetCommand(String commitId) {
        this.commitId = commitId;
    }

    @Override
    public void execute() {
        Repository.reset(commitId);
    }
}
