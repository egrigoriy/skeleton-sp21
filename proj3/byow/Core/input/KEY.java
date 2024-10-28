package byow.Core.input;

public enum KEY {
    NEW_GAME("n"),
    LOAD_GAME("l"),
    QUIT(":");

    private String key;

    KEY(String key) {
        this.key = key;
    }
}
