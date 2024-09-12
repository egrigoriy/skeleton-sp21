package gitlet.commands;

import gitlet.Repository;

/**
 *  Command used for creating a branch in the repository
 *  @author Grigoriy Emiliyanov
 */
public class BranchCommand implements Command {
    private final String branchName;
    public BranchCommand(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public void execute() {
        Repository.branch(branchName);
    }
}
