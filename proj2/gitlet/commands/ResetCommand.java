package gitlet.commands;

import gitlet.Repository;
import gitlet.Error;

public class ResetCommand implements Command {
    private final String commitID;
    public ResetCommand(String commitID) {
        this.commitID = commitID;
    }

    @Override
    public Error execute() {
        return Repository.reset(commitID);
    }
}
