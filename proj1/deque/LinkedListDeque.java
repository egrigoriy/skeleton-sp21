package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private final Node<T> sentinel;
    /**
     * Creates an empty linked list deque.
     */
    public LinkedListDeque() {
        sentinel = new Node<T>(null);
        link2Nodes(sentinel, sentinel);
        size = 0;
    }
     /**
      * Same as get, but uses recursion.
      * */
    public T getRecursive(int index) {
        if (size() == 0) {
            return null;
        }
        return getItRecursive(getFirstNode(), index);
    }

    private T getItRecursive(Node<T> node, int index) {
        if (index == 0) {
            return node.item;
        }
        if (node.next == sentinel) {
            return null;
        }
        return getItRecursive(node.next, index - 1);
    }

    @Override
    public void addFirst(T item) {
        Node<T> oldFirst = getFirstNode();
        Node<T> newFirst = new Node<>(item);
        link3Nodes(sentinel, newFirst, oldFirst);
        size++;
    }

    @Override
    public void addLast(T item) {
        Node<T> oldLast = getLastNode();
        Node<T> newLast = new Node<>(item);
        link3Nodes(oldLast, newLast, sentinel);
        size++;
    }

    private Node<T> getLastNode() {
        return sentinel.prev;
    }

    private Node<T> getFirstNode() {
        return sentinel.next;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node<T> cursor = getFirstNode();
        while (cursor != sentinel) {
            System.out.print(cursor.item);
            if (cursor.next != null) {
                System.out.print(" ");
            }
            cursor = cursor.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        Node<T> currentFirst = getFirstNode();
        Node<T> newFirst =  currentFirst.next;
        link2Nodes(sentinel, newFirst);
        size--;
        return  currentFirst.item;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        Node<T> oldLast = getLastNode();
        Node<T> newLast = oldLast.prev;
        link2Nodes(newLast, sentinel);
        size--;
        return oldLast.item;
    }

    private void link2Nodes(Node<T> first, Node<T> second) {
        first.next = second;
        second.prev = first;
    }

    private void link3Nodes(Node<T> first, Node<T> second, Node<T> third) {
        link2Nodes(first, second);
        link2Nodes(second, third);
    }
    @Override
    public T get(int index) {
        Node<T> cursor = getFirstNode();
        int i = 0;
        while (cursor != sentinel) {
            if (i == index) {
                return cursor.item;
            }
            i++;
            cursor = cursor.next;
        }
        return null;
    }

    private class Node<T> {
        private T item;
        private Node<T> prev;
        private Node<T> next;

        private Node(T item) {
            this.item = item;
        }
    }

    /**
     *  Returns whether or not the parameter o is equal to the Deque.
     o is considered equal if it is a Deque and if it contains the same contents
     (as goverened by the generic T’s equals method) in the same order.
     *  (ADDED 2/12: You’ll need to use the instance of keywords for this.
     Read here for more information)
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
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node<T> cursor = getFirstNode();
        @Override
        public boolean hasNext() {
            return cursor != sentinel;
        }

        @Override
        public T next() {
            T nextItem = cursor.item;
            cursor = cursor.next;
            return nextItem;
        }
    }

}
