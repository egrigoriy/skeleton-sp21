package deque;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;

    public ArrayDeque() {
        items = (T[])new Object[8];
        size = 0;
    }
    @Override
    public void addFirst(T item) {
        System.arraycopy(items, 0, items, 1, size);
        items[0] = item;
        size++;
    }

    @Override
    public void addLast(T item) {
        items[size] = item;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {

    }

    @Override
    public T removeFirst() {
        if (size == 0) return null;

        T first = items[0];
        size--;
        System.arraycopy(items, 1, items, 0, size);
        return first;
    }

    @Override
    public T removeLast() {
        if (size == 0) return null;

        T last = items[size - 1];
        size--;
        items[size] = null;
        return last;
    }

    @Override
    public T get(int index) {
        if (size == 0) return null;

        return items[index -1];
    }
}
