package gitlet.commands;

import gitlet.Repository;

public class CommitCommand implements Command {
    private final String message;
    public CommitCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        Repository.commit(message);
    }
}
