package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * Represents Index (staging area) of the repository
 */
public class Index implements Serializable {
    private TreeMap<String, String> filesToAdd = new TreeMap<String, String>();
    private TreeMap<String, String> filesToRemove = new TreeMap<String, String>();
    private TreeMap<String, String> repo = new TreeMap<String, String>();

    /**
     * Adds a file with given name to the index
     * @param fileName
     */
    public void add(String fileName) {
        if (filesToRemove.containsKey(fileName)) {
            filesToRemove.remove(fileName);
            return;
        }
        if (inRepo(fileName)) {
            return;
        }
        String hash = Store.saveBlob(fileName);
        filesToAdd.put(fileName, hash);
    }

    /**
     * Removes a file with given name from the index
     * @param fileName
     */
    public void remove(String fileName) {
        filesToAdd.remove(fileName);
        if (repo.containsKey(fileName)) {
            filesToRemove.put(fileName, repo.get(fileName));
            WorkingDir.removeFile(fileName);
        }
    }

    /**
     * Clears the index
     */
    public void clear() {
        filesToAdd.clear();
        filesToRemove.clear();
    }

    /**
     * Returns the status of branches and index
     * @return status of branches and index
     */
    public String status() {
        String result = "=== Branches ===" + "\n"
                + formatSetToString(Store.getBranchesStatus()) + "\n"
                + "=== Staged Files ===" + "\n"
                + formatSetToString(filesToAdd.keySet()) + "\n"
                + "=== Removed Files ===" + "\n"
                + formatSetToString(filesToRemove.keySet()) + "\n"
                + "=== Modifications Not Staged For Commit ===" + "\n"
                + formatSetToString(getModifiedNotStaged()) + "\n"
                + "=== Untracked Files ===" + "\n"
                + formatSetToString(getUntrackedFileNames());
        return result;
    }


    /**
     * Returns a set of modified, but not staged files in the working repository
     * @return a set of file names
     */
    private Set<String> getModifiedNotStaged() {
        Set<String> result = new HashSet<>();
        List<String> fileNames = WorkingDir.getFileNames();
        for (String fileName : fileNames) {
            if (isModified(fileName)) {
                result.add(fileName + " (modified)");
            }
        }
        for (String fileName : repo.keySet()) {
            File filePath = Utils.join(WorkingDir.CWD, fileName);
            if (!filesToRemove.containsKey(fileName) && !filePath.exists()) {
                result.add(fileName + " (deleted)");
            }
        }
        return result;
    }

    /**
     * Sets the given repository files table to the index
     * @param newRepo
     */
    public void setRepo(TreeMap<String, String> newRepo) {
        repo = newRepo;
    }

    /**
     * Returns true if there is an untracked file in the working directory
     * @return boolean
     */
    public boolean untrackedFileInTheWay() {
        List<String> files = WorkingDir.getFileNames();
        for (String fileName : files) {
            if (isUntracked(fileName) || isModified(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the file with given name in the working directory differs from index
     * @param fileName
     * @return boolean
     */
    public boolean isModified(String fileName) {
        String hash = WorkingDir.getFileHash(fileName);
        return  (filesToAdd.containsKey(fileName) && !filesToAdd.get(fileName).equals(hash))
                || (repo.containsKey(fileName) && !repo.get(fileName).equals(hash));
    }

    /**
     * Returns true if a file with given name is untracked, otherwise false
     * @param fileName
     * @return boolean
     */
    public boolean isUntracked(String fileName) {
        return !filesToAdd.containsKey(fileName) && !repo.containsKey(fileName);
    }

    /**
     * Returns true if a file with given name from working directory is same as in the repository
     * @param fileName
     * @return boolean
     */
    private boolean inRepo(String fileName) {
        String hash = WorkingDir.getFileHash(fileName);
        return repo.containsKey(fileName) && repo.get(fileName).equals(hash);
    }

    /**
     * Returns a set of files names that are untracked by the repository
     * @return a set of files names
     */
    private Set<String> getUntrackedFileNames() {
        Set<String> untrackedFiles = new HashSet<>();
        List<String> fileNames = WorkingDir.getFileNames();
        for (String fileName : fileNames) {
            if (isUntracked(fileName)) {
                untrackedFiles.add(fileName);
            }
        }
        return untrackedFiles;
    }

    /**
     * Updates the index repo with added and removed files and return the update
     * @return files to commit
     */
    public TreeMap<String, String> getFilesToCommit() {
        repo.putAll(filesToAdd);
        repo.keySet().removeAll(filesToRemove.keySet());
        return repo;
    }

    /**
     * Returns true if no files are added or removed, otherwise false
     * @return boolean
     */
    public boolean nothingToAddOrRemove() {
        return filesToAdd.isEmpty() && filesToRemove.isEmpty();
    }

    /**
     * Joins a set of strings together with new line delimiter
     * @param aSet
     * @return string
     */
    private String formatSetToString(Set<String> aSet) {
        if (aSet.isEmpty()) {
            return "";
        }
        return String.join("\n", aSet) + "\n";
    }

    /**
     * Updates the index on merge with given active commit, other commit and split commit
     * @param activeCommit
     * @param otherCommit
     * @param splitCommit
     */
    public void updateOnMerge(Commit activeCommit,
                              Commit otherCommit,
                              Commit splitCommit) {
        Set<String> allFileNames = getAllCommittedFileNames(splitCommit,
                activeCommit,
                otherCommit);
        for (String fileName : allFileNames) {
            if (activeCommit.hasSameEntryFor(fileName, splitCommit)
                    && !otherCommit.hasFile(fileName)) {
                remove(fileName);
            }
            if (otherCommit.hasCreated(fileName, splitCommit)
                    || otherCommit.hasModified(fileName, splitCommit)) {
                Store.checkoutFileFromCommit(fileName, otherCommit);
                add(fileName);
            }
            if (modifiedInDifferentWays(fileName, activeCommit, otherCommit, splitCommit)) {
                System.out.println("Encountered a merge conflict.");
                String fixedContent = fixConflictFileContent(fileName, activeCommit, otherCommit);
                WorkingDir.writeContentToFile(fileName, fixedContent);
                add(fileName);
            }
        }
    }

    /**
     * Returns true if a file with given name was modified in different ways from split commit
     * to given active commit and to given other commit. Otherwise returns false.
     * @param fileName
     * @param activeCommit
     * @param otherCommit
     * @param splitCommit
     * @return boolean
     */
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

    /**
     * Returns the content of a conflicted file with given name based on active commit and other commit
     * @param fileName
     * @param activeCommit
     * @param otherCommit
     * @return fixed file content
     */
    private static String fixConflictFileContent(String fileName, Commit activeCommit, Commit otherCommit) {
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

    /**
     * Returns a set of all file name involved in given commits
     * @param c1
     * @param c2
     * @param c3
     * @return a set of file names
     */
    private static Set<String> getAllCommittedFileNames(Commit c1, Commit c2, Commit c3) {
        Set<String> result = new HashSet<>();
        result.addAll(c1.getFileNames());
        result.addAll(c2.getFileNames());
        result.addAll(c3.getFileNames());
        return result;
    }
}
