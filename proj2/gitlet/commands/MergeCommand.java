package gitlet.commands;

import gitlet.Repository;

/**
 *  Command used for merging current branch to given one in the repository
 *  @author Grigoriy Emiliyanov
 */
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
