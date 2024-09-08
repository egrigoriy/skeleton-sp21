package gitlet.commands;

import gitlet.Repository;

public class AddCommand implements Command {
    private final String fileName;
    public AddCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        Repository.add(fileName);
    }
}
