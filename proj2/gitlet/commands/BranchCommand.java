package gitlet.commands;

import gitlet.Repository;
import gitlet.Error;

public class BranchCommand implements Command {
    private final String branchName;
    public BranchCommand(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public Error execute() {
        return Repository.branch(branchName);
    }
}
