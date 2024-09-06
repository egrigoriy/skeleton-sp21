package gitlet.commands;

import gitlet.Error;

public interface Command {
    Error execute();
}
