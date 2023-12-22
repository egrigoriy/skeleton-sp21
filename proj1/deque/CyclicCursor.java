package deque;

public class Cursor {
    private int capacity;
    private int dir;

    public Cursor(int capacity, int dir) {
        this.capacity = capacity;
        this.dir = dir;
    }

    public static enum DIR {
        FORTH(1),
        BACK(-1);

        private final int dir;
        DIR(int dir) {
            this.dir = dir;
        }

        public int getDir() {
            return dir;
        }
    }
}
