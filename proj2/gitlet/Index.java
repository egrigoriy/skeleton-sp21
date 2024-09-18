package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class Index implements Serializable {
    private TreeMap<String, String> filesToAdd = new TreeMap<String, String>();
    private TreeMap<String, String> filesToRemove = new TreeMap<String, String>();
    private TreeMap<String, String> repo = new TreeMap<String, String>();

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

    public void remove(String fileName) {
        filesToAdd.remove(fileName);
        if (repo.containsKey(fileName)) {
            filesToRemove.put(fileName, repo.get(fileName));
            WorkingDir.removeFile(fileName);
        }
    }

    public void clear() {
        filesToAdd.clear();
        filesToRemove.clear();
    }

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



    private Set<String> getModifiedNotStaged() {
        Set<String> result = new HashSet<>();

        // Staged for addition, but with different contents than in the working directory; or
        // Staged for addition, but deleted in the working directory; or

        List<String> fileNames = WorkingDir.getFileNames();
        for (String fileName : fileNames) {
            if (isModified(fileName)) {
                result.add(fileName + " (modified)");
            }
        }
        // Not staged for removal, but tracked in the current commit and
        // deleted from the working directory.
        for (String fileName : repo.keySet()) {
            File filePath = Utils.join(WorkingDir.CWD, fileName);
            if (!filesToRemove.containsKey(fileName) && !filePath.exists()) {
                result.add(fileName + " (deleted)");
            }
        }
        return result;
    }

    public void setRepo(TreeMap<String, String> newRepo) {
        repo = newRepo;
    }

    public boolean untrackedFileInTheWay() {
        List<String> files = WorkingDir.getFileNames();
        for (String fileName : files) {
            if (isUntracked(fileName) || isModified(fileName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isModified(String fileName) {
        String hash = WorkingDir.getFileHash(fileName);
        return  (filesToAdd.containsKey(fileName) && !filesToAdd.get(fileName).equals(hash))
                || (repo.containsKey(fileName) && !repo.get(fileName).equals(hash));
    }

    public boolean isUntracked(String fileName) {
        return !filesToAdd.containsKey(fileName) && !repo.containsKey(fileName);
    }

    private boolean inRepo(String fileName) {
        String hash = WorkingDir.getFileHash(fileName);
        return repo.containsKey(fileName) && repo.get(fileName).equals(hash);
    }

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

    public TreeMap<String, String> getFilesToCommit() {
        repo.putAll(filesToAdd);
        repo.keySet().removeAll(filesToRemove.keySet());
        return repo;
    }
    public boolean nothingToAddOrRemove() {
        return filesToAdd.isEmpty() && filesToRemove.isEmpty();
    }

    private String formatSetToString(Set<String> aSet) {
        if (aSet.isEmpty()) {
            return "";
        }
        return String.join("\n", aSet) + "\n";
    }
    public void updateOnMerge(Index index,
                              Commit activeCommit,
                              Commit otherCommit,
                              Commit splitCommit) {
        Set<String> allFileNames = getFileNamesInMerge(splitCommit,
                activeCommit,
                otherCommit);
        for (String fileName : allFileNames) {
            if (activeCommit.hasSameEntryFor(fileName, splitCommit)
                    && !otherCommit.hasFile(fileName)) {
                index.remove(fileName);
            }
            if (otherCommit.hasCreated(fileName, splitCommit)
                    || otherCommit.hasModified(fileName, splitCommit)) {
                Store.checkoutFileFromCommit(fileName, otherCommit);
                index.add(fileName);
            }
            if (modifiedInDifferentWays(fileName, activeCommit, otherCommit, splitCommit)) {
                System.out.println("Encountered a merge conflict.");
                String fixedContent = fixConflict(fileName, activeCommit, otherCommit);
                WorkingDir.writeContentToFile(fileName, fixedContent);
                index.add(fileName);
            }
        }
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
//        return splitCommit.hasFile(fileName)
//                && activeCommit.hasModified(fileName, splitCommit)
//                && !activeCommit.hasSameEntryFor(fileName, otherCommit);
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


}
