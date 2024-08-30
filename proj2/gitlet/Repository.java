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

    public static void commit(String  message, String secondParent) {
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
        newCommit.setSecondParent(secondParent);
        String commitId = Persistor.saveCommit(newCommit);
        Persistor.setActiveCommitTo(commitId);
        index.clear();
        Persistor.saveIndex(index);
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
        Commit otherCommit = Persistor.getBranchHeadCommit(branchName);
        Commit splitCommit = findSplitCommit(activeCommit, otherCommit);
        if (splitCommit.getUid().equals(otherCommit.getUid())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (splitCommit.getUid().equals(activeCommit.getUid())) {
            System.out.println("Current branch fast-forwarded.");
            checkoutFilesFromBranchHead(branchName);
            System.exit(0);
        }
        Set<String> allFileNames = getFileNamesInMerge(splitCommit, activeCommit, otherCommit);
        for (String fileName : allFileNames) {
            if (same(fileName, activeCommit, splitCommit) && !otherCommit.hasFile(fileName)) {
                remove(fileName);
            }
            if (created(fileName, otherCommit, splitCommit)
                    || modif(fileName, otherCommit, splitCommit)) {
                checkoutFileFromCommit(fileName, otherCommit.getUid());
                add(fileName);
            }
            if (modifiedInDifferentWays(fileName, activeCommit, otherCommit, splitCommit)) {
                System.out.println("Encountered a merge conflict.");
                String fixedContent = fixConflict(fileName, activeCommit, otherCommit);
                WorkingDir.writeContentToFile(fileName, fixedContent);
                add(fileName);
            }
        }
        String message = "Merged " + branchName + " into " + Persistor.getActiveBranchName() + ".";
//        if (Objects.equals(branchName, "B2")) {
//            System.out.println("FOO" + getFullCommitHistory(activeCommit));
//        }
        commit(message, otherCommit.getUid());
    }

    private static boolean created(String fileName, Commit otherCommit, Commit splitCommit) {
        return !splitCommit.hasFile(fileName) && otherCommit.hasFile(fileName);
    }

    private static boolean same(String fileName, Commit c1, Commit c2) {
        return c1.hasFile(fileName) && c2.hasFile(fileName)
                && c1.getHash(fileName).equals(c2.getHash(fileName));
    }

    private static boolean modif(String fileName, Commit c1, Commit c2) {
        return c1.hasFile(fileName) && c2.hasFile(fileName)
                && !c1.getHash(fileName).equals(c2.getHash(fileName));
    }
    private static boolean modifiedInDifferentWays(String fileName,
                                                   Commit activeCommit,
                                                   Commit otherCommit,
                                                   Commit splitCommit) {
        return splitCommit.hasFile(fileName)
                && !same(fileName, activeCommit, splitCommit)
                && modif(fileName, otherCommit, splitCommit)
                || splitCommit.hasFile(fileName)
                && modif(fileName, activeCommit, splitCommit)
                && !otherCommit.hasFile(fileName);
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

    private static Set<String> getFileNamesInMerge(Commit c1, Commit c2, Commit c3) {
        Set<String> result = new HashSet<>();
        result.addAll(c1.getFileNames());
        result.addAll(c2.getFileNames());
        result.addAll(c3.getFileNames());
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

    private static List<Stack<Commit>> getFullCommitHistory(Commit c) {
//        System.out.println("BAZ" + c.getFirstParent());
//        System.out.println("BAZ" + c.getSecondParent());
        List<Stack<Commit>> history = new ArrayList<>();
        Stack<Commit> f = new Stack<>();
        f.push(c);
        history.add(f);
        Queue<Commit> queue = new LinkedList<>(getCommitParents(c));
        System.out.println("First history: " + history);
        while (!queue.isEmpty()) {
            Commit current = queue.poll();
            List<Commit> children = getCommitParents(current);
            // add children to queque
            if (!children.isEmpty()) {
                queue.addAll(children);
            }
//            System.out.println("BAR: " + queue);
            // push current to each stack in result
            for (Commit child : children) {
                for (Stack<Commit> stack : history) {
                    stack.push(child);
                }
            }
            //System.out.println("BOO " + history);
        }
        return history;
    }

    private static List<Commit> getCommitParents(Commit c) {
        List<Commit> result = new LinkedList<>();
        String firstParent = c.getFirstParent();
        String secondParent = c.getSecondParent();
        if (firstParent != null) {
            result.add(Persistor.readCommit(firstParent));
        }
        if (secondParent != null) {
            result.add(Persistor.readCommit(secondParent));
        }
        return result;
    }
}
