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
    public void toAdd(String fileName) {
        String hash = Persistor.saveBlob(fileName);
        if (repo.containsKey(fileName) && repo.get(fileName).equals(hash)) {
            return;
        }
        filesToAdd.put(fileName, hash);
    }

    public void toRemove(String fileName) {
        if (filesToAdd.containsKey(fileName)) {
            filesToAdd.remove(fileName);
        }
        if (repo.containsKey(fileName)) {
            filesToRemove.put(fileName, repo.get(fileName));
        }
        Persistor.removeCWDFile(fileName);
    }

    public void clear() {
        filesToAdd.clear();
        filesToRemove.clear();
    }


    public TreeMap<String, String> getFilesToAdd() {
        return filesToAdd;
    }

    public void status() {
        String result = "=== Branches ===" + "\n"
                + "*master" + "\n" + "\n"
                + "=== Staged Files ===" + "\n" + getFileNamesToAdd()
                + "=== Removed Files ===" + "\n" + getFileNamesToDelete()
                + "=== Modifications Not Staged For Commit ===" + "\n" + "\n"
                + "=== Untracked Files ===" + "\n" + getUntrackedFileNames();
        System.out.println(result);
    }

    private String getFileNamesToAdd() {
        return String.join("\n", filesToAdd.keySet()) + "\n";
    }

    private String getFileNamesToDelete() {
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
        return String.join("\n", untrackedFiles) + "\n";
    }

    public boolean fileInStageOrRepo(String fileName) {
        return filesToAdd.containsKey(fileName) || repo.containsKey(fileName);
    }
}
