package gitlet;

import java.io.File;
import java.util.TreeMap;

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

    public void init() {
        if (!Persistor.isRepositoryInitialized()) {
            Persistor.buildInfrastructure();
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        Commit initialCommit = new Commit();
        String uid = initialCommit.getUid();
        Persistor.saveCommit(initialCommit);
        Persistor.saveMaster(uid);
    }

    public void add(String fileName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        // update index
        index.toAdd(fileName);
        // save index
        index.save();
    }

    public void status() {

    }

    public void log() {
        String head_commit_uid = Persistor.readMaster();
        Commit head_commit = Persistor.readCommit(head_commit_uid);
        Commit current = head_commit;
        while (current != null) {
            System.out.println(current);
            current = Persistor.readCommit(current.getFirstParent());
        }
    }

    public void commit(String message) {
        // read index
        Index index = Persistor.readIndex();
        // get files to add
        TreeMap<String, String> filesToAdd = index.getFilesToAdd();
        String firstParent = Persistor.readMaster();
        // create commit with msg, files to add
        Commit commit = new Commit(message, filesToAdd, firstParent);
        // save commit
        Persistor.saveCommit(commit);
        // clean index
        index.clear();
        // save index
        index.save();
        // update master head
        Persistor.saveMaster(commit.getUid());
    }
    /* TODO: fill in the rest of this class. */
}
