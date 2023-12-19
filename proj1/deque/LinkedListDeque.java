package deque;
public class LinkedListDeque<T> implements Deque<T> {
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
        return null;
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
    public boolean isEmpty() {
        return size == 0;
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
        if (size == 0) return null;

        Node<T> currentFirst = getFirstNode();
        Node<T> newFirst =  currentFirst.next;
        link2Nodes(sentinel, newFirst);
        size--;
        return  currentFirst.item;
    }

    @Override
    public T removeLast() {
        if (size == 0) return null;

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
            if (i == 0) {
                return cursor.item;
            }
            i++;
            cursor = cursor.next;
        }
        return null;
    }

    private class Node<T> {
        public T item;
        public Node<T> prev;
        public Node<T> next;

         public Node(T item) {
            this.item = item;
         }
    }

}
