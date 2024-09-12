package gitlet.commands;

import gitlet.Repository;

/**
 *  Command used for removing given branch from the repository
 *  @author Grigoriy Emiliyanov
 */
public class RemoveBranchCommand implements Command {
    private final String branchName;
    public RemoveBranchCommand(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public void execute() {
        Repository.removeBranch(branchName);
    }
}
