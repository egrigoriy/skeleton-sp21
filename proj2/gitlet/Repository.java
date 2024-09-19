package gitlet;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Repository with high level commands
 *
 *  @author Grigoriy Emiliyanov
 */
public class Repository {

    /**
     * Returns true if the repository is initialized, otherwise false
     * @return boolean
     */
    public static boolean isInitialized() {
        return Store.isInitialized();
    }

    /**
     * Initializes the repository (infrastructure build + initial commit)
     */
    public static void init() {
        Store.buildInfrastructure();
        Commit initialCommit = new Commit();
        String commitId = Store.saveCommit(initialCommit);
        Branch branch = new Branch("master");
        branch.activate();
        setActiveCommitTo(commitId);
    }

    /**
     * Returns the log of the active branch as a string
     * @return the log of the active branch
     */
    public static String log() {
        Commit current = getActiveCommit();
        List<String> result = new ArrayList<>();
        while (current != null) {
            result.add(current.toString());
            current = Store.readCommit(current.getFirstParent());
        }
        return String.join("\n", result);
    }

    /**
     * Returns all created commits as a string
     * @return all created commits as a string
     */
    public static String allCommitsAsString() {
        List<String> result = new ArrayList<>();
        List<Commit> allCommits = Store.getAllCommits();
        for (Commit commit : allCommits) {
            result.add(commit.toString());
        }
        return String.join("\n", result);
    }

    /**
     * Makes commit of the given index with given message. Head commit is updated.
     * @param message
     * @param index
     */
    public static void makeCommit(String message, Index index) {
        Commit newCommit = new Commit(message, index);
        String commitId = Store.saveCommit(newCommit);
        setActiveCommitTo(commitId);
    }

    /**
     * Makes commit of the given index with given message and given second parent.
     * Head commit is updated.
     * @param message
     * @param index
     * @param secondParent
     */
    public static void makeCommit(String message, Index index, String secondParent) {
        Commit newCommit = new Commit(message, index, secondParent);
        String commitId = Store.saveCommit(newCommit);
        setActiveCommitTo(commitId);
    }

    /**
     * Returns the commit with given id
     * @param commitId
     * @return commit with given id
     */
    public static Commit getCommit(String commitId) {
        return Store.readCommit(commitId);
    }

    /**
     * Returns as a string all ids of commits which message contains the given search text
     * @param searchText
     * @return all ids of found commits
     */
    public static String find(String searchText) {
        List<String> foundCommits = new ArrayList<>();
        List<Commit> allCommits = Store.getAllCommits();
        for (Commit commit : allCommits) {
            if (commit.getMessage().equals(searchText)) {
                foundCommits.add(commit.getUid());
            }
        }
        return String.join("\n", foundCommits);
    }

    /**
     * Returns the head commit of the active branch
     * @return head commit of the active branch
     */
    private static Commit getActiveCommit() {
        Branch branch = new Branch();
        return branch.getHeadCommit();
    }

    /**
     * Sets the given commit id as head commit of the active branch
     */
    public static void setActiveCommitTo(String commitId) {
        Branch branch = new Branch();
        branch.setHeadCommitTo(commitId);
    }

    /**
     * Checks out a given file name from the head commit of the active branch
     * @param fileName
     */
    public static void checkoutFileFromActiveCommit(String fileName) {
        Commit commit = getActiveCommit();
        checkoutFileFromCommit(fileName, commit);
    }

    /**
     * Checks out a given file name from a given commit
     * @param fileName
     * @param commit
     */
    public static void checkoutFileFromCommit(String fileName, Commit commit) {
        Store.checkoutFileFromCommit(fileName, commit);
    }

    /**
     * Checks out all files from a given commit
     * @param commit
     */
    public static void checkoutFilesFromCommit(Commit commit) {
        Store.checkoutFilesFromCommit(commit);
    }

    /**
     * Returns the index as stored in the repository
     * @return the index
     */
    public static Index readIndex() {
        return Store.readIndex();
    }

    /**
     * Saves the given index to the repository
     * @param index
     */
    public static void saveIndex(Index index) {
        Store.saveIndex(index);
    }

    /**
     * Find the split commit of two given commits
     * @param commit1
     * @param commit2
     * @return
     */
    public static Commit findSplitCommit(Commit commit1, Commit commit2) {
        return DAG.findSplitCommit(commit1, commit2);
    }
}
