package gitlet;

import java.io.File;
import java.util.List;

public class WorkingDir {
    public static final File CWD = new File(System.getProperty("user.dir"));

    public static byte[] readFileContent(String fileName) {
        return Utils.readContents(Utils.join(CWD, fileName));
    }

    public static void writeContentToFile(String fileName, String content) {
        Utils.writeContents(Utils.join(CWD, fileName), content);
    }

    public static boolean fileExists(String fileName) {
        return Utils.join(CWD, fileName).exists();
    }
    public static void removeFile(String fileName) {
        File filePath = Utils.join(CWD, fileName);
        Utils.restrictedDelete(filePath);
    }
    public static String getFileHash(String fileName) {
        byte[] fileContent = readFileContent(fileName);
        String hash = Utils.sha1(fileContent);
        return hash;
    }

    public static void clean() {
        for (String f : Utils.plainFilenamesIn(WorkingDir.CWD)) {
            Utils.restrictedDelete(f);
        }
    }
    public static List<String> getFileNames() {
        return Utils.plainFilenamesIn(WorkingDir.CWD);
    }
}
