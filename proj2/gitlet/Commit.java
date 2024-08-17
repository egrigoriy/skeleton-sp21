package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeMap;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
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

    /* TODO: fill in the rest of this class. */
    public Commit () {
        message = "initial commit";
        timestamp = new Date(0);
        uid = calculateUID();
    }

    public Commit(String message, TreeMap<String, String> filesToAdd, String firstParent) {
        this.firstParent = firstParent;
        this.message = message;
        updateFilesTable(filesToAdd);
        timestamp = new Date();
        uid = calculateUID();
    }

    private void updateFilesTable(TreeMap<String, String> filesToAdd) {
        if (filesToAdd != null) {
            filesTable = new TreeMap<String, String>();
            filesTable.putAll(filesToAdd);
        }
    }

    private String calculateUID() {
        return Utils.sha1(timestamp.toString().getBytes(), message.getBytes());
    }

    @Override
    public String toString() {
        String result = "===" + "\n";
        result += "commit " + uid + "\n";
        if (secondParent != null) {
            result += "Merge: " + firstParent.substring(0, 7) + " " + secondParent.substring(0, 7) + "\n";
        }
        result += "Date: " + formatTimestamp() + "\n";
        result += message + "\n";
        return  result;
    }

    private String formatTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return formatter.format(timestamp);
    }

    public String getUid() {
        return uid;
    }

    public String getFirstParent() {
        return firstParent;
    }
}
