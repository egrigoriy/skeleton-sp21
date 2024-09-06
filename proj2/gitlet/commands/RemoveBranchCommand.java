package gitlet.commands;

import gitlet.Repository;
import gitlet.Error;

public class RemoveBranchCommand implements Command {
    private final String branchName;
    public RemoveBranchCommand(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public Error execute() {
        return Repository.removeBranch(branchName);
    }
}
