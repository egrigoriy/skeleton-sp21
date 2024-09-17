package gitlet.storage;

import gitlet.Index;
import gitlet.Store;
import gitlet.Utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  TOD: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Grigoriy Emiliyanov
 */
public class Commit implements StorageObject, Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String uid;
    private Date timestamp;
    private String firstParent = null;
    private String secondParent = null;
    private TreeMap<String, String> filesTable = new TreeMap<>();

    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        setUid();
    }

    public Commit(String message, Index index) {
        this.firstParent = Store.getActiveCommit().getUid();
        this.message = message;
        this.filesTable = new TreeMap<>(index.getFilesToCommit());
        this.timestamp = new Date();
        setUid();
    }

    @Override
    public String toString() {
        String result = "===" + "\n";
        result += "commit " + uid + "\n";
        if (secondParent != null) {
            result += "Merge: "
                    + firstParent.substring(0, 7) + " " + secondParent.substring(0, 7) + "\n";
        }
        result += "Date: " + formatTimestamp() + "\n";
        result += message + "\n";
        return  result;
    }

    private String formatTimestamp() {
        String pattern = "EEE MMM d HH:mm:ss yyyy Z";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return formatter.format(timestamp);
    }

    public String getUid() {
        return uid;
    }

    public String getFirstParent() {
        return firstParent;
    }

    public boolean hasFile(String fileName) {
        return filesTable.containsKey(fileName);
    }

    public String getFileHash(String fileName) {
        return filesTable.get(fileName);
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    private void setUid() {
        this.uid = Utils.sha1(
                this.getTimestamp().toString().getBytes(),
                this.getMessage().getBytes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Commit commit = (Commit) o;
        return Objects.equals(getUid(), commit.getUid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid());
    }

    public void setSecondParent(String commitId) {
        this.secondParent = commitId;
    }

    public String getSecondParent() {
        return secondParent;
    }

    public TreeMap<String, String> getFilesTable() {
        return filesTable;
    }

    public Set<String> getFileNames() {
        return filesTable.keySet();
    }

    public String getHash(String fileName) {
        return filesTable.get(fileName);
    }

    public boolean hasSameEntryFor(String fileName, Commit other) {
        return this.hasFile(fileName) && other.hasFile(fileName)
                && this.getHash(fileName).equals(other.getHash(fileName));
    }

    public boolean hasModified(String fileName, Commit other) {
        return this.hasFile(fileName) && other.hasFile(fileName)
                && !this.getHash(fileName).equals(other.getHash(fileName));
    }
    public boolean hasCreated(String fileName, Commit other) {
        return this.hasFile(fileName) && !other.hasFile(fileName);
    }
}
