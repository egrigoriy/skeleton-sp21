package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;
    /**
     * Creates a MaxArrayDeque with the given Comparator.
     *
     * @param c is a Comparator
     */
    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    /**
     * Returns the maximum element in the deque as governed by the previously given Comparator.
     * If the MaxArrayDeque is empty, simply return null.
     *
     * @return maximum element in the deque
     */
    public T max() {
        return max(comparator);
    }

    /**
     *  Returns the maximum element in the deque as governed by the parameter Comparator c.
     *  If the MaxArrayDeque is empty, simply return null.
     * @param c is a Comparator
     * @return maximum element in the deque
     */
    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        T maxItem = this.get(0);
        for (T item : this) {
            if (c.compare(maxItem, item) < 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }
}
