package gitlet.storage;
import java.io.Serializable;
import gitlet.*;

public class Blob implements StorageObject, Serializable {
    byte[] content;

    public Blob(byte[] content) {
        this.content = content;
    }

    @Override
    public String getUid() {
        return Utils.sha1(content);
    }
}
