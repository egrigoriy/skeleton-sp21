package gitlet.commands;

import gitlet.Repository;

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
