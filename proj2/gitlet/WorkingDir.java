package gitlet;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

public class WorkingDir {
    public static final File CWD = new File(System.getProperty("user.dir"));

    public static byte[] readContent(String fileName) {
        return Utils.readContents(Utils.join(CWD, fileName));
    }

    public static void writeContentToCWDFile(String fileName, String content) {
        Utils.writeContents(Utils.join(CWD, fileName), content);
    }

    public static boolean fileExists(String fileName) {
        return Utils.join(CWD, fileName).exists();
    }
    public static void removeCWDFile(String fileName) {
        File filePath = Utils.join(CWD, fileName);
        Utils.restrictedDelete(filePath);
    }
    public static String getFileHash(String fileName) {
        byte[] fileContent = Utils.readContents(Utils.join(CWD, fileName));
        String hash = Utils.sha1(fileContent);
        return hash;
    }

    public static void writeCWDFiles(TreeMap<String, String> files) {
        for (String fileName : files.keySet()) {
            String sha = files.get(fileName);
            String content = Persistor.readBlob(sha);
            writeContentToCWDFile(fileName, content);
        }
    }

    public static List<String> getFileNames() {
        return Utils.plainFilenamesIn(WorkingDir.CWD);
    }
}
