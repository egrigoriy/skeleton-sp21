package gitlet;

import java.io.File;
import java.util.concurrent.TimeUnit;

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
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File REF_HEADS_DIR = join(REFS_DIR, "heads");
    public static final File head_master = Utils.join(REF_HEADS_DIR, "master");
    public static final File INDEX = Utils.join(GITLET_DIR, "index");

    public void init() {
        // System.out.println(GITLET_DIR.exists());
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
            OBJECTS_DIR.mkdir();
            REFS_DIR.mkdir();
            REF_HEADS_DIR.mkdir();
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        Commit init = new Commit();
        String uid = init.getUid();
        init.save();
        Utils.writeContents(head_master, uid);
//        try {
//            TimeUnit.MILLISECONDS.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Commit com = Persistor.readCommit(uid);
        //System.out.println(com);
    }

    public void add(String fileName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Index index;
        if (INDEX.exists()) {
            // read INDEX
            index = Persistor.readIndex();
        } else {

            // create INDEX
            index = new Index();
        }
        // update index
        index.toAdd(fileName);
        // save index
        index.save();
    }

    public void status() {

    }

    public void log() {
        String head_commit_uid = Utils.readContentsAsString(head_master);
        Commit head_commit = Persistor.readCommit(head_commit_uid);
        Commit current = head_commit;
        while (current != null) {
            System.out.println(current);
            current = Persistor.readCommit(current.getFirstParent());
        }
    }
    /* TODO: fill in the rest of this class. */
}
