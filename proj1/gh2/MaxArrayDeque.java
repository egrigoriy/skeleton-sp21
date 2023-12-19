package gh2;

import deque.ArrayDeque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    /**
     * Creates a MaxArrayDeque with the given Comparator.
     * @param c
     */
    public MaxArrayDeque(Comparator<T> c) {

    }

    /**
     * Returns the maximum element in the deque as governed by the previously given Comparator.
     * If the MaxArrayDeque is empty, simply return null.
     *
     * @return
     */
    public T max() {
        return null;
    }

    /**
     *  Returns the maximum element in the deque as governed by the parameter Comparator c.
     *  If the MaxArrayDeque is empty, simply return null.
     * @param c
     * @return
     */
    public T max(Comparator<T> c) {
        return null;
    }
}
