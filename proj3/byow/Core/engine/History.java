package byow.Core.engine;

import java.io.File;

/**
 * Represents a history as string, that can be stored within a file
 */
public class History {
    private static final File CWD = new File(System.getProperty("user.dir"));
    private static final File HISTORY_FILE = Utils.join(CWD, "history.txt");
    private String history = "";

    public String read() {
        history = "";
        return Utils.readContentsAsString(HISTORY_FILE);
    }

    public void update(String s) {
        history += s;
    }

    public void save() {
        Utils.writeContents(HISTORY_FILE, history);
    }

    public void clear() {
        history = "";
    }
}
