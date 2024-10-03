package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Grigoriy Emiliyanov
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private static final int DEFAULT_INITIAL_SIZE = 16;
    private static final double DEFAULT_MAX_LOAD = 0.75;
    private static final int RESIZE_FACTOR = 2;

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private HashSet<K> keys = new HashSet<>();
    private int initialSize;
    private double loadFactor;

    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_INITIAL_SIZE, DEFAULT_MAX_LOAD);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_MAX_LOAD);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialSize, double loadFactor) {
        this.initialSize = initialSize;
        this.loadFactor = loadFactor;
        buckets = createTable(this.initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    @Override
    public void clear() {
        buckets = createTable(DEFAULT_INITIAL_SIZE);
        keys.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    @Override
    public V get(K key) {
        Node node = findNode(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    /**
     * Returns the hash corresponding to given key with modulo to the buckets length.
     * AsciiToInt conversion is used.
     * @param key
     * @return hash code
     */
    private int getIndexFromKey(K key) {
        return Math.floorMod(asciiToInt((String)key), buckets.length);
    }

    /**
     * Returns the bucket corresponding to a given key
     * @param key
     * @return bucket
     */
    private Collection<Node> getBucket(K key) {
        int index = getIndexFromKey(key);
        return buckets[index];
    }

    @Override
    public int size() {
        return keys.size();
    }

    @Override
    public void put(K key, V value) {
        if (isOverloaded()) {
            resize(buckets.length * RESIZE_FACTOR);
        }
        Node node = findNode(key);
        if (node != null) {
            node.value = value;
            return;
        }
        Node nodeToPut = createNode(key, value);
        Collection<Node> bucket = getBucket(key);
        bucket.add(nodeToPut);
        keys.add(key);
    }

    /**
     * Returns true if this hash map is overloaded,
     * i.e. size exceeds the current number of buckets * load factor
     * @return boolean
     */
    private boolean isOverloaded() {
        return size() > loadFactor * buckets.length;
    }

    /**
     * Returns the node from this hash map having given key.
     * If the key is not present, then returns null.
     * @param key
     * @return a node if found, otherwise null
     */
    private Node findNode(K key) {
        for (Node node : getBucket(key)) {
            if (key.equals(node.key)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Resizes this hash map buckets table
     * @param size
     */
    private void resize(int size) {
        Collection<Node>[] newBuckets = createTable(size);
        System.arraycopy(buckets, 0, newBuckets, 0, buckets.length);
        buckets = newBuckets;
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }

    @Override
    public V remove(K key) {
        Node node = findNode(key);
        if (node == null) {
            return null;
        }
        keys.remove(key);
        V value = node.value;
        Collection<Node> bucket = getBucket(key);
        bucket.remove(node);
        return value;
    }

    @Override
    public V remove(K key, V value) {
        Node node = findNode(key);
        if (node == null) {
            return null;
        }
        if (!value.equals(node.value)) {
            return null;
        }
        return remove(key);
    }

    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    /**
     * Returns the integer corresponding to given string
     * Taken from Lecture slides: "cs61b 2020 ds5 lec19 hashing"
     * @param s
     * @return hash
     */
    private static int asciiToInt(String s) {
        int intRep = 0;
        for (int i = 0; i < s.length(); i += 1) {
            intRep = intRep * 126;
            intRep = intRep + s.charAt(i);
        }
        return intRep;
    }
}
