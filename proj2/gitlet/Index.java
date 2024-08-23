package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Index implements Serializable {
    private TreeMap<String, String> filesToAdd = new TreeMap<String, String>();
    private TreeMap<String, String> filesToRemove = new TreeMap<String, String>();
    private TreeMap<String, String> repo = new TreeMap<>();
    private TreeMap<String, String> stage = new TreeMap<>();

    public Index() {
    }

    private boolean alreadyStagedForRemoval(String fileName) {
        String hash = Persistor.saveBlob(fileName);
        return  filesToRemove.containsKey(fileName) && filesToRemove.get(fileName).equals(hash);
    }

    private boolean alreadyInRepo(String fileName) {
        String hash = Persistor.saveBlob(fileName);
        return repo.containsKey(fileName) && repo.get(fileName).equals(hash);
    }
    public void add(String fileName) {
        if (alreadyStagedForRemoval(fileName)) {
            filesToRemove.remove(fileName);
            return;
        }
        if (alreadyInRepo(fileName)) {
            return;
        }
        String hash = Persistor.saveBlob(fileName);
        stage.put(fileName, hash);
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
        stage = new TreeMap<>(repo);
        filesToAdd.clear();
        filesToRemove.clear();
    }

    public void status() {
        String result = "=== Branches ===" + "\n"
                + "*master" + "\n" + "\n"
                + "=== Staged Files ===" + "\n" + getFileNamesToAdd() + "\n"
                + "=== Removed Files ===" + "\n" + getFileNamesToDelete() + "\n"
                + "=== Modifications Not Staged For Commit ===" + "\n" + "\n"
                + "=== Untracked Files ===" + "\n" + getUntrackedFileNames();
        System.out.println(result);
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
        return String.join("\n", untrackedFiles) + "\n";
    }

    public TreeMap<String, String> getFilesToCommit() {
        TreeMap<String, String> filesToCommit = new TreeMap<>(repo);
        filesToCommit.putAll(filesToAdd);
        filesToCommit.keySet().removeAll(filesToRemove.keySet());
        repo = filesToCommit;
        return filesToCommit;
    }
    public boolean nothingToAddOrRemove() {
        return filesToAdd.isEmpty() && filesToRemove.isEmpty();
    }
}
