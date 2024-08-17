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
        File subDirPath = Utils.join(OBJECTS_DIR, getDirNameFromUID(commit.getUid()));
        if (!subDirPath.exists()) {
            subDirPath.mkdir();
        }
        File filePath = Utils.join(subDirPath, getFileNameFromUID(commit.getUid()));
        Utils.writeObject(filePath, commit);
    }

    public static Commit readCommit(String uid) {
        if (uid == null ) {
            return null;
        }
        String subDirName = getDirNameFromUID(uid);
        String fileName = getFileNameFromUID(uid);
        File file = Utils.join(OBJECTS_DIR, subDirName, fileName);
        return Utils.readObject(file, Commit.class);
    }


    private static String getDirNameFromUID(String uid) {
        return uid.substring(0, 2);
    }

    private static String getFileNameFromUID(String uid) {
        return uid.substring(2);
    }

    public static void saveBlob(String blobSHA1, byte[] fileContent) {
        String subDirName = getDirNameFromUID(blobSHA1);
        String fileName = getFileNameFromUID(blobSHA1);
        File subDirPath =  Utils.join(OBJECTS_DIR, subDirName);
        if (!subDirPath.exists()) {
            subDirPath.mkdir();
        }
        File file = Utils.join(OBJECTS_DIR, subDirName, fileName);
        Utils.writeContents(file, fileContent);
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
}
