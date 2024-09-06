package gitlet.commands;

import gitlet.Statuses;
import gitlet.Repository;

public class AddCommand implements Command {
    private final String fileName;
    public AddCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Statuses execute() {
        return Repository.add(fileName);
    }
}
