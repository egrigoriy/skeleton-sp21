package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;

    public ArrayDeque() {
        items = (T[])new Object[8];
        size = 0;
    }

    private void resize(int capacity) {
        T[] newArray = (T[])new Object[capacity];
        System.arraycopy(items, 0, newArray, 0, size);
        items = newArray;
    }
    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        System.arraycopy(items, 0, items, 1, size);
        items[0] = item;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[size] = item;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(items[i]);
            if (i < size - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) return null;

        if ((size < items.length / 8) && (size > 8)) {
            resize(size);
        }

        T first = items[0];
        size--;
        System.arraycopy(items, 1, items, 0, size);
        return first;
    }

    @Override
    public T removeLast() {
        if (size == 0) return null;

        if ((size < items.length / 8) && (size > 8)) {
            resize(size);
        }

        T last = items[size - 1];
        size--;
        items[size] = null;
        return last;
    }

    @Override
    public T get(int index) {
        if ((size == 0) || (index < 0)) return null;
        return items[index];
    }


}
