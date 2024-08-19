package gitlet;

import java.io.Serializable;
import java.util.TreeMap;

public class Index implements Serializable {
    private TreeMap<String, String> filesToAdd = new TreeMap<String, String>();

    public void toAdd(String fileName) {
        String hash = Persistor.saveBlob(fileName);
        filesToAdd.put(fileName, hash);
    }
    public void clear() {
        filesToAdd.clear();
    }

    public TreeMap<String, String> getFilesToAdd() {
        return filesToAdd;
    }

    public void save() {
        Persistor.saveIndex(this);
    }

    public void status() {
        String result = "=== Branches ===" + "\n"
                + "*master" + "\n" + "\n"
                + "=== Staged Files ===" + "\n" + getFileNamesToAdd()
                + "=== Removed Files ===" + "\n" + "\n"
                + "=== Modifications Not Staged For Commit ===" + "\n" + "\n"
                + "=== Untracked Files ===" + "\n" + "\n";
        System.out.println(result);
    }

    private String getFileNamesToAdd() {
        return String.join("\n", filesToAdd.keySet()) + "\n";
    }

    public boolean hasFile(String fileName) {
        return filesToAdd.containsKey(fileName);
    }
}
