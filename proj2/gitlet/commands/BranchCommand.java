package gitlet.commands;

import gitlet.Repository;

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
