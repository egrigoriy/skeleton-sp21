package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Index implements Serializable {
    private TreeMap<String, String> filesToAdd = new TreeMap<String, String>();
    private TreeMap<String, String> filesToRemove = new TreeMap<String, String>();
    private TreeMap<String, String> repo = new TreeMap<String, String>();

    public Index() {
    }

    private boolean isStagedForRemoval(String fileName) {
        byte[] fileContent = Utils.readContents(Utils.join(Persistor.CWD, fileName));
        String hash = Utils.sha1(fileContent);
        return  filesToRemove.containsKey(fileName) && filesToRemove.get(fileName).equals(hash);
    }

    private boolean inRepo(String fileName) {
        File filePath = Utils.join(Persistor.CWD, fileName);
        if (!filePath.exists()) {
            return false;
        }
        byte[] fileContent = Utils.readContents(filePath);
        String hash = Utils.sha1(fileContent);
        return repo.containsKey(fileName) && repo.get(fileName).equals(hash);
    }
    public void add(String fileName) {
        if (isStagedForRemoval(fileName)) {
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
                + getBranches() + "\n"
                + "=== Staged Files ===" + "\n"
                + getFileNamesToAdd() + "\n"
                + "=== Removed Files ===" + "\n"
                + getFileNamesToDelete() + "\n"
                + "=== Modifications Not Staged For Commit ===" + "\n"
                + getModifiedNotStaged() + "\n"
                + "=== Untracked Files ===" + "\n"
                + getUntrackedFileNames();
        System.out.println(result);
    }

    private String getModifiedNotStaged() {
        List<String> result = new ArrayList<String>();

        // Staged for addition, but with different contents than in the working directory; or
        // Staged for addition, but deleted in the working directory; or

        List<String> fileNames = Utils.plainFilenamesIn(Persistor.CWD);
        for (String fileName : fileNames) {
            byte[] fileContent = Utils.readContents(Utils.join(Persistor.CWD, fileName));
            String hash = Utils.sha1(fileContent);
            // Tracked in the current commit, changed in the working directory, but not staged; or
            if (repo.containsKey(fileName)
                    && !repo.get(fileName).equals(hash)
                    && !filesToAdd.containsKey(fileName)) {
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
        if (result.isEmpty()) {
            return "";
        }
        return String.join("\n", result) + "\n";
    }

    private String getBranches() {
        List<String> branchNames = Persistor.readAllBranchNames();
        String currentBranch = Persistor.getBranchNameFromHead();
        String result = "";
        for (String branchName : branchNames) {
            if (branchName.equals(currentBranch)) {
                result += "*" + branchName + "\n";
            } else {
                result += branchName + "\n";
            }
        }
        return result;
    }

    private String getFileNamesToAdd() {
        if (filesToAdd.isEmpty()) {
            return "";
        }
        return String.join("\n", filesToAdd.keySet()) + "\n";
    }

    private String getFileNamesToDelete() {
        if (filesToRemove.isEmpty()) {
            return "";
        }
        return String.join("\n", filesToRemove.keySet()) + "\n";
    }

    public void setRepo(TreeMap<String, String> newRepo) {
        repo = newRepo;
    }

    public boolean untrackedFileInTheWay() {
        List<String> files = Utils.plainFilenamesIn(Persistor.CWD);
        for (String fileName : files) {
            byte[] fileContent = Utils.readContents(Utils.join(Persistor.CWD, fileName));
            String hash = Utils.sha1(fileContent);
            if (!filesToAdd.containsKey(fileName) && !repo.containsKey(fileName)) {
                return true;
            }
            if (filesToAdd.containsKey(fileName) && !filesToAdd.get(fileName).equals(hash)) {
                return true;
            }
            if (repo.containsKey(fileName) && !repo.get(fileName).equals(hash)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUntracked(String fileName) {
        return !filesToAdd.containsKey(fileName) && !repo.containsKey(fileName);
    }
    private String getUntrackedFileNames() {
        List<String> untrackedFiles = new ArrayList<String>();
        List<String> fileNames = Utils.plainFilenamesIn(Persistor.CWD);
        for (String fileName : fileNames) {
            if (isUntracked(fileName)) {
                untrackedFiles.add(fileName);
            }
        }
        if (untrackedFiles.isEmpty()) {
            return "";
        }
        //System.out.println("FOO" + untrackedFiles);
        return String.join("\n", untrackedFiles) + "\n";
    }

    public TreeMap<String, String> getFilesToCommit() {
        TreeMap<String, String> filesToCommit = new TreeMap<String, String>(repo);
        filesToCommit.putAll(filesToAdd);
        filesToCommit.keySet().removeAll(filesToRemove.keySet());
        repo = filesToCommit;
        return filesToCommit;
    }
    public boolean nothingToAddOrRemove() {
        return filesToAdd.isEmpty() && filesToRemove.isEmpty();
    }
}
