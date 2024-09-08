package gitlet.commands;

import gitlet.Repository;

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
