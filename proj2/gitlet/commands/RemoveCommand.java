package gitlet.commands;

import gitlet.Repository;
import gitlet.Error;

public class RemoveCommand implements Command {
    private final String fileName;
    public RemoveCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Error execute() {
        return Repository.remove(fileName);
    }
}
