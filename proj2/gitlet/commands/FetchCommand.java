package gitlet.commands;

import gitlet.Repository;

public class FetchCommand implements Command {
    private final String remoteName;
    private final String remoteBranchName;

    public FetchCommand(String remoteName, String remoteBranchName) {
        this.remoteName = remoteName;
        this.remoteBranchName = remoteBranchName;
    }

    public void execute() {
        Repository.fetch(remoteName, remoteBranchName);
    }
}