package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Date; // TODO: You'll likely use this in this class
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
    private TreeMap<String, String> fileBlobPairs = null;

    /* TODO: fill in the rest of this class. */
    public Commit () {
        message = "initial commit";
        timestamp = new Date(0);
        uid = getUID();
    }

    private String getUID() {
        return Utils.sha1(timestamp.toString().getBytes(), message.getBytes());
    }

    public void save() {
        Persistor.saveCommit(this);
    }

    @Override
    public String toString() {
        String result = "===" + "\n";
        result += "commit: " + uid + "\n";
        if (secondParent != null) {
            result += "Merge: " + firstParent.substring(0, 7) + " " + secondParent.substring(0, 7) + "\n";
        }
        result += "Date: " + timestamp + "\n";
        result += message + "\n";
        result += "\n";
        return  result;
    }

    public String getUid() {
        return uid;
    }

    public String getFirstParent() {
        return firstParent;
    }
}
