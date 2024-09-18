package gitlet;

import java.io.File;
import java.util.List;

/**
 * Represents a working directory
 *
 *  @author Grigoriy Emiliyanov
 */
public class WorkingDir {
    public static final File CWD = new File(System.getProperty("user.dir"));

    /**
     * Returns the file content of the given file name from the working directory
     * @param fileName
     * @return content
     */
    public static byte[] readFileContent(String fileName) {
        return Utils.readContents(Utils.join(CWD, fileName));
    }

    /**
     * Writes the given content to a file with given name in the working directory
     * @param fileName
     * @param content
     */
    public static void writeContentToFile(String fileName, String content) {
        Utils.writeContents(Utils.join(CWD, fileName), content);
    }

    /**
     * Returns true if a file with given name exists in the working directory, otherwise false
     * @param fileName
     * @return
     */
    public static boolean fileExists(String fileName) {
        return Utils.join(CWD, fileName).exists();
    }

    /**
     * Removes file with given file name from working directory
     * @param fileName
     */
    public static void removeFile(String fileName) {
        File filePath = Utils.join(CWD, fileName);
        Utils.restrictedDelete(filePath);
    }

    /**
     * Returns the hash of the content of a file with given name in working directory
     * @param fileName
     * @return hash
     */
    public static String getFileHash(String fileName) {
        byte[] fileContent = readFileContent(fileName);
        String hash = Utils.sha1(fileContent);
        return hash;
    }

    /**
     * Removes all files from the working directory
     */
    public static void clean() {
        for (String f : Utils.plainFilenamesIn(WorkingDir.CWD)) {
            Utils.restrictedDelete(f);
        }
    }

    /**
     * Returns the list of all file names in the working directory
     * @return list of file names
     */
    public static List<String> getFileNames() {
        return Utils.plainFilenamesIn(WorkingDir.CWD);
    }
}
