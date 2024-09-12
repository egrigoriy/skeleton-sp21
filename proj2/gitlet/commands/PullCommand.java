package gitlet.commands;

import gitlet.Repository;

/**
 *  Command used for pulling branch from remote repository to the local repository
 *  @author Grigoriy Emiliyanov
 */
public class PullCommand implements Command {
    private final String remoteName;
    private final String remoteBranchName;

    public PullCommand(String remoteName, String remoteBranchName) {
        this.remoteName = remoteName;
        this.remoteBranchName = remoteBranchName;
    }

    public void execute() {
        Repository.pull(remoteName, remoteBranchName);
    }
}
