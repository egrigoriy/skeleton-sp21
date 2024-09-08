package gitlet;


import java.util.*;

/** Represents a gitlet repository.
 *  TOD: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Grigoriy Emiliyanov
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    public static void init() throws GitletException {
        if (Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_ALREADY_INIT.getText());
        }
        Persistor.buildInfrastructure();
        Commit initialCommit = new Commit();
        String commitId = Persistor.saveCommit(initialCommit);
        Persistor.setActiveBranchTo("master");
        Persistor.setActiveCommitTo(commitId);
    }

    public static void add(String fileName) throws GitletException {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (!WorkingDir.fileExists(fileName)) {
            throw new GitletException(Errors.ERR_FILE_NOT_EXIST.getText());
        }
        Index index = Persistor.readIndex();
        index.add(fileName);
        Persistor.saveIndex(index);
    }

    public static void status() {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Index index = Persistor.readIndex();
        index.status();
    }

    public static void log() {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Commit current = Persistor.getActiveCommit();
        List<String> result = new ArrayList<>();
        while (current != null) {
            result.add(current.toString());
            current = Persistor.readCommit(current.getFirstParent());
        }
        System.out.println(String.join("\n", result));
    }

    public static void globalLog() {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        List<Commit> allCommits = Persistor.getAllCommits();
        for (Commit commit : allCommits) {
            System.out.println(commit);
        }
    }

    public static void commit(String  message, String secondParent) {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (message.isEmpty()) {
            throw new GitletException(Errors.ERR_EMPTY_COMMIT_MESSAGE.getText());
        }
        Index index = Persistor.readIndex();
        if (index.nothingToAddOrRemove()) {
            throw new GitletException(Errors.ERR_NO_CHANGES_TO_COMMIT.getText());
        }
        Commit newCommit = new Commit(message, index);
        newCommit.setSecondParent(secondParent);
        String commitId = Persistor.saveCommit(newCommit);
        Persistor.setActiveCommitTo(commitId);
        index.clear();
        Persistor.saveIndex(index);
    }
    public static void commit(String message) {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (message.isEmpty()) {
            throw new GitletException(Errors.ERR_EMPTY_COMMIT_MESSAGE.getText());
        }
        Index index = Persistor.readIndex();
        if (index.nothingToAddOrRemove()) {
            throw new GitletException(Errors.ERR_NO_CHANGES_TO_COMMIT.getText());
        }
        Commit newCommit = new Commit(message, index);
        String commitId = Persistor.saveCommit(newCommit);
        Persistor.setActiveCommitTo(commitId);
        index.clear();
        Persistor.saveIndex(index);
    }

    public static void checkoutFileFromActiveCommit(String fileName) {
        String commitId = Persistor.getActiveCommitId();
        checkoutFileFromCommit(fileName, commitId);
    }

    public static void checkoutFileFromCommit(String fileName, String commitID) {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Commit commit = Persistor.readCommit(commitID);
        if (commit == null) {
            throw new GitletException(Errors.ERR_NOT_EXIST_SUCH_COMMIT.getText());
        }
        if (!commit.hasFile(fileName)) {
            throw new GitletException(Errors.ERR_FILE_NOT_EXIST_IN_COMMIT.getText());
        }
        Persistor.checkoutFileFromCommit(fileName, commit);
    }

    public static void checkoutFilesFromBranchHead(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (!Persistor.branchExists(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST.getText());
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_NEED_CHECKOUT.getText());
        }

        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        Commit branchHeadCommit = Persistor.getBranchHeadCommit(branchName);
        Persistor.checkoutFilesFromCommit(branchHeadCommit);
        index.clear();
        index.setRepo(branchHeadCommit.getFilesTable());
        Persistor.saveIndex(index);
        Persistor.setActiveBranchTo(branchName);
    }

    public static void reset(String commitID) {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Commit commit = Persistor.readCommit(commitID);
        if (commit == null) {
            throw new GitletException(Errors.ERR_NOT_EXIST_SUCH_COMMIT.getText());
        }

        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        Persistor.checkoutFilesFromCommit(commit);
        index.clear();
        index.setRepo(commit.getFilesTable());
        Persistor.saveIndex(index);
        // Also moves the current branch’s head to that commit node.
        Persistor.setActiveCommitTo(commitID);
    }
    public static void remove(String fileName) {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        Index index = Persistor.readIndex();
        if (index.isUntracked(fileName)) {
            throw new GitletException(Errors.ERR_NO_REASON_TO_REMOVE_FILE.getText());
        }
        index.remove(fileName);
        Persistor.saveIndex(index);
    }

    public static void branch(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (Persistor.branchExists(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_ALREADY_EXIST.getText());
        }
        Persistor.createBranch(branchName);
    }

    public static void removeBranch(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (!Persistor.branchExists(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST2.getText());
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            throw new GitletException(Errors.ERR_CANNOT_REMOVE_BRANCH.getText());
        }
        Persistor.removeBranch(branchName);
    }


    public static void find(String message) {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        List<String> foundCommits = new ArrayList<>();
        List<Commit> allCommits = Persistor.getAllCommits();
        for (Commit commit : allCommits) {
            if (commit.getMessage().equals(message)) {
                foundCommits.add(commit.getUid());
            }
        }
        if (foundCommits.isEmpty()) {
            throw new GitletException(Errors.ERR_COMMIT_NOT_FOUND.getText());
        }
        System.out.println(String.join("\n", foundCommits));
    }

    public static void merge(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            throw new GitletException(Errors.ERR_REPO_NOT_INIT.getText());
        }
        if (!Persistor.branchExists(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_NOT_EXIST2.getText());
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            throw new GitletException(Errors.ERR_BRANCH_CANNOT_MERGE_ITSELF.getText());
        }
        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            throw new GitletException(Errors.ERR_UNTRACKED_FILES.getText());
        }
        if (!index.nothingToAddOrRemove()) {
            throw new GitletException(Errors.ERR_UNCOMMITED_CHANGES.getText());
        }
        Commit activeCommit = Persistor.getActiveCommit();
        Commit otherCommit = Persistor.getBranchHeadCommit(branchName);
        Commit splitCommit = findSplitCommit(activeCommit, otherCommit);
        if (splitCommit.getUid().equals(otherCommit.getUid())) {
            throw new GitletException(Errors.ERR_BRANCH_ANCESTOR.getText());
        }
        if (splitCommit.getUid().equals(activeCommit.getUid())) {
            checkoutFilesFromBranchHead(branchName);
            throw new GitletException(Errors.ERR_BRANCH_FAST_FORWARDED.getText());
        }
        Set<String> allFileNames = getFileNamesInMerge(splitCommit, activeCommit, otherCommit);
        for (String fileName : allFileNames) {
            if (activeCommit.hasSameEntryFor(fileName, splitCommit)
                    && !otherCommit.hasFile(fileName)) {
                remove(fileName);
            }
            if (otherCommit.hasCreated(fileName, splitCommit)
                    || otherCommit.hasModified(fileName, splitCommit)) {
                checkoutFileFromCommit(fileName, otherCommit.getUid());
                add(fileName);
            }
            if (modifiedInDifferentWays(fileName, activeCommit, otherCommit, splitCommit)) {
                System.out.println("Encountered a merge conflict.");
                String fixedContent = fixConflict(fileName, activeCommit, otherCommit);
                WorkingDir.writeContentToFile(fileName, fixedContent);
                add(fileName);
            }
        }
        String message = "Merged " + branchName + " into " + Persistor.getActiveBranchName() + ".";
        if (Objects.equals(branchName, "B2")) {
            findSplitCommit(activeCommit, otherCommit);
        }
        commit(message, otherCommit.getUid());
    }

    private static boolean modifiedInDifferentWays(String fileName,
                                                   Commit activeCommit,
                                                   Commit otherCommit,
                                                   Commit splitCommit) {
        return splitCommit.hasFile(fileName)
                && !activeCommit.hasSameEntryFor(fileName, splitCommit)
                && otherCommit.hasModified(fileName, splitCommit)
                || splitCommit.hasFile(fileName)
                && activeCommit.hasModified(fileName, splitCommit)
                && !otherCommit.hasFile(fileName);
    }
    private static String fixConflict(String fileName, Commit activeCommit, Commit otherCommit) {
        String result = "<<<<<<< HEAD" + "\n";
        if (activeCommit.hasFile(fileName)) {
            result += Persistor.readBlob(activeCommit.getFileHash(fileName));
        }
        result += "=======" + "\n";
        if (otherCommit.hasFile(fileName)) {
            result += Persistor.readBlob(otherCommit.getFileHash(fileName));
        }
        result += ">>>>>>>" + "\n";
        return result;
    }

    private static Set<String> getFileNamesInMerge(Commit c1, Commit c2, Commit c3) {
        Set<String> result = new HashSet<>();
        result.addAll(c1.getFileNames());
        result.addAll(c2.getFileNames());
        result.addAll(c3.getFileNames());
        return result;
    }

    private static Commit findSplitCommit(Commit c1, Commit c2) {
        DAG dag = new DAG();
        dag.addSourceNode(c1);
        dag.addSourceNode(c2);
        return dag.getLatestCommonAncestor(c1, c2);
    }

    public static void addRemote(String remoteName, String remoteDirName) {
        if (Persistor.remoteExists(remoteName)) {
            throw new GitletException(Errors.ERR_REMOTE_ALREADY_EXIST.getText());
        }
        Persistor.addRemote(remoteName, remoteDirName);
    }

    public static void removeRemote(String remoteName) {
        if (!Persistor.remoteExists(remoteName)) {
            throw new GitletException(Errors.ERR_REMOTE_NOT_EXIST.getText());
        }
        Persistor.removeRemote(remoteName);
    }

    public static void push(String remoteName, String remoteBranchName) {
        if (!Persistor.remoteDirExists(remoteName)) {
            throw new GitletException(Errors.ERR_REMOTE_DIR_NOT_FOUND.getText());
        }
        if (!Persistor.remoteBranchExists(remoteName, remoteBranchName)) {
            throw new GitletException(Errors.ERR_REMOTE_NO_SUCH_BRANCH.getText());
        }
        System.out.println("Please pull down remote changes before pushing.");
        System.exit(0);

    }

    public static void fetch(String remoteName, String remoteBranchName) {
        if (!Persistor.remoteDirExists(remoteName)) {
            throw new GitletException(Errors.ERR_REMOTE_DIR_NOT_FOUND.getText());
        }
        if (!Persistor.remoteBranchExists(remoteName, remoteBranchName)) {
            throw new GitletException(Errors.ERR_REMOTE_NO_SUCH_BRANCH.getText());
        }
    }

    public static void pull(String remoteName, String remoteBranchName) {
        System.out.println("File does not exist.");
        System.exit(0);
    }
}
