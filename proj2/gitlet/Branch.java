package gitlet;

/**
 * Represents a Branch with a name
 *
 *  @author Grigoriy Emiliyanov
 */
public class Branch {
    private final String name;

    /**
     * Provides the active branch
     */
    public Branch() {
        this.name = Store.getActiveBranchName();
    }

    /**
     * Provides a branch with the given name
     * @param branchName
     */
    public Branch(String branchName) {
        this.name = branchName;
    }

    /**
     * Returns the head commit of this branch
     * @return Commit
     */
    public Commit getHeadCommit() {
        return Store.getBranchHeadCommit(name);
    }

    /**
     * Returns the id of the head commit of this branch
     * @return commitId
     */
    public String getHeadCommitId() {
        return getHeadCommit().getUid();
    }
    /**
     * Sets the given commit id as head of this branch
     * @param commitId
     */
    public void setHeadCommitTo(String commitId) {
        Store.setBranchHeadCommitId(name, commitId);
    }

    /**
     * Returns true if this branch is the active one, otherwise false
     * @return boolean
     */
    public boolean isActive() {
        return Store.getActiveBranchName().equals(name);
    }

    /**
     * Returns true if this branch exists, otherwise false
     * @return boolean
     */
    public boolean exists() {
        return Store.branchExists(name);
    }

    /**
     * Creates a branch with this branch name in the repository
     */
    public void create() {
        Store.createBranch(name);
    }

    /**
     * Removes the branch with this branch name from the repository
     */
    public void remove() {
        Store.removeBranch(name);
    }

    /**
     * Makes this branch active one
     */
    public void activate() {
        Store.setActiveBranchTo(name);
    }

    /**
     * Returns the name of this branch
     * @return name
     */
    public String getName() {
        return name;
    }
}

