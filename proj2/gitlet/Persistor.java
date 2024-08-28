package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static gitlet.Utils.*;

public class Persistor {
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(WorkingDir.CWD, ".gitlet");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File REF_HEADS_DIR = join(REFS_DIR, "heads");
    public static final File HEAD = Utils.join(GITLET_DIR, "HEAD");

    public static final File INDEX = Utils.join(GITLET_DIR, "index");

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

    public static String saveCommit(Commit commit) {
        String commitId = Utils.sha1(
                commit.getTimestamp().toString().getBytes(),
                commit.getMessage().getBytes());
        commit.setUid(commitId);
        File savePath = Utils.join(COMMITS_DIR, commitId);
        Utils.writeObject(savePath, commit);
        return commitId;
    }

    public static Commit readCommit(String commitId) {
        if (commitId == null) {
            return null;
        }
        File readPath = getCommitPath(commitId);
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
        byte[] fileContent = WorkingDir.readFileContent(fileName);
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

    public static void setActiveCommitTo(String commitId) {
        String activeBranchName = getActiveBranchName();
        setBranchHeadCommit(activeBranchName, commitId);
    }

    private static void setBranchHeadCommit(String branchName, String commitId) {
        File branchHeadFile = Utils.join(REF_HEADS_DIR, branchName);
        Utils.writeContents(branchHeadFile, commitId);
    }

    public static Commit getActiveCommit() {
        return getBranchHeadCommit(getActiveBranchName());
    }

    public static String getActiveCommitId() {
        return getBranchHeadCommitId(getActiveBranchName());
    }

    public static List<Commit> getAllCommits() {
        List<Commit> result = new ArrayList<>();
        for (String fileName : plainFilenamesIn(COMMITS_DIR)) {
            Commit commit = readCommit(fileName);
            result.add(commit);
        }
        return result;
    }

    public static void setActiveBranchTo(String branchName) {
        Utils.writeContents(HEAD, "ref: refs/heads/" + branchName);
    }
    public static String getActiveBranchName() {
        String headContent = Utils.readContentsAsString(HEAD);
        String branchName = headContent.substring(headContent.lastIndexOf("/") + 1);
        return branchName;
    }

    private static String getBranchHeadCommitId(String branchName) {
        File branchHeadFile = Utils.join(REF_HEADS_DIR, branchName);
        return Utils.readContentsAsString(branchHeadFile);
    }

    public static Commit getBranchHeadCommit(String branchName) {
        String commitId = getBranchHeadCommitId(branchName);
        return readCommit(commitId);
    }

    public static void createBranch(String branchName) {
        String activeCommitId = getActiveCommitId();
        setBranchHeadCommit(branchName, activeCommitId);
    }

    public static void removeBranch(String branchName) {
        File filePath = Utils.join(REF_HEADS_DIR, branchName);
        filePath.delete();
    }

    public static boolean branchExists(String branchName) {
        return Utils.join(REF_HEADS_DIR, branchName).exists();
    }

    public static Set<String> getBranchesStatus() {
        List<String> branchNames = Utils.plainFilenamesIn(REF_HEADS_DIR);
        String activeBranch = Persistor.getActiveBranchName();
        TreeSet<String> result = new TreeSet<>();
        for (String branchName : branchNames) {
            if (branchName.equals(activeBranch)) {
                result.add("*" + branchName);
            } else {
                result.add(branchName);
            }
        }
        return result;
    }

    public static void checkoutFileFromCommit(String fileName, Commit commit) {
        String hash = commit.getFileHash(fileName);
        String content = readBlob(hash);
        WorkingDir.writeContentToFile(fileName, content);
    }
}
