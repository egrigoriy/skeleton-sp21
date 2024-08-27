package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static gitlet.Utils.*;

public class Persistor {
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File REF_HEADS_DIR = join(REFS_DIR, "heads");
    public static final File HEAD = Utils.join(GITLET_DIR, "HEAD");

    public static final File INDEX = Utils.join(GITLET_DIR, "index");

    public static String saveCommit(Commit commit) {
        String hash = Utils.sha1(
                commit.getTimestamp().toString().getBytes(),
                commit.getMessage().getBytes());
        commit.setUid(hash);
        File savePath = Utils.join(COMMITS_DIR, hash);
        Utils.writeObject(savePath, commit);
        return hash;
    }

    public static Commit readCommit(String commitID) {
        if (commitID == null) {
            return null;
        }
        File readPath = getCommitPath(commitID);
        if (!readPath.exists()) {
            return null;
        }
        return Utils.readObject(readPath, Commit.class);
    }

    private static File getCommitPath(String commitID) {
        return Utils.join(COMMITS_DIR, getCommitFileName(commitID));
    }

    private static String getCommitFileName(String commitID) {
        if (commitID.length() == UID_LENGTH) {
            return commitID;
        }
        return fullCommitId(commitID);
    }

    private static String fullCommitId(String commitID) {
        for (String fileName : Utils.plainFilenamesIn(COMMITS_DIR)) {
            if (fileName.contains(commitID)) {
                return fileName;
            }
        }
        return "";
    }

    public static String saveBlob(String fileName) {
        byte[] fileContent = Utils.readContents(Utils.join(Persistor.CWD, fileName));
        String hash = Utils.sha1(fileContent);
        File savePath = Utils.join(BLOBS_DIR, hash);
        if (!savePath.exists()) {
            Utils.writeContents(savePath, fileContent);
        }
        return hash;
    }

    public static String readBlob(String hash) {
        File readPath = Utils.join(BLOBS_DIR, hash);
        return Utils.readContentsAsString(readPath);
    }

    public static void saveIndex(Index index) {
        Utils.writeObject(INDEX, index);
    }

    public static Index readIndex() {
        if (INDEX.exists()) {
            return Utils.readObject(INDEX, Index.class);
        } else {
            return new Index();
        }
    }

    public static boolean isRepositoryInitialized() {
        return GITLET_DIR.exists();
    }

    public static void buildInfrastructure() {
        GITLET_DIR.mkdir();
        REFS_DIR.mkdir();
        REF_HEADS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
    }

    public static List<Commit> getAllCommits() {
        List<Commit> result = new ArrayList<>();
        for (String fileName : plainFilenamesIn(COMMITS_DIR)) {
            Commit commit = readCommit(fileName);
            result.add(commit);
        }
        return result;
    }

    public static void writeContentToCWDFile(String fileName, String content) {
        Utils.writeContents(Utils.join(CWD, fileName), content);
    }

    public static boolean fileExists(String fileName) {
        return Utils.join(CWD, fileName).exists();
    }



    public static void removeCWDFile(String fileName) {
        File filePath = Utils.join(CWD, fileName);
        Utils.restrictedDelete(filePath);
    }

    public static void pointHEADTo(String branchName) {
        Utils.writeContents(HEAD, "ref: refs/heads/" + branchName);
    }
    public static void writeHashOfHead(String hash) {
        String branchName = getActiveBranchName();
        writeHashOfBranchHead(branchName, hash);
    }

    public static String readHashOfHead() {
        String branchName = getActiveBranchName();
        String hash = readHashOfBranchHead(branchName);
        return hash;
    }

    public static String readHashOfBranchHead(String branchName) {
        File branchHeadFile = Utils.join(REF_HEADS_DIR, branchName);
        return Utils.readContentsAsString(branchHeadFile);
    }

    public static void writeHashOfBranchHead(String branchName, String hash) {
        File branchHeadFile = Utils.join(REF_HEADS_DIR, branchName);
        Utils.writeContents(branchHeadFile, hash);
    }



    public static String getActiveBranchName() {
        String headContent = Utils.readContentsAsString(HEAD);
        String branchName = headContent.substring(headContent.lastIndexOf("/") + 1);
        return branchName;
    }


    public static boolean branchExists(String branchName) {
        return Utils.join(REF_HEADS_DIR, branchName).exists();
    }

    public static void createBranch(String branchName) {
        String currentCommitHash = readHashOfHead();
        writeHashOfBranchHead(branchName, currentCommitHash);
    }

    public static void removeBranch(String branchName) {
        File filePath = Utils.join(REF_HEADS_DIR, branchName);
        filePath.delete();
    }

    public static List<String> readAllBranchNames() {
        return Utils.plainFilenamesIn(REF_HEADS_DIR);
    }

    public static String getFileHash(String fileName) {
        byte[] fileContent = Utils.readContents(Utils.join(Persistor.CWD, fileName));
        String hash = Utils.sha1(fileContent);
        return hash;
    }

    public static Commit getActiveCommit() {
        return getBranchHeadCommit(getActiveBranchName());
    }

    public static Commit getBranchHeadCommit(String branchName) {
        String hash = readHashOfBranchHead(branchName);
        return readCommit(hash);
    }

    public static void writeCWDFiles(TreeMap<String, String> files) {
        for (String fileName : files.keySet()) {
            String sha = files.get(fileName);
            String content = Persistor.readBlob(sha);
            Persistor.writeContentToCWDFile(fileName, content);
        }
    }

    public static void foo(String branchName) {
        // get currently tracked files
        Set<String> activeCommitFiles = Persistor.getActiveCommit().getFilesTable().keySet();

        // Takes all files in the commit at the head of the given branch,
        TreeMap<String, String> checkedOutBranchFiles = Persistor.getBranchHeadCommit(branchName).getFilesTable();
        // and puts them in the working directory, overwriting the versions of the files
        // that are already there if they exist.
        Persistor.writeCWDFiles(checkedOutBranchFiles);
        // Any files that are tracked in the current branch but are not
        // present in the checked-out branch are deleted.
        for (String fileName : activeCommitFiles) {
            if (!checkedOutBranchFiles.keySet().contains(fileName)) {
                Utils.restrictedDelete(Utils.join(Persistor.CWD, fileName));
            }
        }
    }


}
