package gitlet.commands;

import gitlet.Repository;

public class RemoveRemoteCommand implements Command {
    private final String remoteName;

    public RemoveRemoteCommand(String remoteName) {
        this.remoteName = remoteName;
    }

    public void execute() {
        Repository.removeRemote(remoteName);
    }
}
