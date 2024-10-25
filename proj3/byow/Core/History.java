package byow.Core;

import java.io.File;

public class History {
    private static final File CWD = new File(System.getProperty("user.dir"));
    private static final File historyFile = Utils.join(CWD, "history.txt");
    private String history = "";

    public String load() {
        return Utils.readContentsAsString(historyFile);
    }

    public void update(String s) {
        history += s;
    }

    public void save() {
        Utils.writeContents(historyFile, history);
    }
}
