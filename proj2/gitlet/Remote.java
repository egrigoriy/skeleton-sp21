package gitlet;

/**
 * Represents a remote with given name
 *
 *  @author Grigoriy Emiliyanov
 */
public class Remote {
    private final String remoteName;

    public Remote(String name) {
        this.remoteName = name;
    }

    /**
     * Adds for tracking given remote directory under given remote name
     * @param remoteDirName
     */
    public void add(String remoteDirName) {
        Store.addRemote(remoteName, remoteDirName);
    }
    /**
     * Removes for tracking remote under given remote name
     */
    public void remove() {
        Store.removeRemote(remoteName);
    }

    /**
     * Returns true if this remote name is tracked by the repository, otherwise false
     * @return boolean
     */
    public boolean exists() {
        return Store.remoteExists(remoteName);
    }

    /**
     * Fetches the given branch name from this remote name
     * @param remoteBranchName
     */
    public void fetch(String remoteBranchName) {
        Store.copyDistantObjectsToLocal(remoteName);
        Store.copyDistantBranchHeadToLocal(remoteName, remoteBranchName);
    }

    /**
     * Pushes the given branch name to distant corresponding to this remote name
     * @param remoteBranchName
     */
    public void push(String remoteBranchName) {
        Store.copyLocalObjectsToDistant(remoteName);
        Store.copyLocalBranchHeadToDistant(remoteName, remoteBranchName);
    }

    /**
     * Returns true if given branch name of this remote exist on the corresponding distant side,
     * otherwise false.
     * @param remoteBranchName
     * @return
     */
    public boolean branchExists(String remoteBranchName) {
        return Store.distantBranchExists(remoteName, remoteBranchName);
    }

    /**
     * Returns true if directory corresponding to this remote exists, otherwise false
     * @return boolean
     */
    public boolean remoteUrlExists() {
        return Store.remoteUrlExists(remoteName);
    }

    /**
     * Returns true if given branch name of tracked remote is behind the remote one,
     * otherwise false
     * @param remoteBranchName
     * @return
     */
    public boolean isLocalBehindRemote(String remoteBranchName) {
        return Store.isLocalBehindRemote(remoteName, remoteBranchName);
    }
}
