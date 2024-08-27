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
        String hash = Persistor.saveBlob(fileName);
        filesToAdd.put(fileName, hash);
    }

    public void remove(String fileName) {
        filesToAdd.remove(fileName);
        if (repo.containsKey(fileName)) {
            filesToRemove.put(fileName, repo.get(fileName));
            Persistor.removeCWDFile(fileName);
        }
    }

    public void clear() {
        filesToAdd.clear();
        filesToRemove.clear();
    }

    public void status() {
        String result = "=== Branches ===" + "\n"
                + formatSetToString(getBranchesAsSet()) + "\n"
                + "=== Staged Files ===" + "\n"
                + formatSetToString(filesToAdd.keySet()) + "\n"
                + "=== Removed Files ===" + "\n"
                + formatSetToString(filesToRemove.keySet()) + "\n"
                + "=== Modifications Not Staged For Commit ===" + "\n"
                + formatSetToString(getModifiedNotStaged()) + "\n"
                + "=== Untracked Files ===" + "\n"
                + formatSetToString(getUntrackedFileNames());
        System.out.println(result);
    }

    private Set<String> getBranchesAsSet() {
        List<String> branchNames = Persistor.readAllBranchNames();
        String activeBranch = Persistor.getActiveBranchName();
        TreeSet<String> result = new TreeSet<>();
        for (String branchName : branchNames) {
            if (branchName.equals(activeBranch)) {
                result.add("*" + branchName);
            } else {
                result.add(branchName);
            }
        }
        return result;
    }

    private Set<String> getModifiedNotStaged() {
        Set<String> result = new HashSet<>();

        // Staged for addition, but with different contents than in the working directory; or
        // Staged for addition, but deleted in the working directory; or

        List<String> fileNames = Utils.plainFilenamesIn(Persistor.CWD);
        for (String fileName : fileNames) {
            if (isModified(fileName)) {
                result.add(fileName + " (modified)");
            }
        }
        // Not staged for removal, but tracked in the current commit and
        // deleted from the working directory.
        for (String fileName : repo.keySet()) {
            File filePath = Utils.join(Persistor.CWD, fileName);
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
        List<String> files = Utils.plainFilenamesIn(Persistor.CWD);
        for (String fileName : files) {
            if (isUntracked(fileName) || isModified(fileName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isModified(String fileName) {
        String hash = Persistor.getFileHash(fileName);
        return  (filesToAdd.containsKey(fileName) && !filesToAdd.get(fileName).equals(hash))
                || (repo.containsKey(fileName) && !repo.get(fileName).equals(hash));
    }

    public boolean isUntracked(String fileName) {
        return !filesToAdd.containsKey(fileName) && !repo.containsKey(fileName);
    }

    private boolean inRepo(String fileName) {
        if (!Persistor.fileExists(fileName)) {
            return false;
        }

        String hash = Persistor.getFileHash(fileName);
        return repo.containsKey(fileName) && repo.get(fileName).equals(hash);
    }

    private Set<String> getUntrackedFileNames() {
        Set<String> untrackedFiles = new HashSet<>();
        List<String> fileNames = Utils.plainFilenamesIn(Persistor.CWD);
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
}
