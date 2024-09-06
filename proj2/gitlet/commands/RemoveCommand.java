package gitlet.commands;

import gitlet.Repository;
import gitlet.Statuses;

public class RemoveCommand implements Command {
    private final String fileName;
    public RemoveCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Statuses execute() {
        return Repository.remove(fileName);
    }
}
