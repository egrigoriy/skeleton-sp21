package deque;

public class Ring {
    private int capacity;
    public int nextFirst;
    public int nextLast;

    public Ring(int capacity) {
        this.capacity = capacity;
        nextFirst = capacity / 2;
        nextLast = nextFirst + 1;
    }

    public int getNextFirst() {
        return nextFirst;
    }

    public int getNextLast() {
        return nextLast;
    }

    public int getNext(int i) {
        return (i + 1) % capacity;
    }

    public int getPrev(int i) {
        i = i - 1;
        if (i == -1) {
            i = capacity - 1;
        }
        return i;
    }

    public void tickNextFirst() {
        nextFirst = getPrev(nextFirst);
    }

    public void tickNextLast() {
        nextLast = getNext(nextLast);
    }

    public int getFirstIndex() {
        return getNext(nextFirst);
    }

    public int getLastIndex() {
        return getPrev(nextLast);
    }

    public int linearToStorageIndex(int index) {
        int start = getFirstIndex();
        return getRingIndexWithShift(index, start);
    }

    /**
     * Returns the linear index of an item within a ring
     *
     * @param ringIndex in the ring structure
     * @param start 0-th position in the ring
     * @return
     */
    public int getLinearIndexWithShift(int ringIndex, int start) {
        if (ringIndex < start) {
            return capacity + (ringIndex - start);
        }
        return ringIndex - start;
    }

    public int getRingIndexWithShift(int linearIndex, int start) {
        return (start + linearIndex) % capacity;
    }

    /**
     * Returns ring index if 0-position is start
     * */
}
