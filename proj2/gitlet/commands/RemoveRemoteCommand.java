package gitlet.commands;

import gitlet.RepositoryFacade;

/**
 *  Command used for removing given remote from the repository
 *  @author Grigoriy Emiliyanov
 */
public class RemoveRemoteCommand implements Command {
    private final String remoteName;

    public RemoveRemoteCommand(String remoteName) {
        this.remoteName = remoteName;
    }

    public void execute() {
        RepositoryFacade.removeRemote(remoteName);
    }
}
