package gitlet.commands;

import gitlet.Repository;

/**
 *  Command used for checking out a branch from the repository
 *  @author Grigoriy Emiliyanov
 */
public class CheckoutBranchCommand implements Command {
    private final String branchName;
    public CheckoutBranchCommand(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public void execute() {
        Repository.checkoutFilesFromBranchHead(branchName);
    }
}
