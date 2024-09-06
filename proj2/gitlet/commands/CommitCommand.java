package gitlet.commands;

import gitlet.Repository;
import gitlet.Statuses;

public class CommitCommand implements Command {
    private final String message;
    public CommitCommand(String message) {
        this.message = message;
    }

    @Override
    public Statuses execute() {
        return Repository.commit(message);
    }
}
