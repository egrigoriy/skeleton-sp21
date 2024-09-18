package gitlet;

public class Blob implements StorageObject {
    byte[] content;

    public Blob(byte[] content) {
        this.content = content;
    }

    @Override
    public String getUid() {
        return Utils.sha1(content);
    }
}
