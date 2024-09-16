package gitlet;

import gitlet.storage.Commit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Repository {

    public static boolean isInitialized() {
        return Persistor.isRepositoryInitialized();
    }

    public static void init() {
        Persistor.buildInfrastructure();
        Commit initialCommit = new Commit();
        String commitId = Persistor.saveCommit(initialCommit);
        Persistor.setActiveBranchTo("master");
        Persistor.setActiveCommitTo(commitId);
    }

    public static List<String> log() {
        Commit current = Persistor.getActiveCommit();
        List<String> result = new ArrayList<>();
        while (current != null) {
            result.add(current.toString());
            current = Persistor.readCommit(current.getFirstParent());
        }
        return result;
    }

    public static List<String> listAllCommits() {
        List<String> result = new ArrayList<>();
        List<Commit> allCommits = Persistor.getAllCommits();
        for (Commit commit : allCommits) {
            result.add(commit.toString());
        }
        return result;
    }

    public static void makeCommit(String message, Index index) {
        Commit newCommit = new Commit(message, index);
        String commitId = Persistor.saveCommit(newCommit);
        Persistor.setActiveCommitTo(commitId);
    }

    public static Commit getCommit(String commitId) {
        return Persistor.readCommit(commitId);

    }
    public static List<String> find(String message) {
        List<String> foundCommits = new ArrayList<>();
        List<Commit> allCommits = Persistor.getAllCommits();
        for (Commit commit : allCommits) {
            if (commit.getMessage().equals(message)) {
                foundCommits.add(commit.getUid());
            }
        }
        return foundCommits;
    }
    public static void checkoutFileFromCommit(String fileName, Commit commit) {
        Persistor.checkoutFileFromCommit(fileName, commit);
    }

    public static void createBranch(String branchName) {
        Persistor.createBranch(branchName);
    }

    public static void removeBranch(String branchName) {
        Persistor.removeBranch(branchName);
    }

    public static boolean branchExists(String branchName) {
        return Persistor.branchExists(branchName);
    }

    public static boolean isActiveBranch(String branchName) {
        return Persistor.getActiveBranchName().equals(branchName);
    }

    public static void setActiveCommitTo(String commitId) {
        Persistor.setActiveCommitTo(commitId);
    }

    public static void checkoutFileFromActiveCommit(String fileName) {
        String commitId = Persistor.getActiveCommitId();
        Commit commit = getCommit(commitId);
        checkoutFileFromCommit(fileName, commit);
    }


    public static boolean modifiedInDifferentWays(String fileName,
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
    public static String fixConflict(String fileName, Commit activeCommit, Commit otherCommit) {
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

    public static Set<String> getFileNamesInMerge(Commit c1, Commit c2, Commit c3) {
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
        return Persistor.remoteExists(remoteName);
    }
    public static void addRemote(String remoteName, String remoteDirName) {
        Persistor.addRemote(remoteName, remoteDirName);
    }

    public static void removeRemote(String remoteName) {
        Persistor.removeRemote(remoteName);
    }

    public static void push(String remoteName, String remoteBranchName) {
        Persistor.copyLocalObjectsToDistant(remoteName);
        Persistor.copyLocalBranchHeadToDistant(remoteName, remoteBranchName);
    }


    public static void fetch(String remoteName, String remoteBranchName) {
        Persistor.copyDistantObjectsToLocal(remoteName);
        Persistor.copyDistantBranchHeadToLocal(remoteName, remoteBranchName);
    }

    public static boolean remoteUrlExists(String remoteName) {
        return Persistor.remoteUrlExists(remoteName);
    }

    public static boolean remoteBranchExists(String remoteName, String remoteBranchName) {
        return Persistor.distantBranchExists(remoteName, remoteBranchName);
    }


    public static Commit getBranchHeadCommit(String branchName) {
        return Persistor.getBranchHeadCommit(branchName);
    }

    public static void checkoutFilesFromCommit(Commit branchHeadCommit) {
        Persistor.checkoutFilesFromCommit(branchHeadCommit);
    }

    public static void setActiveBranchTo(String branchName) {
        Persistor.setActiveBranchTo(branchName);
    }
}
