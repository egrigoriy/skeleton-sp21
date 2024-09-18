package gitlet;

import gitlet.commands.Commit;

public class Branch {
    private final String name;

    public Branch() {
        this.name = Store.getActiveBranchName();
    }

    public Branch(String branchName) {
        this.name = branchName;
    }

    public Commit getHeadCommit() {
        return Store.getBranchHeadCommit(name);
    }

    public void setHeadCommitTo(String commitId) {
        Store.setActiveCommitTo(commitId);
    }

    public String getHeadCommitId() {
        return Store.getBranchHeadCommitId(name);
    }

    public boolean isActive() {
        return Store.getActiveBranchName().equals(name);
    }

    public boolean exists() {
        return Store.branchExists(name);
    }

    public void create() {
        Store.createBranch(name);
    }

    public void remove() {
        Store.removeBranch(name);
    }

    public void activate() {
        Store.setActiveBranchTo(name);
    }

    public String getName() {
        return name;
    }
}

