package byow.Core;

import java.io.File;

public class History {
    private static final File CWD = new File(System.getProperty("user.dir"));
    private static final File HISTORY_FILE = Utils.join(CWD, "history.txt");
    private String history = "";

    public String read() {
        return Utils.readContentsAsString(HISTORY_FILE);
    }

    public void update(String s) {
        history += s;
    }

    public void save() {
        Utils.writeContents(HISTORY_FILE, history);
    }
}
