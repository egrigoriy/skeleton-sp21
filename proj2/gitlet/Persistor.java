package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static gitlet.Utils.*;

public class Persistor {
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(WorkingDir.CWD, ".gitlet");
    public static final File CONFIG = join(GITLET_DIR, "config");
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File REF_LOCAL_HEADS_DIR = join(GITLET_DIR, "refs/heads");

    public static final File REF_REMOTES_DIR = join(GITLET_DIR, "remotes");

    public static final File HEAD = Utils.join(GITLET_DIR, "HEAD");

    public static final File INDEX = Utils.join(GITLET_DIR, "index");

    public static boolean isRepositoryInitialized() {
        return GITLET_DIR.exists();
    }

    public static void buildInfrastructure() {
        GITLET_DIR.mkdir();
        REFS_DIR.mkdir();
        REF_LOCAL_HEADS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
    }

    public static String saveCommit(Commit commit) {
        String commitId = Utils.sha1(
                commit.getTimestamp().toString().getBytes(),
                commit.getMessage().getBytes());
        commit.setUid(commitId);
        Utils.writeObject(getCommitFile(commitId), commit);
        return commitId;
    }

    public static Commit readCommit(String commitId) {
        if (commitId == null) {
            return null;
        }
        File readPath = getCommitFile(commitId);
        if (!readPath.exists()) {
            return null;
        }
        return Utils.readObject(readPath, Commit.class);
    }

    private static File getCommitFile(String commitId) {
        return Utils.join(COMMITS_DIR, getCommitFileName(commitId));
    }

    private static String getCommitFileName(String commitId) {
        if (commitId.length() == UID_LENGTH) {
            return commitId;
        }
        return fullCommitId(commitId);
    }

    private static List<String> getAllCommitsNames() {
        return Utils.plainFilenamesIn(COMMITS_DIR);
    }

    private static List<String> getAllBlobsNames() {
        return Utils.plainFilenamesIn(BLOBS_DIR);
    }
    private static String fullCommitId(String commitID) {
        for (String fileName : getAllCommitsNames()) {
            if (fileName.contains(commitID)) {
                return fileName;
            }
        }
        return "";
    }

    public static List<Commit> getCommitParents(Commit c) {
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

    private static File getBlobFile(String fileName) {
        return Utils.join(BLOBS_DIR, fileName);
    }
    public static String saveBlob(String fileName) {
        byte[] fileContent = WorkingDir.readFileContent(fileName);
        String hash = Utils.sha1(fileContent);
        File savePath = getBlobFile(hash);
        if (!savePath.exists()) {
            Utils.writeContents(savePath, fileContent);
        }
        return hash;
    }

    public static String readBlob(String hash) {
        return Utils.readContentsAsString(getBlobFile(hash));
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
        return getBranchHeadCommit(getActiveBranchName());
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

    private static String getBranchHeadCommitId(String branchName) {
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

    public static void setRemoteUrlToConfig(String remoteName, String remoteUrl) {
        Path url = Paths.get(remoteUrl).toAbsolutePath().normalize();
        Utils.writeContents(CONFIG, "remote." + remoteName + ".url=" + url);
    }

    public static String getRemoteUrlFromConfig(String remoteName) {
        String configLine = Utils.readContentsAsString(CONFIG);
        return configLine.split("=")[1];
    }

    public static boolean remoteDirExists(String remoteName) {
        String remoteUrl = getRemoteUrlFromConfig(remoteName);
        return Utils.join(remoteUrl).exists();
    }

    public static void removeRemote(String remoteName) {
        // remove from config
        Utils.writeContents(CONFIG, "");
        // remove from refs
        getRemoteRefsDir(remoteName).delete();
    }


    public static boolean remoteBranchExists(String remoteName, String remoteBranchName) {
        String remoteUrl = getRemoteUrlFromConfig(remoteName);
        File remoteRefsHeadsDir = Utils.join(remoteUrl, "refs/heads");
        List<String> remoteBranchNames = Utils.plainFilenamesIn(remoteRefsHeadsDir);
        return remoteBranchNames.contains(remoteBranchName);
    }


    public static String getRemoteBranchHeadCommitId(String remoteName, String remoteBranchName) {
        String remoteUrl = getRemoteUrlFromConfig(remoteName);
        File path = Utils.join(remoteUrl, "refs/heads/" + remoteBranchName);
        return Utils.readContentsAsString(path);
    }

    public static Commit readRemoteCommit(String remoteName, String remoteBranchTipCommitId) {
        String remoteUrl = getRemoteUrlFromConfig(remoteName);
        return null;

    }

    public static void copyRemoteBranchCommitsAndBlobs(String remoteName) {
        String remoteUrl = getRemoteUrlFromConfig(remoteName);
        File remoteCommitsDir = Utils.join(remoteUrl, "commits");
        List<String> allRemoteCommitsFileNames = Utils.plainFilenamesIn(remoteCommitsDir);
        for (String fileName : allRemoteCommitsFileNames) {
            Path src = Paths.get(Utils.join(remoteCommitsDir, fileName).toString());
            Path dst = Paths.get(Utils.join(COMMITS_DIR, fileName).toString());
            try {
                Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File remoteBlobsDir = Utils.join(remoteUrl, "blobs");
        List<String> allRemoteBlobsFileNames = Utils.plainFilenamesIn(remoteBlobsDir);
        for (String fileName : allRemoteBlobsFileNames) {
            Path src = Paths.get(Utils.join(remoteBlobsDir, fileName).toString());
            Path dst = Paths.get(Utils.join(BLOBS_DIR, fileName).toString());
            try {
                Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void copyRemoteBranchHeadToLocal(String remoteName,
                                                   String remoteBranchName,
                                                   String commitId) {
        File branchHeadFile = Utils.join(getRemoteRefsDir(remoteName), remoteBranchName);
        Utils.writeContents(branchHeadFile, commitId);
    }

    public static void copyLocalBranchHeadToRemote(String remoteName, String remoteBranchName) {
        String localBranchHead = getActiveCommitId();
        String remoteUrl = getRemoteUrlFromConfig(remoteName);
        File remoteRefsHeadsDir = Utils.join(remoteUrl, "refs/heads");
        File remoteBranchHeadFile = Utils.join(remoteRefsHeadsDir, remoteBranchName);
        Utils.writeContents(remoteBranchHeadFile, localBranchHead);
    }

    public static void copyLocalBranchCommitsAndBlobs(String remoteName, String remoteBranchName) {
        String remoteUrl = getRemoteUrlFromConfig(remoteName);
        File remoteCommitsDir = Utils.join(remoteUrl, "commits");
        for (String fileName : getAllCommitsNames()) {
            Path src = Paths.get(Utils.join(COMMITS_DIR, fileName).toString());
            Path dst = Paths.get(Utils.join(remoteCommitsDir, fileName).toString());
            try {
                Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File remoteBlobsDir = Utils.join(remoteUrl, "blobs");
        List<String> allLocalBlobsFileNames = Utils.plainFilenamesIn(BLOBS_DIR);
        for (String fileName : allLocalBlobsFileNames) {
            Path src = Paths.get(Utils.join(BLOBS_DIR, fileName).toString());
            Path dst = Paths.get(Utils.join(remoteBlobsDir, fileName).toString());
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
