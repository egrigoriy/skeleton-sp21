package gitlet.commands;

import gitlet.Repository;
import gitlet.Statuses;

public class FindCommand implements Command {
    private final String message;
    public FindCommand(String message) {
        this.message = message;
    }

    @Override
    public Statuses execute() {
        return Repository.find(message);
    }
}
