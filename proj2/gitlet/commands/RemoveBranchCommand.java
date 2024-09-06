package gitlet.commands;

import gitlet.Repository;
import gitlet.Statuses;

public class RemoveBranchCommand implements Command {
    private final String branchName;
    public RemoveBranchCommand(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public Statuses execute() {
        return Repository.removeBranch(branchName);
    }
}
