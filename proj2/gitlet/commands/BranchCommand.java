package gitlet.commands;

import gitlet.Repository;
import gitlet.Statuses;

public class BranchCommand implements Command {
    private final String branchName;
    public BranchCommand(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public Statuses execute() {
        return Repository.branch(branchName);
    }
}
