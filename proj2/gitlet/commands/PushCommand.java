package gitlet.commands;

import gitlet.Repository;

/**
 *  Command used for pushing branch to remote repository from the local repository
 *  @author Grigoriy Emiliyanov
 */
public class PushCommand implements Command {
    private final String remoteName;
    private final String remoteBranchName;

    public PushCommand(String remoteName, String remoteBranchName) {
        this.remoteName = remoteName;
        this.remoteBranchName = remoteBranchName;
    }

    public void execute() {
        Repository.push(remoteName, remoteBranchName);
    }
}
