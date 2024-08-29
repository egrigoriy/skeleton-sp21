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
        if (Persistor.isRepositoryInitialized()) {
            String m = "A Gitlet version-control system already exists in the current directory.";
            System.out.println(m);
            System.exit(0);
        } else {
            Persistor.buildInfrastructure();
        }
        Commit initialCommit = new Commit();
        String commitId = Persistor.saveCommit(initialCommit);
        Persistor.setActiveBranchTo("master");
        Persistor.setActiveCommitTo(commitId);
    }

    public static void add(String fileName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (!WorkingDir.fileExists(fileName)) {
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
        Commit p = Persistor.getActiveCommit();
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
            System.out.println(commit);
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
        Commit newCommit = new Commit(message, index);
        String commitId = Persistor.saveCommit(newCommit);
        Persistor.setActiveCommitTo(commitId);
        index.clear();
        Persistor.saveIndex(index);
    }

    public static void checkoutFileFromActiveCommit(String fileName) {
        String commitId = Persistor.getActiveCommitId();
        checkoutFileFromCommit(fileName, commitId);
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
        Persistor.checkoutFileFromCommit(fileName, commit);
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
        Commit branchHeadCommit = Persistor.getBranchHeadCommit(branchName);
        Persistor.checkoutFilesFromCommit(branchHeadCommit);
        index.clear();
        index.setRepo(branchHeadCommit.getFilesTable());
        Persistor.saveIndex(index);
        Persistor.setActiveBranchTo(branchName);
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
        Persistor.checkoutFilesFromCommit(commit);
        index.clear();
        index.setRepo(commit.getFilesTable());
        Persistor.saveIndex(index);
        // Also moves the current branch’s head to that commit node.
        Persistor.setActiveCommitTo(commitID);
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

    public static void merge(String branchName) {
        if (!Persistor.isRepositoryInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        if (!Persistor.branchExists(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (Persistor.getActiveBranchName().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        Index index = Persistor.readIndex();
        if (index.untrackedFileInTheWay()) {
            String message = "There is an untracked file in the way; "
                    + "delete it, or add and commit it first.";
            System.out.println(message);
            System.exit(0);
        }
        if (!index.nothingToAddOrRemove()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        Commit activeCommit = Persistor.getActiveCommit();
        Commit otherBranchHeadCommit = Persistor.getBranchHeadCommit(branchName);
        Commit splitCommit = findSplitCommit(activeCommit, otherBranchHeadCommit);
        Set<String> allFileNames = getFileNamesInMerge(splitCommit,
                activeCommit,
                otherBranchHeadCommit
        );
        for (String fileName : allFileNames) {
            if (same(fileName, activeCommit, splitCommit)
                    && !otherBranchHeadCommit.hasFile(fileName)) {
                remove(fileName);
            }
            if (created(fileName, otherBranchHeadCommit, splitCommit)
                || modif(fileName, otherBranchHeadCommit, splitCommit)) {
                checkoutFileFromCommit(fileName, otherBranchHeadCommit.getUid());
                add(fileName);
            }
            if (splitCommit.hasFile(fileName)
                && !same(fileName, activeCommit, splitCommit)
                && modif(fileName, otherBranchHeadCommit, splitCommit)) {
                System.out.println("Encountered a merge conflict.");
                String fixedContent = fixConflict(fileName, activeCommit, otherBranchHeadCommit);
                WorkingDir.writeContentToFile(fileName, fixedContent);
                add(fileName);
            }
            if (splitCommit.hasFile(fileName)
                    && modif(fileName, activeCommit, splitCommit)
                    && !otherBranchHeadCommit.hasFile(fileName)) {
                System.out.println("Encountered a merge conflict.");
                String fixedContent = fixConflict(fileName, activeCommit, otherBranchHeadCommit);
                WorkingDir.writeContentToFile(fileName, fixedContent);
                add(fileName);
            }
        }
//        status();
        commit("Merged " + branchName + " into " + Persistor.getActiveBranchName() + ".");
    }

    private static boolean created(String fileName, Commit otherCommit, Commit splitCommit) {
        return !splitCommit.hasFile(fileName) && otherCommit.hasFile(fileName);
    }


    private static String fixConflict(String fileName, Commit activeCommit, Commit otherCommit) {
        String result = "<<<<<<< HEAD" + "\n";
        if (activeCommit.hasFile(fileName)) {
            result += Persistor.readBlob(activeCommit.getFileHash(fileName));
        }
        result += "=======" + "\n";
        if (otherCommit.hasFile(fileName)) {
            result += Persistor.readBlob(otherCommit.getFileHash(fileName));
        }
        result += ">>>>>>>" + "\n";
        return result;
    }


    private static boolean same(String fileName, Commit c1, Commit c2) {
        return c1.getFilesTable().containsKey(fileName) && c2.getFilesTable().containsKey(fileName)
                && c1.getFilesTable().get(fileName).equals(c2.getFilesTable().get(fileName));
    }

    private static boolean modif(String fileName, Commit c1, Commit c2) {
        return c1.getFilesTable().containsKey(fileName) && c2.getFilesTable().containsKey(fileName)
                && !c1.getFilesTable().get(fileName).equals(c2.getFilesTable().get(fileName));
    }

    private static Set<String> getFileNamesInMerge(Commit c1, Commit c2, Commit c3) {
        Set<String> result = new HashSet<>();
        result.addAll(c1.getFilesTable().keySet());
        result.addAll(c2.getFilesTable().keySet());
        result.addAll(c3.getFilesTable().keySet());
        return result;
    }


    private static Commit findSplitCommit(Commit c1, Commit c2) {
        Stack<Commit> c1History = getCommitHistory(c1);
        Stack<Commit> c2History = getCommitHistory(c2);
        Commit splitCommit = c1History.peek();
        while (!c1History.isEmpty() && !c2History.isEmpty()) {
            Commit c11 = c1History.pop();
            Commit c22 = c2History.pop();
            if (c11.getUid().equals(c22.getUid())) {
                splitCommit = c11;
            } else {
                return splitCommit;
            }
        }
        return splitCommit;
    }

    private static Stack<Commit> getCommitHistory(Commit c1) {
        Stack<Commit> history = new Stack<>();
        Commit p = c1;
        while (p != null) {
            history.push(p);
            p = Persistor.readCommit(p.getFirstParent());
        }
        return history;
    }

}
