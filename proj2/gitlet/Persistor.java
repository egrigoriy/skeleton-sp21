package gitlet;

import java.io.File;

import static gitlet.Utils.join;

public class Persistor {
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File REF_HEADS_DIR = join(REFS_DIR, "heads");
    public static final File HEAD_MASTER = Utils.join(REF_HEADS_DIR, "master");
    public static final File HEAD = Utils.join(GITLET_DIR, "HEAD");

    public static final File INDEX = Utils.join(GITLET_DIR, "index");

    public static String saveCommit(Commit commit) {
        String sha = Utils.sha1(
                commit.getTimestamp().toString().getBytes(),
                commit.getMessage().getBytes());
        commit.setUid(sha);
        File objectPath = hashToObjectPath(sha);
        Utils.writeObject(objectPath, commit);
        return sha;
    }

    public static Commit readCommit(String commitID) {
        if (commitID == null) {
            return null;
        }
        File objectPath = hashToObjectPath(commitID);
        return Utils.readObject(objectPath, Commit.class);
    }

    private static File hashToObjectPath(String hash) {
        File subDirPath = Utils.join(OBJECTS_DIR, hash.substring(0, 2));
        if (!subDirPath.exists()) {
            subDirPath.mkdir();
        }
        String fileName = hash.substring(2);
        return Utils.join(subDirPath, fileName);
    }

    public static String saveBlob(String fileName) {
        byte[] fileContent = Utils.readContents(Utils.join(Persistor.CWD, fileName));
        String blobSHA1 = Utils.sha1(fileContent);
        File objectPath = hashToObjectPath(blobSHA1);
        Utils.writeContents(objectPath, fileContent);
        return blobSHA1;
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

    public static String readMaster() {
        return Utils.readContentsAsString(HEAD_MASTER);
    }

    public static boolean isRepositoryInitialized() {
        return GITLET_DIR.exists();
    }

    public static void buildInfrastructure() {
        GITLET_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        REFS_DIR.mkdir();
        REF_HEADS_DIR.mkdir();
    }
    public static void saveMaster(String uid) {
        Utils.writeContents(HEAD_MASTER, uid);
    }

    public static Commit readLastCommit() {
        String lastCommitID = Persistor.readMaster();
        return readCommit(lastCommitID);
    }

    public static String readTrackedFileContent(String blobSHA1) {
        File objectPath = hashToObjectPath(blobSHA1);
        return Utils.readContentsAsString(objectPath);
    }

    public static void writeContentToCWDFile(String fileName, String content) {
        Utils.writeContents(Utils.join(CWD, fileName), content);
    }

    public static boolean fileExists(String fileName) {
        return Utils.join(CWD, fileName).exists();
    }

    public static void headToMaster() {
        String refToMaster = "refs/heads/master";
        Utils.writeContents(HEAD, "path: " + refToMaster);
    }

    public static void removeCWDFile(String fileName) {
        File filePath = Utils.join(CWD, fileName);
        Utils.restrictedDelete(filePath);
    }
}
