package deque;

public class CyclicCursor {
    private int capacity;
    private DIR dir;
    private int position;

    public CyclicCursor(int capacity, DIR dir, int position) {
        this.capacity = capacity;
        this.dir = dir;
        this.position = (position == -1) ? capacity - 1 : position;
    }

    public int getPosition() {
        return position;
    }

    public int getPrevPosition() {
        return getNextTo(DIR.BACK);
    }

    public int getNextPosition() {
        return getNextTo(DIR.FORTH);
    }

    private int getNextTo(DIR d) {
        int result = (position + d.dir * dir.dir) % capacity;
        if (result == -1) {
            result = capacity - 1;
        }
        return result;
    }

    public void advance() {
        position = getNextPosition();
    }

    public void back() {
        position = getPrevPosition();
    }

    public enum DIR {
        FORTH(1),
        BACK(-1);

        private final int dir;
        DIR(int dir) {
            this.dir = dir;
        }
    }
}
