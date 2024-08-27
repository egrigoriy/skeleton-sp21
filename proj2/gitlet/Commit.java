package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeMap;

/** Represents a gitlet commit object.
 *  TOD: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Grigoriy Emiliyanov
 */
public class Commit implements Serializable {
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
    private TreeMap<String, String> filesTable = null;

    public Commit() {
        message = "initial commit";
        timestamp = new Date(0);
    }

    public Commit(String message, Index index) {
        this.firstParent = Persistor.getActiveCommit().getUid();
        this.message = message;
        this.filesTable = new TreeMap<>(index.getFilesToCommit());
        timestamp = new Date();
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

    public void setUid(String uid) {
        this.uid = uid;
    }

    public TreeMap<String, String> getFilesTable() {
        return filesTable;
    }
}
