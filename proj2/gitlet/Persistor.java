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
    public static final File INDEX = Utils.join(GITLET_DIR, "index");

    public static void saveCommit(Commit commit) {
        File objectPath = hashToObjectPath(commit.getUid());
        Utils.writeObject(objectPath, commit);
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

    public static void saveBlob(String blobSHA1, byte[] fileContent) {
        File objectPath = hashToObjectPath(blobSHA1);
        Utils.writeContents(objectPath, fileContent);
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
}
