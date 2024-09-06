package gitlet.commands;

import gitlet.Repository;
import gitlet.Error;

public class MergeCommand implements Command {
    private final String branchName;
    public MergeCommand(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public Error execute() {
        return Repository.merge(branchName);
    }
}
