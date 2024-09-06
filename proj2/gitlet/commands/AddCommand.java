package gitlet.commands;

import gitlet.Error;
import gitlet.Repository;

public class AddCommand implements Command {
    private final String fileName;
    public AddCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Error execute() {
        return Repository.add(fileName);
    }
}
