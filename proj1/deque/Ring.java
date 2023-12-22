package deque;

public class Ring {
    private int capacity;
    private CyclicCursor first;
    private CyclicCursor last;

    private int size;

    public Ring(int capacity, int size) {
        this.capacity = capacity;
        this.size = size;
        first = new CyclicCursor(capacity, CyclicCursor.DIR.BACK, 0);
        last = new CyclicCursor(capacity, CyclicCursor.DIR.FORTH, size - 1);
    }

    public int size() {
        return size;
    }
    public int getNextFirst() {
        return first.getNextPosition();
    }

    public int getNextLast() {
        return last.getNextPosition();
    }

    public void tickNextFirst() {
        first.advance();
        size++;
    }

    public void backNextFirst() {
        first.back();
        size--;
    }

    public void backNextLast() {
        last.back();
        size--;
    }

    public void tickNextLast() {
        last.advance();
        size++;
    }

    public int getFirstIndex() {
        return first.getPosition();
    }

    public int getLastIndex() {
        return last.getPosition();
    }

    public int indexToStorageIndex(int index) {
        int start = getFirstIndex();
        return getRingIndexWithShift(index, start);
    }

    private int getRingIndexWithShift(int linearIndex, int start) {
        return (start + linearIndex) % capacity;
    }
}
