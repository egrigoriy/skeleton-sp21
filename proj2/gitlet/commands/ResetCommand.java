package gitlet.commands;

import gitlet.Repository;

public class ResetCommand implements Command {
    private final String commitID;
    public ResetCommand(String commitID) {
        this.commitID = commitID;
    }

    @Override
    public void execute() {
        Repository.reset(commitID);
    }
}
