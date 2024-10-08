package gitlet.commands;

import gitlet.RepositoryFacade;

/**
 *  Command used for fetching from remote repository branch to the local repository
 *  @author Grigoriy Emiliyanov
 */
public class FetchCommand implements Command {
    private final String remoteName;
    private final String remoteBranchName;

    public FetchCommand(String remoteName, String remoteBranchName) {
        this.remoteName = remoteName;
        this.remoteBranchName = remoteBranchName;
    }

    public void execute() {
        RepositoryFacade.fetch(remoteName, remoteBranchName);
    }
}
