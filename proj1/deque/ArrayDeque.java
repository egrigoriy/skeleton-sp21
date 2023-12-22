package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private final int EXPAND_FACTOR = 2;
    private final int SHRINK_FACTOR = 4;
    private int size;
    private T[] items;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        int capacity = 8;
        items = (T[]) new Object[capacity];
        nextFirst = capacity / 2;
        nextLast = nextFirst + 1;
        size = 0;
    }

    private void resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];

        for (int i = 0; i < size(); i++) {
            int storageIndex = indexToStorageIndex(i);
            newArray[i] = items[storageIndex];
        }
        nextFirst = capacity - 1;
        nextLast = size;

        items = newArray;
    }
    @Override
    public void addFirst(T item) {
        if (shouldExpand()) {
            resize(items.length * EXPAND_FACTOR);
        }
        items[nextFirst] = item;
        tickNextFirst();
        size++;
    }

    @Override
    public void addLast(T item) {
        if (shouldExpand()) {
            resize(items.length * EXPAND_FACTOR);
        }
        items[nextLast] = item;
        tickNextLast();
        size++;
    }

    @Override
    public T removeFirst() {
        if (size() == 0) {
            return null;
        }

        if (shouldShrink()) {
            resize(items.length / SHRINK_FACTOR);
        }

        int firstIndex = getFirstIndex();
        T first = items[firstIndex];
        items[firstIndex] = null;
        backNextFirst();
        size--;
        return first;
    }

    @Override
    public T removeLast() {
        if (size() == 0) {
            return null;
        }

        if (shouldShrink()) {
            resize(items.length / SHRINK_FACTOR);
        }

        int lastIndex = getLastIndex();
        T last = items[lastIndex];
        items[lastIndex] =  null;
        backNextLast();
        size--;
        return last;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        if ((size() == 0) || (index < 0)) {
            return null;
        }

        return items[indexToStorageIndex(index)];
    }

    private void tickNextFirst() {
        nextFirst = minusOne(nextFirst);
    }

    private void backNextFirst() {
        nextFirst = plusOne(nextFirst);
    }

    private void tickNextLast() {
        nextLast = plusOne(nextLast);
    }

    private void backNextLast() {
        nextLast = minusOne(nextLast);
    }

    private int minusOne(int i) {
        return (i != 0) ? i - 1 : items.length - 1;
    }

    private int plusOne(int i) {
        return (i + 1) % items.length;
    }

    private int getFirstIndex() {
        return plusOne(nextFirst);
    }

    private int getLastIndex() {
        return minusOne(nextLast);
    }

    private int indexToStorageIndex(int index) {
        return (getFirstIndex() + index) % items.length;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size(); i++) {
            System.out.print(get(i));
            if (i < size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    private boolean shouldShrink() {
        return (size <= items.length / 4) && (size > 4);
    }

    private boolean shouldExpand() {
        return size() == items.length;
    }


    /**
     * Returns whether or not the parameter o is equal to the Deque.
     * o is considered equal if it is a Deque and if it contains the same contents
     * (as goverened by the generic T’s equals method) in the same order.
     * (ADDED 2/12: You’ll need to use the instance of keywords for this.
     * Read here for more information)
     * */
    public boolean equals(Object o) {
        if (!(o instanceof Deque)) {
            return false;
        }

        Deque<T> other = (Deque<T>) o;

        if (other == this) {
            return true;
        }

        if (size() != other.size()) {
            return false;
        }

        for (int i = 0; i < size(); i++) {
            if (!get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int i = 0;
        @Override
        public boolean hasNext() {
            return i < size();
        }

        @Override
        public T next() {
            T nextItem = get(i);
            i++;
            return nextItem;
        }
    }
}
