package gitlet;

import gitlet.storage.Blob;
import gitlet.storage.Commit;

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

public class Store {
    public static final File GITLET_DIR = join(WorkingDir.CWD, ".gitlet");
    public static final File CONFIG = join(GITLET_DIR, "config");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File REF_LOCAL_HEADS_DIR = join(GITLET_DIR, "refs/heads");

    public static final File REF_REMOTES_DIR = join(GITLET_DIR, "remotes");

    public static final File HEAD = Utils.join(GITLET_DIR, "HEAD");

    public static final File INDEX = Utils.join(GITLET_DIR, "index");

    public static boolean isInitialized() {
        return GITLET_DIR.exists();
    }

    private static ArrayList<Object> readRawObjectContent(File fileName) {
        ArrayList<Object> content = Utils.readObject(fileName, ArrayList.class);
        return content;
    }

    private static void saveRawObject(File fileName, Object content, Object type) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(type);
        objects.add(content);
        Utils.writeObject(fileName, (Serializable) objects);
    }

    private static Object readRawObject(File fileName) {
        ArrayList<Object> content = readRawObjectContent(fileName);
        Object type = content.get(0);
        return content.get(1);
    }

    public static void buildInfrastructure() {
        GITLET_DIR.mkdir();
        REFS_DIR.mkdir();
        REF_LOCAL_HEADS_DIR.mkdir();
        OBJECTS_DIR.mkdirs();
    }

    public static String saveCommit(Commit commit) {
        String commitId = commit.getUid();
        saveRawObject(getObjectFile(commitId), commit, "commit");
        return commitId;
    }

    private static File getObjectFile(String fileName) {
        return Utils.join(OBJECTS_DIR, getObjectFileName(fileName));
    }
    private static String getObjectFileName(String fileName) {
        if (fileName.length() == UID_LENGTH) {
            return fileName;
        }
        return fullObjectId(fileName);
    }
    private static String fullObjectId(String commitID) {
        for (String fileName : Utils.plainFilenamesIn(OBJECTS_DIR)) {
            if (fileName.contains(commitID)) {
                return fileName;
            }
        }
        return "";
    }

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

    private static List<String> getAllCommitsNames() {
        List<String> result = new ArrayList<>();
        for (String fileName : Utils.plainFilenamesIn(OBJECTS_DIR)) {
            ArrayList<Object> content = readRawObjectContent(Utils.join(OBJECTS_DIR, fileName));
            if (content.get(0).equals("commit")) {
                result.add(fileName);
            }
        }
        return result;
    }

    public static String saveBlob(String fileName) {
        byte[] fileContent = WorkingDir.readFileContent(fileName);
        Blob blob = new Blob(fileContent);
        String hash = blob.getUid();
        File savePath = getObjectFile(hash);
        if (!savePath.exists()) {
            saveRawObject(savePath, fileContent, "blob");
        }
        return hash;
    }

    public static String readBlob(String hash) {
        Object rawObject = readRawObject(Utils.join(OBJECTS_DIR, hash));
        String result = new String((byte[]) rawObject, StandardCharsets.UTF_8);
        return result;
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


    private static File getBranchHeadFile(String branchName) {
        if (branchName.contains("/")) {
            return Utils.join(REF_REMOTES_DIR, branchName);
        }
        return Utils.join(REF_LOCAL_HEADS_DIR, branchName);
    }
    private static void setBranchHeadCommit(String branchName, String commitId) {
        File branchHeadFile = getBranchHeadFile(branchName);
        Utils.writeContents(branchHeadFile, commitId);
    }

    public static Commit getActiveCommit() {
        return readCommit(getActiveCommitId());
    }

    public static String getActiveCommitId() {
        return getBranchHeadCommitId(getActiveBranchName());
    }

    public static List<Commit> getAllCommits() {
        List<Commit> result = new ArrayList<>();
        for (String fileName : getAllCommitsNames()) {
            Commit commit = readCommit(fileName);
            result.add(commit);
        }
        return result;
    }



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

    public static String getBranchHeadCommitId(String branchName) {
        File branchHeadFile = getBranchHeadFile(branchName);
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
