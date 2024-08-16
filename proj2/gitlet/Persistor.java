package gitlet;

import java.io.File;

public class Persistor {

    public static void saveCommit(Commit commit) {
        File subDirPath = Utils.join(Repository.OBJECTS_DIR, getDirNameFromUID(commit.getUid()));
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
        File file = Utils.join(Repository.OBJECTS_DIR, subDirName, fileName);
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
        File subDirPath =  Utils.join(Repository.OBJECTS_DIR, subDirName);
        if (!subDirPath.exists()) {
            subDirPath.mkdir();
        }
        File file = Utils.join(Repository.OBJECTS_DIR, subDirName, fileName);
        Utils.writeContents(file, fileContent);
    }

    public static void saveIndex(Index index) {
        Utils.writeObject(Repository.INDEX, index);
    }

    public static Index readIndex() {
        return Utils.readObject(Repository.INDEX, Index.class);
    }
}
