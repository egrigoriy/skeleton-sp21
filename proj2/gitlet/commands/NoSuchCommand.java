package gitlet.commands;

import gitlet.Error;

public class NoSuchCommand implements Command {
    @Override
    public Error execute() {
        return Error.ERR_NO_SUCH_COMMAND;
    }
}
