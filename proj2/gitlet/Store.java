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

    /**
     * Returns the index stored in the store or a clear one if it does not exist.
     * @return index
     */
    public static Index readIndex() {
        if (INDEX.exists()) {
            return Utils.readObject(INDEX, Index.class);
        } else {
            return new Index();
        }
    }

    /**
     * Sets the given branch name to the HEAD file
     * @param branchName
     */
    public static void setActiveBranchTo(String branchName) {
        if (branchName.contains("/")) {
            Utils.writeContents(HEAD, "ref: refs/remotes/" + branchName);
        } else {
            Utils.writeContents(HEAD, "ref: refs/heads/" + branchName);
        }
    }

    /**
     * Returns the name of the given branch stored at HEAD
     * @return active branch name
     */
    public static String getActiveBranchName() {
        String headContent = Utils.readContentsAsString(HEAD);
        if (headContent.contains("remotes")) {
            return headContent.split("remotes/")[1];
        }
        return headContent.substring(headContent.lastIndexOf("/") + 1);
    }

    /**
     * Returns the file pointing to the head commit of the given branch name
     * @param branchName
     * @return file with head commit id
     */
    private static File getBranchHeadFile(String branchName) {
        if (branchName.contains("/")) {
            return Utils.join(REF_REMOTES_DIR, branchName);
        }
        return Utils.join(REF_LOCAL_HEADS_DIR, branchName);
    }

    /**
     * Sets the given commit id to the file referencing to the given branch head commit
     * @param branchName
     * @param commitId
     */
    public static void setBranchHeadCommitId(String branchName, String commitId) {
        File branchHeadFile = getBranchHeadFile(branchName);
        Utils.writeContents(branchHeadFile, commitId);
    }

    /**
     * Returns the id of the head commit of the given branch name
     * @param branchName
     * @return commit id
     */
    public static String getBranchHeadCommitId(String branchName) {
        File branchHeadFile = getBranchHeadFile(branchName);
        return Utils.readContentsAsString(branchHeadFile);
    }

    /**
     * Returns the head commit of the given branch name
     * @param branchName
     * @return head commit
     */
    public static Commit getBranchHeadCommit(String branchName) {
        String commitId = getBranchHeadCommitId(branchName);
        return readCommit(commitId);
    }

    /**
     * Returns the id of the active branch head commit
     * @return head commit id
     */
    private static String getActiveCommitId() {
        return getBranchHeadCommitId(getActiveBranchName());
    }

    /**
     * Creates a branch with a given name
     * @param name
     */
    public static void createBranch(String name) {
        setBranchHeadCommitId(name, getActiveCommitId());
    }

    /**
     * Remove the branch with the given name
     * @param branchName
     */
    public static void removeBranch(String branchName) {
        File filePath = getBranchHeadFile(branchName);
        filePath.delete();
    }

    /**
     * Returns true if a branch with given name exists in the store, false otherwise
     * @param branchName
     * @return
     */
    public static boolean branchExists(String branchName) {
        return getBranchHeadFile(branchName).exists();
    }

    /**
     * Returns a list of all branch names in the store
     * @return a list of all branch names
     */
    private static List<String> getBranchNames() {
        return Utils.plainFilenamesIn(REF_LOCAL_HEADS_DIR);
    }

    /**
     * Returns a set of all branch names, where active one is marked by a star (*)
     * @return set of all branch names
     */
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

    /**
     * Checks out the given file name from the given commit to the working directory
     * @param fileName
     * @param commit
     */
    public static void checkoutFileFromCommit(String fileName, Commit commit) {
        String hash = commit.getFileHash(fileName);
        String content = readBlob(hash);
        WorkingDir.writeContentToFile(fileName, content);
    }

    /**
     * Checks out all files from the given commit to the working directory.
     * The working directory is first cleaned.
     * @param commit
     */
    public static void checkoutFilesFromCommit(Commit commit) {
        WorkingDir.clean();
        Set<String> checkedOutFileNames = commit.getFileNames();
        for (String fileName : checkedOutFileNames) {
            checkoutFileFromCommit(fileName, commit);
        }
    }


    /**
     * Returns true if a remote with given name is tracked by the store, otherwise false
     * @param remoteName
     * @return boolean
     */
    public static boolean remoteExists(String remoteName) {
        return getRemoteRefsDir(remoteName).exists();
    }

    /**
     * Returns the directory tracking the given remote name
     * @param remoteName
     * @return
     */
    private static File getRemoteRefsDir(String remoteName) {
        return Utils.join(REF_REMOTES_DIR, remoteName);
    }

    /**
     * Adds for tracking under given remote name having given remote url.
     * The information is stored in config file.
     * @param remoteName
     * @param remoteDirName
     */
    public static void addRemote(String remoteName, String remoteDirName) {
        // add to config
        setRemoteUrlToConfig(remoteName, remoteDirName);
        // add to refs
        getRemoteRefsDir(remoteName).mkdirs();
    }

    /**
     * Writes to the config file given remote name and its url
     * @param remoteName
     * @param remoteUrl
     */
    private static void setRemoteUrlToConfig(String remoteName, String remoteUrl) {
        Path url = Paths.get(remoteUrl).toAbsolutePath().normalize();
        Utils.writeContents(CONFIG, "remote." + remoteName + ".url=" + url);
    }

    /**
     * Returns the url corresponding to the given remote name
     * @param remoteName
     * @return remote url
     */
    private static File getRemoteUrlFromConfig(String remoteName) {
        String configLine = Utils.readContentsAsString(CONFIG);
        String urlString = configLine.split("=")[1];
        return Utils.join(urlString);
    }

    /**
     * Returns true if the remote url does not exist, otherwise false
     * @param remoteName
     * @return boolean
     */
    public static boolean remoteUrlExists(String remoteName) {
        File remoteUrl = getRemoteUrlFromConfig(remoteName);
        return remoteUrl.exists();
    }

    /**
     * Returns the distant ref heads directory
     * @param remoteName
     * @return directory file
     */
    private static File getDistantRemoteRefHeadsDir(String remoteName) {
        return Utils.join(getRemoteUrlFromConfig(remoteName), "refs/heads");
    }

    /**
     * Returns the distant head file of the given branch of a given remote
     * @param remoteName
     * @param remoteBranchName
     * @return
     */
    private static File getDistantBranchHeadFile(String remoteName, String remoteBranchName) {
        return Utils.join(getDistantRemoteRefHeadsDir(remoteName), remoteBranchName);
    }

    /**
     * Removes the remote with given name from tracking
     * @param remoteName
     */
    public static void removeRemote(String remoteName) {
        // remove from config
        Utils.writeContents(CONFIG, "");
        // remove from refs
        getRemoteRefsDir(remoteName).delete();
    }

    /**
     * Returns true if a given branch from given remote exist on remote side, otherwise false
     * @param remoteName
     * @param remoteBranchName
     * @return boolean
     */
    public static boolean distantBranchExists(String remoteName, String remoteBranchName) {
        File remoteRefsHeadsDir = getDistantRemoteRefHeadsDir(remoteName);
        List<String> remoteBranchNames = Utils.plainFilenamesIn(remoteRefsHeadsDir);
        return remoteBranchNames.contains(remoteBranchName);
    }


    /**
     * Returns the head commit id of the given branch of a given remote stored at distant side
     * @param remoteName
     * @param remoteBranchName
     * @return commit id
     */
    private static String getDistantBranchHeadCommitId(String remoteName, String remoteBranchName) {
        File path = getDistantBranchHeadFile(remoteName, remoteBranchName);
        return Utils.readContentsAsString(path);
    }

    /**
     * Copies all objects from distant site of given remote to the local store
     * @param remoteName
     */
    public static void copyDistantObjectsToLocal(String remoteName) {
        File remoteUrl = getRemoteUrlFromConfig(remoteName);
        File distantObjectsDir = Utils.join(remoteUrl, "objects");
        copyObjects(distantObjectsDir, OBJECTS_DIR);
    }

    /**
     * Copies all objects from local store to the distant one corresponding to a given remote
     * @param remoteName
     */
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

    /**
     * Copies the branch head file from distant side of the given remote and branch to the local
     * @param remoteName
     * @param remoteBranchName
     */
    public static void copyDistantBranchHeadToLocal(String remoteName, String remoteBranchName) {
        String commitId = getDistantBranchHeadCommitId(remoteName, remoteBranchName);
        File localBranchHeadFile = Utils.join(getRemoteRefsDir(remoteName), remoteBranchName);
        Utils.writeContents(localBranchHeadFile, commitId);
    }

    /**
     * Copies the branch head file from local to distant side of the given remote and branch
     * @param remoteName
     * @param remoteBranchName
     */
    public static void copyLocalBranchHeadToDistant(String remoteName, String remoteBranchName) {
        String localBranchHead = getActiveCommitId();
        File distantBranchHeadFile = getDistantBranchHeadFile(remoteName, remoteBranchName);
        Utils.writeContents(distantBranchHeadFile, localBranchHead);
    }

    /**
     * Returns true if the given branch of given remote on local side is behind the distant one,
     * otherwise false.
     * @param remoteName
     * @param remoteBranchName
     * @return
     */
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
