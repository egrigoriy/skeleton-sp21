package gitlet.commands;

import gitlet.Repository;

public class MergeCommand implements Command {
    private final String branchName;
    public MergeCommand(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public void execute() {
        Repository.merge(branchName);
    }
}
