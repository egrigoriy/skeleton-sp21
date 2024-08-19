package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Index implements Serializable {
    private TreeMap<String, String> filesToAdd = new TreeMap<String, String>();
    private TreeMap<String, String> stage = new TreeMap<String, String>();
    private TreeMap<String, String> repo = new TreeMap<String, String>();

    public Index() {
    }
    public Index(Commit lastCommit) {
        repo.putAll(lastCommit.getFilesTable());
        stage.putAll(lastCommit.getFilesTable());
    }
    public void toAdd(String fileName) {
        String hash = Persistor.saveBlob(fileName);
        stage.put(fileName, hash);
        calculateFilesToAdd();
    }
    public void clear() {
        filesToAdd.clear();
        stage.clear();
        stage.putAll(repo);
    }

    public void calculateFilesToAdd() {
        for (String file : stage.keySet()) {
            if (!repo.containsKey(file) || !repo.get(file).equals(stage.get(file))) {
                filesToAdd.put(file, stage.get(file));
            }
        }
    }

    public TreeMap<String, String> getFilesToAdd() {
        return filesToAdd;
    }

    public void status() {
        String result = "=== Branches ===" + "\n"
                + "*master" + "\n" + "\n"
                + "=== Staged Files ===" + "\n" + getFileNamesToAdd()
                + "=== Removed Files ===" + "\n" + "\n"
                + "=== Modifications Not Staged For Commit ===" + "\n" + "\n"
                + "=== Untracked Files ===" + "\n" + getUntrackedFileNames();
        System.out.println(result);
    }

    private String getFileNamesToAdd() {
        return String.join("\n", filesToAdd.keySet()) + "\n";
    }

    private String getUntrackedFileNames() {
        List<String> untrackedFiles = new ArrayList<String>();
        List<String> files = Utils.plainFilenamesIn(Persistor.CWD);
        for (String file : files) {
            if (!stage.containsKey(file) && !repo.containsKey(file)) {
                untrackedFiles.add(file);
            }
        }
        return String.join("\n", untrackedFiles) + "\n";
    }

    public boolean hasFile(String fileName) {
        return filesToAdd.containsKey(fileName);
    }
}
