package gitlet.commands;

import gitlet.Repository;
import gitlet.Error;

public class CommitCommand implements Command {
    private final String message;
    public CommitCommand(String message) {
        this.message = message;
    }

    @Override
    public Error execute() {
        return Repository.commit(message);
    }
}
