package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static gitlet.Utils.UID_LENGTH;
import static gitlet.Utils.join;

/**
 * Represents a Store
 *
 *  @author Grigoriy Emiliyanov
 */
public class Store {
    public static final File GITLET_DIR = join(WorkingDir.CWD, ".gitlet");
    public static final File CONFIG = join(GITLET_DIR, "config");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File REF_LOCAL_HEADS_DIR = join(GITLET_DIR, "refs/heads");

    public static final File REF_REMOTES_DIR = join(GITLET_DIR, "remotes");

    public static final File HEAD = Utils.join(GITLET_DIR, "HEAD");

    public static final File INDEX = Utils.join(GITLET_DIR, "index");

    private enum ObjectType {
        COMMIT,
        BLOB
    }
    /**
     * Builds the infrastructure of the store
     */
    public static void buildInfrastructure() {
        GITLET_DIR.mkdir();
        OBJECTS_DIR.mkdirs();
        REFS_DIR.mkdir();
        REF_LOCAL_HEADS_DIR.mkdir();
    }

    /**
     * Returns true if the store is initialized
     * @return boolean
     */
    public static boolean isInitialized() {
        return GITLET_DIR.exists();
    }

    /**
     * Reads the content of the file with given name
     * @param fileName
     * @return
     */
    private static ArrayList<Object> readRawObjectContent(File fileName) {
        ArrayList<Object> content = Utils.readObject(fileName, ArrayList.class);
        return content;
    }

    /**
     * Saves given content under given file name as given object type.
     * Stored object is represented as array with object type at position 0,
     * and content at position 1.
     * @param fileName
     * @param content
     * @param type
     */
    private static void saveRawObject(File fileName, Object content, ObjectType type) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(type);
        objects.add(content);
        Utils.writeObject(fileName, (Serializable) objects);
    }

    /**
     * Reads as object the given file name from the store
     * The object content is stored at position 1 of the array.
     * @param fileName
     * @return objectContent
     */
    private static Object readRawObject(File fileName) {
        ArrayList<Object> content = readRawObjectContent(fileName);
        return content.get(1);
    }

    /**
     * Saves the given commit as object in the store
     * @param commit
     * @return
     */
    public static String saveCommit(Commit commit) {
        String commitId = commit.getUid();
        saveRawObject(getObjectFile(commitId), commit, ObjectType.COMMIT);
        return commitId;
    }

    /**
     * Returns the object file of the given stored file name
     * @param fileName
     * @return file
     */
    private static File getObjectFile(String fileName) {
        return Utils.join(OBJECTS_DIR, getObjectFileName(fileName));
    }

    /**
     * Returns the object file name corresponding to given file name.
     * The given file name could be shorter then 40 chars
     * @param fileName
     * @return fileName
     */
    private static String getObjectFileName(String fileName) {
        if (fileName.length() == UID_LENGTH) {
            return fileName;
        }
        return findFullFileName(fileName);
    }

    /**
     * Returns the full file name corresponding to the given short version.
     * Empty string is returned if full file name is not found in the store.
     * @param shortFileName
     * @return
     */
    private static String findFullFileName(String shortFileName) {
        for (String fileName : Utils.plainFilenamesIn(OBJECTS_DIR)) {
            if (fileName.contains(shortFileName)) {
                return fileName;
            }
        }
        return "";
    }

    /**
     * Reads the commit corresponding to the given id.
     * @param commitId
     * @return commit
     */
    public static Commit readCommit(String commitId) {
        if (commitId == null) {
            return null;
        }
        File readPath = getObjectFile(commitId);
        if (!readPath.exists()) {
            return null;
        }
        return (Commit) readRawObject(readPath);
    }

    /**
     * Returns a list of all commits ids from the store
     * @return list of all commits ids
     */
    private static List<String> getAllCommitsIds() {
        List<String> result = new ArrayList<>();
        for (String fileName : Utils.plainFilenamesIn(OBJECTS_DIR)) {
            ArrayList<Object> content = readRawObjectContent(Utils.join(OBJECTS_DIR, fileName));
            if (content.get(0).equals(ObjectType.COMMIT)) {
                result.add(fileName);
            }
        }
        return result;
    }

    /**
     * Returns the list of all stored commits.
     * @return list of all stored commits
     */
    public static List<Commit> getAllCommits() {
        List<Commit> result = new ArrayList<>();
        for (String fileName : Store.getAllCommitsIds()) {
            Commit commit = Store.readCommit(fileName);
            result.add(commit);
        }
        return result;
    }

    /**
     * Saves to the store a file with given name as blob
     * @param fileName
     * @return uid of the stored file
     */
    public static String saveBlob(String fileName) {
        byte[] fileContent = WorkingDir.readFileContent(fileName);
        Blob blob = new Blob(fileContent);
        String uid = blob.getUid();
        File savePath = getObjectFile(uid);
        if (!savePath.exists()) {
            saveRawObject(savePath, fileContent, ObjectType.BLOB);
        }
        return uid;
    }

    /**
     * Reads a file with given id as blob
     * @param uid
     * @return
     */
    public static String readBlob(String uid) {
        Object rawObject = readRawObject(Utils.join(OBJECTS_DIR, uid));
        String result = new String((byte[]) rawObject, StandardCharsets.UTF_8);
        return result;
    }

    /**
     * Save the given index as file in the store
     * @param index
     */
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

    // READ WRITE FROM HEAD
    public static void setActiveBranchTo(String branchName) {
        if (branchName.contains("/")) {
            Utils.writeContents(HEAD, "ref: refs/remotes/" + branchName);
        } else {
            Utils.writeContents(HEAD, "ref: refs/heads/" + branchName);
        }
    }
    public static String getActiveBranchName() {
        String headContent = Utils.readContentsAsString(HEAD);
        if (headContent.contains("remotes")) {
            return headContent.split("remotes/")[1];
        }
        return headContent.substring(headContent.lastIndexOf("/") + 1);
    }

    // END

    // GET BRANCH NAME FILE FROM REFS
    private static File getBranchHeadFile(String branchName) {
        if (branchName.contains("/")) {
            return Utils.join(REF_REMOTES_DIR, branchName);
        }
        return Utils.join(REF_LOCAL_HEADS_DIR, branchName);
    }
    // END


    // GET SET CONTENT OF BRANCH FILE IN REFS
    public static void setBranchHeadCommitId(String branchName, String commitId) {
        File branchHeadFile = getBranchHeadFile(branchName);
        Utils.writeContents(branchHeadFile, commitId);
    }

    public static String getBranchHeadCommitId(String branchName) {
        File branchHeadFile = getBranchHeadFile(branchName);
        return Utils.readContentsAsString(branchHeadFile);
    }

    public static Commit getBranchHeadCommit(String branchName) {
        String commitId = getBranchHeadCommitId(branchName);
        return readCommit(commitId);
    }
    private static String getActiveCommitId() {
        return getBranchHeadCommitId(getActiveBranchName());
    }
    // END

    public static void createBranch(String name) {
        setBranchHeadCommitId(name, getActiveCommitId());
    }
    public static void removeBranch(String branchName) {
        File filePath = getBranchHeadFile(branchName);
        filePath.delete();
    }

    public static boolean branchExists(String branchName) {
        return getBranchHeadFile(branchName).exists();
    }

    private static List<String> getBranchNames() {
        return Utils.plainFilenamesIn(REF_LOCAL_HEADS_DIR);
    }
    public static Set<String> getBranchesStatus() {
        List<String> branchNames = getBranchNames();
        String activeBranch = Store.getActiveBranchName();
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

    public static void checkoutFilesFromCommit(Commit commit) {
        WorkingDir.clean();
        Set<String> checkedOutFileNames = commit.getFileNames();
        for (String fileName : checkedOutFileNames) {
            checkoutFileFromCommit(fileName, commit);
        }
    }

    public static boolean remoteExists(String remoteName) {
        return getRemoteRefsDir(remoteName).exists();
    }

    private static File getRemoteRefsDir(String remoteName) {
        return Utils.join(REF_REMOTES_DIR, remoteName);
    }

    public static void addRemote(String remoteName, String remoteDirName) {
        // add to config
        setRemoteUrlToConfig(remoteName, remoteDirName);
        // add to refs
        getRemoteRefsDir(remoteName).mkdirs();
    }

    private static void setRemoteUrlToConfig(String remoteName, String remoteUrl) {
        Path url = Paths.get(remoteUrl).toAbsolutePath().normalize();
        Utils.writeContents(CONFIG, "remote." + remoteName + ".url=" + url);
    }

    private static File getRemoteUrlFromConfig(String remoteName) {
        String configLine = Utils.readContentsAsString(CONFIG);
        String urlString = configLine.split("=")[1];
        return Utils.join(urlString);
    }

    public static boolean remoteUrlExists(String remoteName) {
        File remoteUrl = getRemoteUrlFromConfig(remoteName);
        return remoteUrl.exists();
    }

    private static File getDistantRemoteRefHeadsDir(String remoteName) {
        return Utils.join(getRemoteUrlFromConfig(remoteName), "refs/heads");
    }

    private static File getDistantBranchHeadFile(String remoteName, String remoteBranchName) {
        return Utils.join(getDistantRemoteRefHeadsDir(remoteName), remoteBranchName);
    }
    public static void removeRemote(String remoteName) {
        // remove from config
        Utils.writeContents(CONFIG, "");
        // remove from refs
        getRemoteRefsDir(remoteName).delete();
    }

    public static boolean distantBranchExists(String remoteName, String remoteBranchName) {
        File remoteRefsHeadsDir = getDistantRemoteRefHeadsDir(remoteName);
        List<String> remoteBranchNames = Utils.plainFilenamesIn(remoteRefsHeadsDir);
        return remoteBranchNames.contains(remoteBranchName);
    }


    private static String getDistantBranchHeadCommitId(String remoteName, String remoteBranchName) {
        File path = getDistantBranchHeadFile(remoteName, remoteBranchName);
        return Utils.readContentsAsString(path);
    }

    public static void copyDistantObjectsToLocal(String remoteName) {
        File remoteUrl = getRemoteUrlFromConfig(remoteName);
        File distantObjectsDir = Utils.join(remoteUrl, "objects");
        copyObjects(distantObjectsDir, OBJECTS_DIR);
    }

    public static void copyDistantBranchHeadToLocal(String remoteName, String remoteBranchName) {
        String commitId = getDistantBranchHeadCommitId(remoteName, remoteBranchName);
        File localBranchHeadFile = Utils.join(getRemoteRefsDir(remoteName), remoteBranchName);
        Utils.writeContents(localBranchHeadFile, commitId);
    }

    public static void copyLocalBranchHeadToDistant(String remoteName, String remoteBranchName) {
        String localBranchHead = getActiveCommitId();
        File distantBranchHeadFile = getDistantBranchHeadFile(remoteName, remoteBranchName);
        Utils.writeContents(distantBranchHeadFile, localBranchHead);
    }

    public static void copyLocalObjectsToDistant(String remoteName) {
        File remoteUrl = getRemoteUrlFromConfig(remoteName);
        File distantObjectsDir = Utils.join(remoteUrl, "objects");
        copyObjects(OBJECTS_DIR, distantObjectsDir);
    }

    private static void copyObjects(File srcDir, File dstDir) {
        for (String fileName : Utils.plainFilenamesIn(srcDir)) {
            Path src = Paths.get(Utils.join(srcDir, fileName).toString());
            Path dst = Paths.get(Utils.join(dstDir, fileName).toString());
            try {
                Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static boolean isLocalBehindRemote(String remoteName, String remoteBranchName) {
        File remoteRef = Utils.join(REF_REMOTES_DIR, remoteName, remoteBranchName);
        if (!remoteRef.exists()) {
            return true;
        }
        String remoteCommitId = Utils.readContentsAsString(remoteRef);
        String localCommitId = getActiveCommitId();
        Commit current = readCommit(localCommitId);
        while (current != null) {
            if (current.getUid().equals(remoteCommitId)) {
                return false;
            }
            current = readCommit(current.getFirstParent());
        }
        return true;
    }


}
