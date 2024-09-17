package gitlet;

public class Remote {
    private final String remoteName;

    public Remote(String name) {
       this.remoteName = name;
    }
    public void add(String remoteDirName) {
        Store.addRemote(remoteName, remoteDirName);
    }

    public boolean exists() {
        return Store.remoteExists(remoteName);
    }
    public void remove(String remoteName) {
        Store.removeRemote(remoteName);
    }
    public void fetch(String remoteBranchName) {
        Store.copyDistantObjectsToLocal(remoteName);
        Store.copyDistantBranchHeadToLocal(remoteName, remoteBranchName);
    }

    public void push(String remoteBranchName) {
        Store.copyLocalObjectsToDistant(remoteName);
        Store.copyLocalBranchHeadToDistant(remoteName, remoteBranchName);
    }

    public boolean branchExists(String remoteBranchName) {
        return Store.distantBranchExists(remoteName, remoteBranchName);
    }
    public boolean remoteUrlExists() {
        return Store.remoteUrlExists(remoteName);
    }

    public boolean isLocalBehindRemote(String remoteBranchName) {
        return Store.isLocalBehindRemote(remoteName, remoteBranchName);
    }
}
