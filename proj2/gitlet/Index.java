package gitlet;

import java.io.Serializable;
import java.util.TreeMap;

public class Index implements Serializable {
    private TreeMap<String, String> filesToAdd = new TreeMap<String, String>();

    public void toAdd(String fileName) {
        byte[] fileContent = Utils.readContents(Utils.join(Persistor.CWD, fileName));
        String blobSHA1 = Utils.sha1(fileContent);
        filesToAdd.put(fileName, blobSHA1);
        // save blob
        Persistor.saveBlob(blobSHA1, fileContent);
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

}
