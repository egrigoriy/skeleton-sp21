package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  Represents a Commit
 *
 *  @author Grigoriy Emiliyanov
 */
public class Commit implements StorageObject, Serializable {
    private final String message;
    private String uid;
    private final Date timestamp;
    private String firstParent = null;
    private String secondParent = null;
    private TreeMap<String, String> filesTable = new TreeMap<>();

    /**
     * Creates an initial commit.
     * The timestamp for this initial commit will be 00:00:00 UTC, Thursday, 1 January 1970
     */
    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        setUid();
    }

    /**
     * Creates a commit with given message based on files in the given index.
     * First parent is the current active commit and the second parent is null
     * @param message
     * @param index
     */
    public Commit(String message, Index index) {
        this(message, index, null);
    }

    /**
     * Creates a commit with given message based on files in the given index.
     * The created commit has active commit as first parent and the given second commit.
     * @param message
     * @param index
     * @param secondParent
     */
    public Commit(String message, Index index, String secondParent) {
        Branch currentBranch = new Branch();
        this.firstParent = currentBranch.getHeadCommitId();
        this.secondParent = secondParent;
        this.message = message;
        this.filesTable = new TreeMap<>(index.getFilesToCommit());
        this.timestamp = new Date();
        setUid();
    }

    /**
     * Returns the string representation of this commit
     * @return string
     */
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

    /**
     * Returns the time stamp in formatted as Pacific Standard Time
     * @return
     */
    private String formatTimestamp() {
        String pattern = "EEE MMM d HH:mm:ss yyyy Z";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return formatter.format(timestamp);
    }

    /**
     * Returns the uid of this commit
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Returns the uid of the first parent of this commit
     * @return uid
     */
    public String getFirstParent() {
        return firstParent;
    }

    /**
     * Returns the hash corresponding to a file with given name from this commit
     * @param fileName
     * @return hash
     */
    public String getFileHash(String fileName) {
        return filesTable.get(fileName);
    }

    /**
     * Returns the message associated with this commit
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Return true if a file with given name is contained in this commit
     * @param fileName
     * @return boolean
     */
    public boolean hasFile(String fileName) {
        return filesTable.containsKey(fileName);
    }

    /**
     * Sets the uid for this commit using its time stamp and message
     */
    private void setUid() {
        this.uid = Utils.sha1(
                this.timestamp.toString().getBytes(),
                this.message.getBytes());
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

    /**
     * Returns the hash of the second parent of this commit
     * @return uid
     */
    public String getSecondParent() {
        return secondParent;
    }

    /**
     * Returns the files and corresponding hashes contained in this commit
     * @return files and their hashes
     */
    public TreeMap<String, String> getFilesTable() {
        return filesTable;
    }

    /**
     * Returns the names of the files contained in this commit
     * @return a set of file names
     */
    public Set<String> getFileNames() {
        return filesTable.keySet();
    }

    /**
     * Returns the hash of a file with given name contained in this commit
     * @param fileName
     * @return hash
     */
    public String getHash(String fileName) {
        return filesTable.get(fileName);
    }

    /**
     * Returns true if this commit has same file entry for given file name as given commit,
     * otherwise false
     * @param fileName
     * @param other
     * @return boolean
     */
    public boolean hasSameEntryFor(String fileName, Commit other) {
        return this.hasFile(fileName) && other.hasFile(fileName)
                && this.getHash(fileName).equals(other.getHash(fileName));
    }

    /**
     * Returns true if this commit has file hash different from one in the given commit,
     * otherwise false
     * @param fileName
     * @param other
     * @return boolean
     */
    public boolean hasModified(String fileName, Commit other) {
        return this.hasFile(fileName) && other.hasFile(fileName)
                && !this.getHash(fileName).equals(other.getHash(fileName));
    }

    /**
     * Returns true if this commit has file missing in the given commit, otherwise false
     * @param fileName
     * @param other
     * @return boolean
     */
    public boolean hasCreated(String fileName, Commit other) {
        return this.hasFile(fileName) && !other.hasFile(fileName);
    }

    /**
     * Returns true if a file with given name was removed from this commit,
     * but present in the given one. Returns false otherwise.
     * @param fileName
     * @param other
     * @return
     */
    public boolean hasRemoved(String fileName, Commit other) {
        return !this.hasFile(fileName) && other.hasFile(fileName);
    }
}
