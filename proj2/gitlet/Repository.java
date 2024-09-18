package gitlet;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Repository
 *
 *  @author Grigoriy Emiliyanov
 */
public class Repository {

    public static boolean isInitialized() {
        return Store.isInitialized();
    }

    public static void init() {
        Store.buildInfrastructure();
        Commit initialCommit = new Commit();
        String commitId = Store.saveCommit(initialCommit);
        Branch branch = new Branch("master");
        branch.activate();
        setActiveCommitTo(commitId);
    }

    public static String log() {
        Commit current = getActiveCommit();
        List<String> result = new ArrayList<>();
        while (current != null) {
            result.add(current.toString());
            current = Store.readCommit(current.getFirstParent());
        }
        return String.join("\n", result);
    }

    private static List<Commit> getAllCommits() {
        List<Commit> result = new ArrayList<>();
        for (String fileName : Store.getAllCommitsIds()) {
            Commit commit = Store.readCommit(fileName);
            result.add(commit);
        }
        return result;
    }

    public static String listAllCommits() {
        List<String> result = new ArrayList<>();
        List<Commit> allCommits = getAllCommits();
        for (Commit commit : allCommits) {
            result.add(commit.toString());
        }
        return String.join("\n", result);
    }

    public static void makeCommit(String message, Index index) {
        Commit newCommit = new Commit(message, index);
        String commitId = Store.saveCommit(newCommit);
        setActiveCommitTo(commitId);
    }

    public static void makeCommit(String message, Index index, String secondParent) {
        Commit newCommit = new Commit(message, index, secondParent);
        String commitId = Store.saveCommit(newCommit);
        setActiveCommitTo(commitId);
    }

    public static Commit getCommit(String commitId) {
        return Store.readCommit(commitId);
    }
    public static String find(String message) {
        List<String> foundCommits = new ArrayList<>();
        List<Commit> allCommits = getAllCommits();
        for (Commit commit : allCommits) {
            if (commit.getMessage().equals(message)) {
                foundCommits.add(commit.getUid());
            }
        }
        return String.join("\n", foundCommits);
    }

    public static Commit getActiveCommit() {
        Branch branch = new Branch();
        return branch.getHeadCommit();
    }

    public static void setActiveCommitTo(String commitId) {
        Branch branch = new Branch();
        branch.setHeadCommitTo(commitId);
    }

    public static void checkoutFileFromActiveCommit(String fileName) {
        Commit commit = getActiveCommit();
        checkoutFileFromCommit(fileName, commit);
    }

    public static void checkoutFileFromCommit(String fileName, Commit commit) {
        Store.checkoutFileFromCommit(fileName, commit);
    }

    public static void checkoutFilesFromCommit(Commit branchHeadCommit) {
        Store.checkoutFilesFromCommit(branchHeadCommit);
    }

    public static Index readIndex() {
        return Store.readIndex();
    }

    public static void saveIndex(Index index) {
        Store.saveIndex(index);
    }


    public static Commit findSplitCommit(Commit activeCommit, Commit otherCommit) {
        return DAG.findSplitCommit(activeCommit, otherCommit);
    }
}
