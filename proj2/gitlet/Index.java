package gitlet;

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
    public Index(Commit lastCommit) {
        repo.putAll(lastCommit.getFilesTable());
    }
    public void add(String fileName) {
        String hash = Persistor.saveBlob(fileName);
        if (filesToRemove.containsKey(fileName) && filesToRemove.get(fileName).equals(hash)) {
            filesToRemove.remove(fileName);
            return;
        }
        if (repo.containsKey(fileName) && repo.get(fileName).equals(hash)) {
            return;
        }
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

    private String getUntrackedFileNames() {
        List<String> untrackedFiles = new ArrayList<String>();
        List<String> files = Utils.plainFilenamesIn(Persistor.CWD);
        for (String file : files) {
            if (!filesToAdd.containsKey(file) && !repo.containsKey(file)) {
                untrackedFiles.add(file);
            }
        }
        if (untrackedFiles.isEmpty()) {
            return "";
        }
        return String.join("\n", untrackedFiles) + "\n";
    }

    public boolean fileInStageOrRepo(String fileName) {
        return filesToAdd.containsKey(fileName) || repo.containsKey(fileName);
    }

    public TreeMap<String, String> getFilesToCommit() {
        TreeMap<String, String> result = new TreeMap<>(repo);
        result.putAll(filesToAdd);
        for (String fileName : filesToRemove.keySet()) {
            result.remove(fileName);
        }
        repo.putAll(result);
        return result;
    }
    public boolean nothingToAddOrRemove() {
        return filesToAdd.isEmpty() && filesToRemove.isEmpty();
    }
}
