package byow.Core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class History {
    private static final File CWD = new File(System.getProperty("user.dir"));
    private static final File HISTORY_FILE = Utils.join(CWD, "history.txt");
    private String history = "";

    public History() {
//       try {
//           Files.createFile(HISTORY_FILE.toPath());
//       } catch (IOException ignored) {}
    }
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
