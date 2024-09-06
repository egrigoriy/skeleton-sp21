package gitlet.commands;

import gitlet.Repository;
import gitlet.Error;

public class FindCommand implements Command {
    private final String message;
    public FindCommand(String message) {
        this.message = message;
    }

    @Override
    public Error execute() {
        return Repository.find(message);
    }
}
