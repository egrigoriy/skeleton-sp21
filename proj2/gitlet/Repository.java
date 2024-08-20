package gitlet;

import java.util.List;
import java.util.Set;

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
        String hash = Persistor.readHashOfHead();
        log(hash);
    }

    private static void log(String hash) {
        Commit current = Persistor.readCommit(hash);
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
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
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
        if (!Persistor.branchExists(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (Persistor.getBranchNameFromHead().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        // get currently tracked files
        String currentHash = Persistor.readHashOfHead();
        Commit currentCommit = Persistor.readCommit(currentHash);
        Set<String> currentlyTrackedFiles = currentCommit.getFilesTable().keySet();
        // Takes all files in the commit at the head of the given branch,
        String hash = Persistor.readHashOfBranchHead(branchName);
        Commit newBranchHeadCommit = Persistor.readCommit(hash);
        if (newBranchHeadCommit.getFilesTable() != null) {
            // and puts them in the working directory, overwriting the versions of the files
            // that are already there if they exist.
            Set<String> newBranchFiles = newBranchHeadCommit.getFilesTable().keySet();
            for (String fileName : newBranchFiles) {
                // get blob sha of file
                String sha = newBranchHeadCommit.getFileHash(fileName);
                // read blob
                String content = Persistor.readTrackedFileContent(sha);
                // write blob content to filename
                Persistor.writeContentToCWDFile(fileName, content);
            }
            // Any files that are tracked in the current branch but are not present in the checked-out branch are deleted.
            for (String fileName : currentlyTrackedFiles) {
                if (!newBranchFiles.contains(fileName)) {
                    Utils.restrictedDelete(Utils.join(Persistor.CWD, fileName));
                }
            }
            // The staging area is cleared, unless the checked-out branch is the current branch
            Index index = Persistor.readIndex();
            index.clear();
            Persistor.saveIndex(index);
        }


        // Also, at the end of this command,
        // the given branch will now be considered the current branch (HEAD).
        Persistor.writeToHead(branchName);
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

    public static void globalLog() {
        List<String> branchNames = Persistor.readAllBranchNames();
        for (String branchName : branchNames) {
            String hash = Persistor.readHashOfBranchHead(branchName);
            log(hash);
        }
    }

}
