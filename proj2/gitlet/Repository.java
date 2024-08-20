package gitlet;

/** Represents a gitlet repository.
 *  TOD: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Grigoriy Emiliyanov
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    public static void init() {
        if (!Persistor.isRepositoryInitialized()) {
            Persistor.buildInfrastructure();
        } else {
            String m = "A Gitlet version-control system already exists in the current directory.";
            System.out.println(m);
            System.exit(0);
        }
        Commit initialCommit = new Commit();
        String hash = Persistor.saveCommit(initialCommit);
        Persistor.writeToHead("master");
        Persistor.writeHashOfHead(hash);
    }

    public static void add(String fileName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (!Persistor.fileExists(fileName)) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        index.add(fileName);
        Persistor.saveIndex(index);
    }

    public static void status() {
        Index index = Persistor.readIndex();
        index.status();
    }

    public static void log() {
        Commit current = Persistor.readLastCommit();
        while (current != null) {
            System.out.println(current);
            current = Persistor.readCommit(current.getFirstParent());
        }
    }

    public static void commit(String message) {
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        if (index.nothingToAddOrRemove()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        String lastCommitHash = Persistor.readHashOfHead();
        Commit commit = new Commit(message, index, lastCommitHash);
        String hash = Persistor.saveCommit(commit);
        index.clear();
        Persistor.saveIndex(index);
        Persistor.writeHashOfHead(hash);
    }

    public static void checkoutFileFromLastCommit(String fileName) {
        Commit lastCommit = Persistor.readLastCommit();
        checkoutFileFromCommit(fileName, lastCommit.getUid());
    }

    public static void checkoutFileFromCommit(String fileName, String commitID) {
        Commit commit = Persistor.readCommit(commitID);
        if (!commit.hasFile(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        // else
        // get blob sha of file
        String hash = commit.getFileHash(fileName);
        // read blob
        String content = Persistor.readTrackedFileContent(hash);
        // write blob content to filename
        Persistor.writeContentToCWDFile(fileName, content);
    }

    public static void checkoutFilesFromBranchHead(String branchName) {
        // NOT IMPLEMENTED YET
    }

    public static void remove(String fileName) {
        Index index = Persistor.readIndex();
        if (!index.fileInStageOrRepo(fileName)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        index.remove(fileName);
        Persistor.saveIndex(index);
    }

    public static void branch(String branchName) {
        if (Persistor.branchExists(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Persistor.createBranch(branchName);
    }

    public static void removeBranch(String branchName) {
        if (!Persistor.branchExists(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (Persistor.getBranchNameFromHead().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        Persistor.removeBranch(branchName);
    }
}
