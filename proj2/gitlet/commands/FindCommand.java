package gitlet.commands;

import gitlet.Repository;

public class FindCommand implements Command {
    private final String message;
    public FindCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        Repository.find(message);
    }
}
