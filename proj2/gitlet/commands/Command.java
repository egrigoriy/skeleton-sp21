package gitlet.commands;

import gitlet.Statuses;

public interface Command {
    Statuses execute();
}
