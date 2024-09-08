package gitlet.commands;

import gitlet.Repository;

public class AddRemoteCommad implements Command {
    private final String remoteName;
    private final String remoteDirName;

    public AddRemoteCommad(String remoteName, String remoteDirName) {
        this.remoteName = remoteName;
        this.remoteDirName = remoteDirName;
    }

    public void execute() {
        Repository.addRemote(remoteName, remoteDirName);
    }
}
