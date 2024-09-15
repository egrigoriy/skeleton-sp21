package gitlet.commands;

import gitlet.RepositoryFacade;

/**
 *  Command used for adding remote to the repository
 *  @author Grigoriy Emiliyanov
 */
public class AddRemoteCommad implements Command {
    private final String remoteName;
    private final String remoteUrl;

    public AddRemoteCommad(String remoteName, String remoteUrl) {
        this.remoteName = remoteName;
        this.remoteUrl = remoteUrl;
    }

    public void execute() {
        RepositoryFacade.addRemote(remoteName, remoteUrl);
    }
}
