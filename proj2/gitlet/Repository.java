package gitlet;

import gitlet.storage.Commit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Repository {

    public static boolean isInitialized() {
        return Store.isInitialized();
    }

    public static void init() {
        Store.buildInfrastructure();
        Commit initialCommit = new Commit();
        String commitId = Store.saveCommit(initialCommit);
        Store.setActiveBranchTo("master");
        Store.setActiveCommitTo(commitId);
    }

    public static List<String> log() {
        Commit current = Store.getActiveCommit();
        List<String> result = new ArrayList<>();
        while (current != null) {
            result.add(current.toString());
            current = Store.readCommit(current.getFirstParent());
        }
        return result;
    }

    public static List<String> listAllCommits() {
        List<String> result = new ArrayList<>();
        List<Commit> allCommits = Store.getAllCommits();
        for (Commit commit : allCommits) {
            result.add(commit.toString());
        }
        return result;
    }

    public static void makeCommit(String message, Index index) {
        Commit newCommit = new Commit(message, index);
        String commitId = Store.saveCommit(newCommit);
        Store.setActiveCommitTo(commitId);
    }

    public static void makeCommit(String message, Index index, String secondParent) {
        Commit newCommit = new Commit(message, index);
        newCommit.setSecondParent(secondParent);
        String commitId = Store.saveCommit(newCommit);
        Repository.setActiveCommitTo(commitId);
    }

    public static Commit getCommit(String commitId) {
        return Store.readCommit(commitId);

    }
    public static List<String> find(String message) {
        List<String> foundCommits = new ArrayList<>();
        List<Commit> allCommits = Store.getAllCommits();
        for (Commit commit : allCommits) {
            if (commit.getMessage().equals(message)) {
                foundCommits.add(commit.getUid());
            }
        }
        return foundCommits;
    }

    public static void createBranch(String branchName) {
        Store.createBranch(branchName);
    }

    public static void removeBranch(String branchName) {
        Store.removeBranch(branchName);
    }

    public static boolean branchExists(String branchName) {
        return Store.branchExists(branchName);
    }

    public static boolean isActiveBranch(String branchName) {
        return Store.getActiveBranchName().equals(branchName);
    }


    public static void checkoutFileFromCommit(String fileName, Commit commit) {
        Store.checkoutFileFromCommit(fileName, commit);
    }


    public static void setActiveCommitTo(String commitId) {
        Store.setActiveCommitTo(commitId);
    }

    public static void checkoutFileFromActiveCommit(String fileName) {
        String commitId = Store.getActiveCommitId();
        Commit commit = getCommit(commitId);
        checkoutFileFromCommit(fileName, commit);
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
            result += Store.readBlob(activeCommit.getFileHash(fileName));
        }
        result += "=======" + "\n";
        if (otherCommit.hasFile(fileName)) {
            result += Store.readBlob(otherCommit.getFileHash(fileName));
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

    public static Commit findSplitCommit(Commit c1, Commit c2) {
        DAG dag = new DAG();
        dag.addSourceNode(c1);
        dag.addSourceNode(c2);
        return dag.getLatestCommonAncestor(c1, c2);
    }

    public static boolean remoteExists(String remoteName) {
        return Store.remoteExists(remoteName);
    }
    public static void addRemote(String remoteName, String remoteDirName) {
        Store.addRemote(remoteName, remoteDirName);
    }

    public static void removeRemote(String remoteName) {
        Store.removeRemote(remoteName);
    }

    public static void push(String remoteName, String remoteBranchName) {
        Store.copyLocalObjectsToDistant(remoteName);
        Store.copyLocalBranchHeadToDistant(remoteName, remoteBranchName);
    }


    public static void fetch(String remoteName, String remoteBranchName) {
        Store.copyDistantObjectsToLocal(remoteName);
        Store.copyDistantBranchHeadToLocal(remoteName, remoteBranchName);
    }

    public static boolean remoteUrlExists(String remoteName) {
        return Store.remoteUrlExists(remoteName);
    }

    public static boolean remoteBranchExists(String remoteName, String remoteBranchName) {
        return Store.distantBranchExists(remoteName, remoteBranchName);
    }


    public static Commit getBranchHeadCommit(String branchName) {
        return Store.getBranchHeadCommit(branchName);
    }

    public static void checkoutFilesFromCommit(Commit branchHeadCommit) {
        Store.checkoutFilesFromCommit(branchHeadCommit);
    }

    public static void setActiveBranchTo(String branchName) {
        Store.setActiveBranchTo(branchName);
    }

    public static void updateIndexOnMerge(Index index, Commit activeCommit, Commit otherCommit, Commit splitCommit) {
        Set<String> allFileNames = Repository.getFileNamesInMerge(splitCommit,
                activeCommit,
                otherCommit);
        for (String fileName : allFileNames) {
            if (activeCommit.hasSameEntryFor(fileName, splitCommit)
                    && !otherCommit.hasFile(fileName)) {
                index.remove(fileName);
            }
            if (otherCommit.hasCreated(fileName, splitCommit)
                    || otherCommit.hasModified(fileName, splitCommit)) {
                checkoutFileFromCommit(fileName, otherCommit);
                index.add(fileName);
            }
            if (Repository.modifiedInDifferentWays(fileName, activeCommit,
                    otherCommit,
                    splitCommit)) {
                System.out.println("Encountered a merge conflict.");
                String fixedContent = Repository.fixConflict(fileName, activeCommit, otherCommit);
                WorkingDir.writeContentToFile(fileName, fixedContent);
                index.add(fileName);
            }
        }
    }

    public static String getActiveBranchName() {
        return Store.getActiveBranchName();
    }

    public static Commit getActiveCommit() {
        return Store.getActiveCommit();
    }

    public static boolean isLocalBehindRemote(String remoteName, String remoteBranchName) {
        return Store.isLocalBehindRemote(remoteName, remoteBranchName);
    }
}
