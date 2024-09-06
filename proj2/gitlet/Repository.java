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

    public static Statuses init() {
        if (Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_ALREADY_INIT;
        }
        Persistor.buildInfrastructure();
        Commit initialCommit = new Commit();
        String commitId = Persistor.saveCommit(initialCommit);
        Persistor.setActiveBranchTo("master");
        Persistor.setActiveCommitTo(commitId);
        return Statuses.SUCCESS;
    }

    public static Statuses add(String fileName) {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        if (!WorkingDir.fileExists(fileName)) {
            return Statuses.ERR_FILE_NOT_EXIST;
        }
        Index index = Persistor.readIndex();
        index.add(fileName);
        Persistor.saveIndex(index);
        return Statuses.SUCCESS;
    }

    public static Statuses status() {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        Index index = Persistor.readIndex();
        index.status();
        return Statuses.SUCCESS;
    }

    public static Statuses log() {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        Commit current = Persistor.getActiveCommit();
        List<String> result = new ArrayList<>();
        while (current != null) {
            result.add(current.toString());
            current = Persistor.readCommit(current.getFirstParent());
        }
        System.out.println(String.join("\n", result));
        return Statuses.SUCCESS;
    }

    public static Statuses globalLog() {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        List<Commit> allCommits = Persistor.getAllCommits();
        for (Commit commit : allCommits) {
            System.out.println(commit);
        }
        return Statuses.SUCCESS;
    }

    public static Statuses commit(String  message, String secondParent) {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        if (message.isEmpty()) {
            return Statuses.ERR_EMPTY_COMMIT_MESSAGE;
        }
        Index index = Persistor.readIndex();
        if (index.nothingToAddOrRemove()) {
            return Statuses.ERR_NO_CHANGES_TO_COMMIT;
        }
        Commit newCommit = new Commit(message, index);
        newCommit.setSecondParent(secondParent);
        String commitId = Persistor.saveCommit(newCommit);
        Persistor.setActiveCommitTo(commitId);
        index.clear();
        Persistor.saveIndex(index);
        return Statuses.SUCCESS;
    }
    public static Statuses commit(String message) {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        if (message.isEmpty()) {
            return Statuses.ERR_EMPTY_COMMIT_MESSAGE;
        }
        Index index = Persistor.readIndex();
        if (index.nothingToAddOrRemove()) {
            return Statuses.ERR_NO_CHANGES_TO_COMMIT;
        }
        Commit newCommit = new Commit(message, index);
        String commitId = Persistor.saveCommit(newCommit);
        Persistor.setActiveCommitTo(commitId);
        index.clear();
        Persistor.saveIndex(index);
        return Statuses.SUCCESS;
    }

    public static Statuses checkoutFileFromActiveCommit(String fileName) {
        String commitId = Persistor.getActiveCommitId();
        checkoutFileFromCommit(fileName, commitId);
        return Statuses.SUCCESS;
    }

    public static Statuses checkoutFileFromCommit(String fileName, String commitID) {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        Commit commit = Persistor.readCommit(commitID);
        if (commit == null) {
            return Statuses.ERR_NOT_EXIST_SUCH_COMMIT;
        }
        if (!commit.hasFile(fileName)) {
            return Statuses.ERR_FILE_NOT_EXIST_IN_COMMIT;
        }
        Persistor.checkoutFileFromCommit(fileName, commit);
        return Statuses.SUCCESS;
    }

    public static Statuses checkoutFilesFromBranchHead(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        if (!Persistor.branchExists(branchName)) {
            return Statuses.ERR_BRANCH_NOT_EXIST;
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            return Statuses.ERR_BRANCH_NOT_NEED_CHECKOUT;
        }

        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            return Statuses.ERR_UNTRACKED_FILES;
        }
        Commit branchHeadCommit = Persistor.getBranchHeadCommit(branchName);
        Persistor.checkoutFilesFromCommit(branchHeadCommit);
        index.clear();
        index.setRepo(branchHeadCommit.getFilesTable());
        Persistor.saveIndex(index);
        Persistor.setActiveBranchTo(branchName);
        return Statuses.SUCCESS;
    }

    public static Statuses reset(String commitID) {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        Commit commit = Persistor.readCommit(commitID);
        if (commit == null) {
            return Statuses.ERR_NOT_EXIST_SUCH_COMMIT;
        }

        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            return Statuses.ERR_UNTRACKED_FILES;
        }
        Persistor.checkoutFilesFromCommit(commit);
        index.clear();
        index.setRepo(commit.getFilesTable());
        Persistor.saveIndex(index);
        // Also moves the current branch’s head to that commit node.
        Persistor.setActiveCommitTo(commitID);
        return Statuses.SUCCESS;
    }
    public static Statuses remove(String fileName) {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        Index index = Persistor.readIndex();
        if (index.isUntracked(fileName)) {
            return Statuses.ERR_NO_REASON_TO_REMOVE_FILE;
        }
        index.remove(fileName);
        Persistor.saveIndex(index);
        return Statuses.SUCCESS;
    }

    public static Statuses branch(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        if (Persistor.branchExists(branchName)) {
            return Statuses.ERR_BRANCH_ALREADY_EXIST;
        }
        Persistor.createBranch(branchName);
        return Statuses.SUCCESS;
    }

    public static Statuses removeBranch(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        if (!Persistor.branchExists(branchName)) {
            return Statuses.ERR_BRANCH_NOT_EXIST2;
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            return Statuses.ERR_CANNOT_REMOVE_BRANCH;
        }
        Persistor.removeBranch(branchName);
        return Statuses.SUCCESS;
    }


    public static Statuses find(String message) {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        List<String> foundCommits = new ArrayList<>();
        List<Commit> allCommits = Persistor.getAllCommits();
        for (Commit commit : allCommits) {
            if (commit.getMessage().equals(message)) {
                foundCommits.add(commit.getUid());
            }
        }
        if (foundCommits.isEmpty()) {
            return Statuses.ERR_COMMIT_NOT_FOUND;
        }
        System.out.println(String.join("\n", foundCommits));
        return Statuses.SUCCESS;
    }

    public static Statuses merge(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            return Statuses.ERR_REPO_NOT_INIT;
        }
        if (!Persistor.branchExists(branchName)) {
            return Statuses.ERR_BRANCH_NOT_EXIST2;
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            return Statuses.ERR_BRANCH_CANNOT_MERGE_ITSELF;
        }
        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            return Statuses.ERR_UNTRACKED_FILES;
        }
        if (!index.nothingToAddOrRemove()) {
            return Statuses.ERR_UNCOMMITED_CHANGES;
        }
        Commit activeCommit = Persistor.getActiveCommit();
        Commit otherCommit = Persistor.getBranchHeadCommit(branchName);
        Commit splitCommit = findSplitCommit(activeCommit, otherCommit);
        if (splitCommit.getUid().equals(otherCommit.getUid())) {
            return Statuses.ERR_BRANCH_ANCESTOR;
        }
        if (splitCommit.getUid().equals(activeCommit.getUid())) {
            checkoutFilesFromBranchHead(branchName);
            return Statuses.ERR_BRANCH_FAST_FORWARDED;
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
        return Statuses.SUCCESS;
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

    public static Statuses addRemote(String remoteName, String remoteDirName) {
//                System.out.println("ARG1 " + remoteName);
//                System.out.println("ARG2 " + remoteDirName);
        if (Persistor.remoteExists(remoteName)) {
            return Statuses.ERR_REMOTE_ALREADY_EXIST;
        }
        Persistor.addRemote(remoteName, remoteDirName);
        return Statuses.SUCCESS;
    }

    public static Statuses removeRemote(String remoteName) {
        if (!Persistor.remoteExists(remoteName)) {
            return Statuses.ERR_REMOTE_NOT_EXIST;
        }
        Persistor.removeRemote(remoteName);

        return Statuses.SUCCESS;
    }

    public static Statuses push(String remoteName, String remoteBranchName) {
        if (!Persistor.remoteDirExists(remoteName)) {
            return Statuses.ERR_REMOTE_DIR_NOT_FOUND;
        }
        if (!Persistor.remoteBranchExists(remoteName, remoteBranchName)) {
            return Statuses.ERR_REMOTE_NO_SUCH_BRANCH;
        }
        System.out.println("Please pull down remote changes before pushing.");
        System.exit(0);
        return Statuses.SUCCESS;

    }

    public static Statuses fetch(String remoteName, String remoteBranchName) {
        if (!Persistor.remoteDirExists(remoteName)) {
            return Statuses.ERR_REMOTE_DIR_NOT_FOUND;
        }
        if (!Persistor.remoteBranchExists(remoteName, remoteBranchName)) {
            return Statuses.ERR_REMOTE_NO_SUCH_BRANCH;
        }
        return Statuses.SUCCESS;
    }

    public static Statuses pull(String remoteName, String remoteBranchName) {
        System.out.println("File does not exist.");
        System.exit(0);
        return Statuses.SUCCESS;
    }
}
