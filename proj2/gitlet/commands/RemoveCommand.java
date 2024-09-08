package gitlet.commands;

import gitlet.Repository;

public class RemoveCommand implements Command {
    private final String fileName;
    public RemoveCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        Repository.remove(fileName);
    }
}
