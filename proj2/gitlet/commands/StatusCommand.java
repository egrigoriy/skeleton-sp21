package gitlet.commands;

import gitlet.Repository;

/**
 *  Command used for showing the status of the working directory
 *  @author Grigoriy Emiliyanov
 */
public class StatusCommand implements Command {
    @Override
    public void execute() {
        Repository.status();
    }
}
