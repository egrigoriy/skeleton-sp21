package gitlet;

import java.io.File;

public class Persistor {

    public static void saveCommit(Commit commit) {
        File subDirPath = Utils.join(Repository.OBJECTS_DIR, getDirNameFromUID(commit.getUid()));
        if (!subDirPath.exists()) {
            subDirPath.mkdir();
        }
        File filePath = Utils.join(subDirPath, getFIleNameFromUID(commit.getUid()));
        Utils.writeObject(filePath, commit);
    }

    public static Commit readCommit(String uid) {
        if (uid == null ) {
            return null;
        }
        String subDirName = getDirNameFromUID(uid);
        String fileName = getFIleNameFromUID(uid);
        File file = Utils.join(Repository.OBJECTS_DIR, subDirName, fileName);
        return Utils.readObject(file, Commit.class);
    }


    private static String getDirNameFromUID(String uid) {
        return uid.substring(0, 2);
    }

    private static String getFIleNameFromUID(String uid) {
        return uid.substring(2);
    }
}
