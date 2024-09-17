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
        Index index = Store.readIndex();
        index.add(fileName);
        Store.saveIndex(index);
    }

    public static void status() {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Index index = Store.readIndex();
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
        Index index = Store.readIndex();
        if (index.nothingToAddOrRemove()) {
            throw new GitletException(Errors.ERR_NO_CHANGES_TO_COMMIT.getText());
        }
        Repository.makeCommit(message, index);
        index.clear();
        Store.saveIndex(index);
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
        if (!Repository.branchExists(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST.getText());
        }
        if (Repository.isActiveBranch(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_NEED_CHECKOUT.getText());
        }

        Index index = Store.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        Commit branchHeadCommit = Repository.getBranchHeadCommit(branchName);
        Repository.checkoutFilesFromCommit(branchHeadCommit);
        index.clear();
        index.setRepo(branchHeadCommit.getFilesTable());
        Store.saveIndex(index);
        Repository.setActiveBranchTo(branchName);
    }

    public static void reset(String commitID) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Commit commit = Repository.getCommit(commitID);
        if (commit == null) {
            throw new GitletException(Errors.ERR_NOT_EXIST_SUCH_COMMIT.getText());
        }

        Index index = Store.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        Repository.checkoutFilesFromCommit(commit);
        index.clear();
        index.setRepo(commit.getFilesTable());
        Store.saveIndex(index);
        Repository.setActiveCommitTo(commitID);
    }
    public static void removeFile(String fileName) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Index index = Store.readIndex();
        if (index.isUntracked(fileName)) {
            throw new GitletException(Errors.ERR_NO_REASON_TO_REMOVE_FILE.getText());
        }
        index.remove(fileName);
        Store.saveIndex(index);
    }

    public static void createBranch(String branchName) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (Repository.branchExists(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_ALREADY_EXIST.getText());
        }
        Repository.createBranch(branchName);
    }

    public static void removeBranch(String branchName) {
        if (!Repository.isInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (!Repository.branchExists(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST2.getText());
        }
        if (Repository.isActiveBranch(branchName)) {
            throw new GitletException(Errors.ERR_CANNOT_REMOVE_BRANCH.getText());
        }
        Repository.removeBranch(branchName);
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
        if (!Repository.branchExists(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST2.getText());
        }
        if (Repository.isActiveBranch(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_CANNOT_MERGE_ITSELF.getText());
        }
        Index index = Store.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        if (!index.nothingToAddOrRemove()) {
            throw new GitletException(Errors.ERR_UNCOMMITED_CHANGES.getText());
        }
        Commit activeCommit = Repository.getActiveCommit();
        Commit otherCommit = Repository.getBranchHeadCommit(branchName);
        Commit splitCommit = Repository.findSplitCommit(activeCommit, otherCommit);
        if (splitCommit.equals(otherCommit)) {
            throw new GitletException(Errors.ERR_BRANCH_ANCESTOR.getText());
        }
        if (splitCommit.equals(activeCommit)) {
            checkoutFilesFromBranchHead(branchName);
            throw new GitletException(Errors.ERR_BRANCH_FAST_FORWARDED.getText());
        }
        Repository.updateIndexOnMerge(index, activeCommit, otherCommit, splitCommit);
        String message = "Merged " + branchName + " into " + Repository.getActiveBranchName() + ".";
        Repository.makeCommit(message, index, otherCommit.getUid());
        index.clear();
        Store.saveIndex(index);
    }


    public static void addRemote(String remoteName, String remoteDirName) {
        if (Repository.remoteExists(remoteName)) {
            throw new GitletException(Errors.ERR_REMOTE_ALREADY_EXIST.getText());
        }
        Repository.addRemote(remoteName, remoteDirName);
    }

    public static void removeRemote(String remoteName) {
        if (!Repository.remoteExists(remoteName)) {
            throw new GitletException(Errors.ERR_REMOTE_NOT_EXIST.getText());
        }
        Repository.removeRemote(remoteName);
    }

    public static void push(String remoteName, String remoteBranchName) {
        if (!Repository.remoteUrlExists(remoteName)) {
            throw new GitletException(Errors.ERR_REMOTE_DIR_NOT_FOUND.getText());
        }
        if (!Repository.remoteBranchExists(remoteName, remoteBranchName)) {
            throw new GitletException(Errors.ERR_REMOTE_NO_SUCH_BRANCH.getText());
        }
        if (Repository.isLocalBehindRemote(remoteName, remoteBranchName)) {
            throw new GitletException(Errors.ERR_LOCAL_BEHIND_REMOTE.getText());
        }
        Repository.push(remoteName, remoteBranchName);
    }

    public static void fetch(String remoteName, String remoteBranchName) {
        if (!Repository.remoteUrlExists(remoteName)) {
            throw new GitletException(Errors.ERR_REMOTE_DIR_NOT_FOUND.getText());
        }
        if (!Repository.remoteBranchExists(remoteName, remoteBranchName)) {
            throw new GitletException(Errors.ERR_REMOTE_NO_SUCH_BRANCH.getText());
        }
        Repository.fetch(remoteName, remoteBranchName);
    }

    public static void pull(String remoteName, String remoteBranchName) {
        fetch(remoteName, remoteBranchName);
        String branchName = remoteName + "/" + remoteBranchName;
        merge(branchName);
    }
}
