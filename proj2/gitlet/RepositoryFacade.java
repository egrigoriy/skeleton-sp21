package gitlet;


import gitlet.storage.Commit;

import java.util.*;

/** Represents a gitlet repository.
 *
 *  @author Grigoriy Emiliyanov
 */
public class RepositoryFacade {
    public static void init() throws GitletException {
        if (Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_ALREADY_INIT.getText());
        }
        Repository.init();
    }

    public static void addFile(String fileName) throws GitletException {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (!WorkingDir.fileExists(fileName)) {
            throw new GitletException(Errors.ERR_FILE_NOT_EXIST.getText());
        }
        Index index = Repository.readIndex();
        index.add(fileName);
        Repository.saveIndex(index);
    }

    public static void status() {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Index index = Repository.readIndex();
        index.status();
    }

    public static void log() {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        List<String> logAsList = Repository.log();
        System.out.println(String.join("\n", logAsList));
    }

    public static void globalLog() {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        List<String> logAsList = Repository.listAllCommits();
        System.out.println(String.join("\n", logAsList));
    }

    public static void commit(String message) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (message.isEmpty()) {
            throw new GitletException(Errors.ERR_EMPTY_COMMIT_MESSAGE.getText());
        }
        Index index = Repository.readIndex();
        if (index.nothingToAddOrRemove()) {
            throw new GitletException(Errors.ERR_NO_CHANGES_TO_COMMIT.getText());
        }
        Repository.makeCommit(message, index);
        index.clear();
        Repository.saveIndex(index);
    }

    public static void checkoutFileFromActiveCommit(String fileName) {
        Repository.checkoutFileFromActiveCommit(fileName);
    }

    public static void checkoutFileFromCommit(String fileName, String commitId) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Commit commit = Repository.getCommit(commitId);
        if (commit == null) {
            throw new GitletException(Errors.ERR_NOT_EXIST_SUCH_COMMIT.getText());
        }
        if (!commit.hasFile(fileName)) {
            throw new GitletException(Errors.ERR_FILE_NOT_EXIST_IN_COMMIT.getText());
        }
        Repository.checkoutFileFromCommit(fileName, commit);
    }

    public static void checkoutFilesFromBranchHead(String branchName) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Branch branch = new Branch(branchName);
        if (!branch.exists()) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST.getText());
        }
        if (branch.isActive()) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_NEED_CHECKOUT.getText());
        }

        Index index = Repository.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        Commit branchHeadCommit = branch.getHeadCommit();
        Repository.checkoutFilesFromCommit(branchHeadCommit);
        index.clear();
        index.setRepo(branchHeadCommit.getFilesTable());
        Repository.saveIndex(index);
        branch.activate();
    }

    public static void reset(String commitId) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Commit commit = Repository.getCommit(commitId);
        if (commit == null) {
            throw new GitletException(Errors.ERR_NOT_EXIST_SUCH_COMMIT.getText());
        }
        Index index = Repository.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        Repository.checkoutFilesFromCommit(commit);
        index.clear();
        index.setRepo(commit.getFilesTable());
        Repository.saveIndex(index);
        Repository.setActiveCommitTo(commitId);
    }
    public static void removeFile(String fileName) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Index index = Repository.readIndex();
        if (index.isUntracked(fileName)) {
            throw new GitletException(Errors.ERR_NO_REASON_TO_REMOVE_FILE.getText());
        }
        index.remove(fileName);
        Repository.saveIndex(index);
    }

    public static void createBranch(String branchName) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Branch branch = new Branch(branchName);
        if (branch.exists()) {
            throw new GitletException(Errors.ERR_BRANCH_ALREADY_EXIST.getText());
        }
        branch.create();
    }

    public static void removeBranch(String branchName) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Branch branch = new Branch(branchName);
        if (!branch.exists()) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST2.getText());
        }
        if (branch.isActive()) {
            throw new GitletException(Errors.ERR_CANNOT_REMOVE_BRANCH.getText());
        }
        branch.remove();
    }

    public static void find(String message) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        List<String> foundCommits = Repository.find(message);
        if (foundCommits.isEmpty()) {
            throw new GitletException(Errors.ERR_COMMIT_NOT_FOUND.getText());
        }
        System.out.println(String.join("\n", foundCommits));
    }

    public static void merge(String branchName) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Branch otherBranch = new Branch(branchName);
        if (!otherBranch.exists()) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST2.getText());
        }
        if (otherBranch.isActive()) {
            throw new GitletException(Errors.ERR_BRANCH_CANNOT_MERGE_ITSELF.getText());
        }
        Index index = Repository.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        if (!index.nothingToAddOrRemove()) {
            throw new GitletException(Errors.ERR_UNCOMMITED_CHANGES.getText());
        }
        Branch currentBranch = new Branch();
        Commit activeCommit = currentBranch.getHeadCommit();
        Commit otherCommit = otherBranch.getHeadCommit();
        Commit splitCommit = Repository.findSplitCommit(activeCommit, otherCommit);
        if (otherCommit.equals(splitCommit)) {
            throw new GitletException(Errors.ERR_BRANCH_ANCESTOR.getText());
        }
        if (activeCommit.equals(splitCommit)) {
            checkoutFilesFromBranchHead(branchName);
            throw new GitletException(Errors.ERR_BRANCH_FAST_FORWARDED.getText());
        }
        index.updateOnMerge(index, activeCommit, otherCommit, splitCommit);
        String message = "Merged " + branchName + " into " + currentBranch.getName() + ".";
        Repository.makeCommit(message, index, otherCommit.getUid());
        index.clear();
        Repository.saveIndex(index);
    }


    public static void addRemote(String remoteName, String remoteDirName) {
        Remote remote = new Remote(remoteName);
        if (remote.exists()) {
            throw new GitletException(Errors.ERR_REMOTE_ALREADY_EXIST.getText());
        }
        remote.add(remoteDirName);
    }

    public static void removeRemote(String remoteName) {
        Remote remote = new Remote(remoteName);
        if (!remote.exists()) {
            throw new GitletException(Errors.ERR_REMOTE_NOT_EXIST.getText());
        }
        remote.remove();
    }

    public static void push(String remoteName, String remoteBranchName) {
        Remote remote = new Remote(remoteName);
        if (!remote.remoteUrlExists()) {
            throw new GitletException(Errors.ERR_REMOTE_DIR_NOT_FOUND.getText());
        }
        if (!remote.branchExists(remoteBranchName)) {
            throw new GitletException(Errors.ERR_REMOTE_NO_SUCH_BRANCH.getText());
        }
        if (remote.isLocalBehindRemote(remoteBranchName)) {
            throw new GitletException(Errors.ERR_LOCAL_BEHIND_REMOTE.getText());
        }
        remote.push(remoteBranchName);
    }

    public static void fetch(String remoteName, String remoteBranchName) {
        Remote remote = new Remote(remoteName);
        if (!remote.remoteUrlExists()) {
            throw new GitletException(Errors.ERR_REMOTE_DIR_NOT_FOUND.getText());
        }
        if (!remote.branchExists(remoteBranchName)) {
            throw new GitletException(Errors.ERR_REMOTE_NO_SUCH_BRANCH.getText());
        }
        remote.fetch(remoteBranchName);
    }

    public static void pull(String remoteName, String remoteBranchName) {
        fetch(remoteName, remoteBranchName);
        String branchName = remoteName + "/" + remoteBranchName;
        merge(branchName);
    }
}
