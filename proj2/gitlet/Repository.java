package gitlet;

import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static void init() {
        if (Persistor.isRepositoryInitialized()) {
            String m = "A Gitlet version-control system already exists in the current directory.";
            System.out.println(m);
            System.exit(0);
        } else {
            Persistor.buildInfrastructure();
        }
        Commit initialCommit = new Commit();
        String commitId = Persistor.saveCommit(initialCommit);
        Persistor.setActiveBranchTo("master");
        Persistor.setActiveCommitTo(commitId);
    }

    public static void add(String fileName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (!WorkingDir.fileExists(fileName)) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        index.add(fileName);
        Persistor.saveIndex(index);
    }

    public static void status() {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        index.status();
    }

    public static void log() {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
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
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        List<Commit> allCommits = Persistor.getAllCommits();
        for (Commit commit : allCommits) {
            System.out.println(commit);
        }
    }

    public static void commit(String  message, String secondParent) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        if (index.nothingToAddOrRemove()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
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
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        if (index.nothingToAddOrRemove()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
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
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        Commit commit = Persistor.readCommit(commitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (!commit.hasFile(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        Persistor.checkoutFileFromCommit(fileName, commit);
    }

    public static void checkoutFilesFromBranchHead(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (!Persistor.branchExists(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            String message = "There is an untracked file in the way; "
                    + "delete it, or add and commit it first.";
            System.out.println(message);
            System.exit(0);
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
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        Commit commit = Persistor.readCommit(commitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            String message = "There is an untracked file in the way; "
                    + "delete it, or add and commit it first.";
            System.out.println(message);
            System.exit(0);
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
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        if (index.isUntracked(fileName)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        index.remove(fileName);
        Persistor.saveIndex(index);
    }

    public static void branch(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (Persistor.branchExists(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Persistor.createBranch(branchName);
    }

    public static void removeBranch(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (!Persistor.branchExists(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        Persistor.removeBranch(branchName);
    }


    public static void find(String message) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        List<String> foundCommits = new ArrayList<>();
        List<Commit> allCommits = Persistor.getAllCommits();
        for (Commit commit : allCommits) {
            if (commit.getMessage().equals(message)) {
                foundCommits.add(commit.getUid());
            }
        }
        if (foundCommits.isEmpty()) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
        System.out.println(String.join("\n", foundCommits));
    }

    public static void merge(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (!Persistor.branchExists(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            String message = "There is an untracked file in the way; "
                    + "delete it, or add and commit it first.";
            System.out.println(message);
            System.exit(0);
        }
        if (!index.nothingToAddOrRemove()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        Commit activeCommit = Persistor.getActiveCommit();
        Commit otherCommit = Persistor.getBranchHeadCommit(branchName);
        Commit splitCommit = findSplitCommit(activeCommit, otherCommit);
        if (splitCommit.getUid().equals(otherCommit.getUid())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (splitCommit.getUid().equals(activeCommit.getUid())) {
            System.out.println("Current branch fast-forwarded.");
            checkoutFilesFromBranchHead(branchName);
            System.exit(0);
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
//                System.out.println("ARG1 " + remoteName);
//                System.out.println("ARG2 " + remoteDirName);
        if (Persistor.remoteExists(remoteName)) {
            System.out.println("A remote with that name already exists.");
            System.exit(0);
        }
        Persistor.addRemote(remoteName, remoteDirName);
        Path path = Paths.get(remoteDirName).toAbsolutePath().normalize();
        //buildInfrastructure(Utils.join(path.normalize().toFile()));
    }

    public static void removeRemote(String remoteName) {
        if (!Persistor.remoteExists(remoteName)) {
            System.out.println("A remote with that name does not exist.");
            System.exit(0);
        }
        Persistor.removeRemote(remoteName);

    }

    public static void push(String remoteName, String remoteBranchName) {
        if (!Persistor.remoteDirExists(remoteName)) {
            System.out.println("Remote directory not found.");
            System.exit(0);
        }
        if (!Persistor.remoteBranchExists(remoteName, remoteBranchName)) {
            System.out.println("That remote does not have that branch.");
            System.exit(0);
        }
        System.out.println("Please pull down remote changes before pushing.");
        System.exit(0);

    }

    public static void fetch(String remoteName, String remoteBranchName) {
        if (!Persistor.remoteDirExists(remoteName)) {
            System.out.println("Remote directory not found.");
            System.exit(0);
        }
        if (!Persistor.remoteBranchExists(remoteName, remoteBranchName)) {
            System.out.println("That remote does not have that branch.");
            System.exit(0);
        }
    }

    public static void pull(String remoteName, String remoteBranchName) {
        System.out.println("File does not exist.");
        System.exit(0);
    }
}
