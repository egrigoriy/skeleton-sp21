package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private Ring ring;

    public ArrayDeque() {
        int capacity = 8;
        items = (T[]) new Object[capacity];
        size = 0;
        ring = new Ring(capacity);
    }

    private void resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];

        for (int i = 0; i < size; i++) {
            int ringIndex = ring.getRingIndexWithShift(i, ring.getFirstIndex());
            newArray[i] = items[ringIndex];
        }
        ring = new Ring(capacity);
        ring.nextFirst = ring.getPrev(0);
        ring.nextLast = ring.getNext(size - 1);

        items = newArray;
    }
    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[ring.getNextFirst()] = item;
        ring.tickNextFirst();
        size++;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[ring.getNextLast()] = item;
        ring.tickNextLast();
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i));
            if (i < size - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        if ((size < items.length / 4) && (size > 8)) {
            resize(size);
        }

        int firstIndex = ring.getFirstIndex();
        T first = items[firstIndex];
        items[firstIndex] = null;
        ring.nextFirst = firstIndex;
        size--;
        return first;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        if ((size < items.length / 4) && (size > 4)) {
            resize(size);
        }

        int lastIndex = ring.getLastIndex();
        T last = items[lastIndex];
        items[lastIndex] =  null;
        ring.nextLast = lastIndex;
        size--;
        return last;
    }

    @Override
    public T get(int index) {
        if ((size == 0) || (index < 0)) {
            return null;
        }

        return items[ring.linearToStorageIndex(index)];
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
            return i < size;
        }

        @Override
        public T next() {
            T nextItem = get(i);
            i++;
            return nextItem;
        }
    }
}
