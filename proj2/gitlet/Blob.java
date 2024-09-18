package gitlet;

/**
 * Represents a blob with content and uid
 *
 *  @author Grigoriy Emiliyanov
 */
public class Blob implements StorageObject {
    byte[] content;
    String uid;

    public Blob(byte[] content) {
        this.content = content;
        this.uid = Utils.sha1(content);
    }

    /**
     * Returns this blob uid
     * @return uid
     */
    @Override
    public String getUid() {
        return uid;
    }
}
