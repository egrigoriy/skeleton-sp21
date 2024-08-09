package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");

    public void init() {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
            BRANCHES_DIR.mkdir();
            File master = join(BRANCHES_DIR, "master");
            Utils.writeContents(master, "hash of initial commit");

        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
    }

    public void add() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    public void status() {
        StringBuilder result = new StringBuilder();
        result.append("=== Branches ===" + "\n");
        result.append("*master" + "\n");
        result.append("\n");
        result.append("=== Staged Files ===" + "\n");
        result.append("\n");
        result.append("=== Removed Files ===" + "\n");
        result.append("\n");
        result.append("=== Modifications Not Staged For Commit ===" + "\n");
        result.append("\n");
        result.append("=== Untracked Files ===" + "\n");
        result.append("\n");

        System.out.println(result);

    }
    /* TODO: fill in the rest of this class. */
}
