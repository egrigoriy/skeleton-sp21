package gitlet.commands;

import gitlet.Repository;

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
