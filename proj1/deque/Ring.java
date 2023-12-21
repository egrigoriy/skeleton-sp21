package deque;

public class Ring {
    private int size;

    public Ring(int size) {
        this.size = size;
    }

    public int getNext(int i) {
        return (i + 1) % size;
    }

    public int getPrev(int i) {
        i = i - 1;
        if (i == - 1) {
            i = size - 1;
        }
        return i;
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
            return size + (ringIndex - start);
        }
        return ringIndex - start;
    }

    public int getRingIndexWithShift(int linearIndex, int start) {
        return (start + linearIndex) % size;
    }

    /**
     * Returns ring index if 0-position is start
     * */
}
