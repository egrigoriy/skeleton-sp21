package gitlet;

import java.util.*;

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
        Persistor.pointHEADTo("master");
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
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        index.status();
    }

    public static void log() {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        Commit lastCommit = Persistor.readCommit(Persistor.readHashOfHead());
        log(lastCommit);
    }

    private static void log(Commit commit) {
        Commit p = commit;
        while (p != null) {
            System.out.println(p);
            p = Persistor.readCommit(p.getFirstParent());
        }
    }

    public static void globalLog() {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        List<Commit> allCommits = Persistor.getAllCommits();
        for (Commit commit : allCommits) {
            log(commit);
        }
    }
    public static void commit(String message) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
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
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        String hash = Persistor.readHashOfHead();
        checkoutFileFromCommit(fileName, hash);
    }

    public static void checkoutFileFromCommit(String fileName, String commitID) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        Commit commit = Persistor.readCommit(commitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (!commit.hasFile(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        String hash = commit.getFileHash(fileName);
        String content = Persistor.readBlob(hash);
        Persistor.writeContentToCWDFile(fileName, content);
    }

    public static void checkoutFilesFromBranchHead(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (!Persistor.branchExists(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            String message = "There is an untracked file in the way; "
                    + "delete it, or add and commit it first.";
            System.out.println(message);
            System.exit(0);
        }
        // get currently tracked files
        String currentHash = Persistor.readHashOfHead();
        Commit currentCommit = Persistor.readCommit(currentHash);
        Set<String> currentlyTrackedFiles;
        if (currentCommit.getFilesTable() != null) {
            currentlyTrackedFiles = currentCommit.getFilesTable().keySet();
        } else {
            currentlyTrackedFiles = new HashSet<>();
        }

        // Takes all files in the commit at the head of the given branch,
        String hash = Persistor.readHashOfBranchHead(branchName);
        Commit checkedOutHeadCommit = Persistor.readCommit(hash);
        TreeMap<String, String> checkedOutBranchFiles;
        if (checkedOutHeadCommit.getFilesTable() != null) {
            checkedOutBranchFiles = checkedOutHeadCommit.getFilesTable();
        } else {
            checkedOutBranchFiles = new TreeMap<>();
        }
            // and puts them in the working directory, overwriting the versions of the files
            // that are already there if they exist.

        for (String f : Utils.plainFilenamesIn(Persistor.CWD)) {
            Utils.restrictedDelete(f);
        }
        if (!Utils.plainFilenamesIn(Persistor.CWD).isEmpty()) {
            throw new GitletException("NOT EMPTY");
        }
        for (String fileName : checkedOutBranchFiles.keySet()) {
            // get blob sha of file
            String sha = checkedOutHeadCommit.getFileHash(fileName);
            // read blob
            String content = Persistor.readBlob(sha);
            // write blob content to filename
            Persistor.writeContentToCWDFile(fileName, content);
        }
        // Any files that are tracked in the current branch but are not
        // present in the checked-out branch are deleted.
        for (String fileName : currentlyTrackedFiles) {
            if (!checkedOutBranchFiles.keySet().contains(fileName)) {
                //System.out.println("FOO: " + fileName);
                Utils.restrictedDelete(Utils.join(Persistor.CWD, fileName));
            }
        }

        // The staging area is cleared, unless the checked-out branch is the current branch
        index.clear();
        index.setRepo(checkedOutBranchFiles);
        Persistor.saveIndex(index);

        // Also, at the end of this command,
        // the given branch will now be considered the current branch (HEAD).
        Persistor.pointHEADTo(branchName);
    }

    public static void remove(String fileName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        if (index.isUntracked(fileName)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        index.remove(fileName);
        Persistor.saveIndex(index);
    }

    public static void branch(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (Persistor.branchExists(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Persistor.createBranch(branchName);
    }

    public static void removeBranch(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (!Persistor.branchExists(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        Persistor.removeBranch(branchName);
    }


    public static void find(String message) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        List<String> foundCommits = new ArrayList<>();
        List<Commit> allCommits = Persistor.getAllCommits();
        for (Commit commit : allCommits) {
            if (commit.getMessage().equals(message)) {
                foundCommits.add(commit.getUid());
            }
        }

        if (foundCommits.isEmpty()) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }

        System.out.println(String.join("\n", foundCommits));
    }

    public static void reset(String commitID) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        Commit commit = Persistor.readCommit(commitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            String message = "There is an untracked file in the way; "
                    + "delete it, or add and commit it first.";
            System.out.println(message);
            System.exit(0);
        }
        // get currently tracked files
        String currentHash = Persistor.readHashOfHead();
        Commit currentCommit = Persistor.readCommit(currentHash);
        Set<String> currentlyTrackedFiles;
        if (currentCommit.getFilesTable() != null) {
            currentlyTrackedFiles = currentCommit.getFilesTable().keySet();
        } else {
            currentlyTrackedFiles = new HashSet<>();
        }

        Commit checkedOutCommit = Persistor.readCommit(commitID);
        TreeMap<String, String> checkedOutBranchFiles;
        if (checkedOutCommit.getFilesTable() != null) {
            checkedOutBranchFiles = checkedOutCommit.getFilesTable();
        } else {
            checkedOutBranchFiles = new TreeMap<>();
        }
        // and puts them in the working directory, overwriting the versions of the files
        // that are already there if they exist.

        for (String f : Utils.plainFilenamesIn(Persistor.CWD)) {
            Utils.restrictedDelete(f);
        }
        if (!Utils.plainFilenamesIn(Persistor.CWD).isEmpty()) {
            throw new GitletException("NOT EMPTY");
        }
        for (String fileName : checkedOutBranchFiles.keySet()) {
            // get blob sha of file
            String sha = checkedOutCommit.getFileHash(fileName);
            // read blob
            String content = Persistor.readBlob(sha);
            // write blob content to filename
            Persistor.writeContentToCWDFile(fileName, content);
        }
        // Any files that are tracked in the current branch but are not
        // present in the checked-out branch are deleted.
        for (String fileName : currentlyTrackedFiles) {
            if (!checkedOutBranchFiles.keySet().contains(fileName)) {
                //System.out.println("FOO: " + fileName);
                Utils.restrictedDelete(Utils.join(Persistor.CWD, fileName));
            }
        }

        // The staging area is cleared, unless the checked-out branch is the current branch
        index.clear();
        index.setRepo(checkedOutBranchFiles);
        Persistor.saveIndex(index);

        // Also, at the end of this command,
        // the given branch will now be considered the current branch (HEAD).
        Persistor.writeHashOfHead(commitID);
    }
}
