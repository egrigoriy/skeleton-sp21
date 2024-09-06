package gitlet.commands;

import gitlet.Repository;
import gitlet.Statuses;

public class ResetCommand implements Command {
    private final String commitID;
    public ResetCommand(String commitID) {
        this.commitID = commitID;
    }

    @Override
    public Statuses execute() {
        return Repository.reset(commitID);
    }
}
