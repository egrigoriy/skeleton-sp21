package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
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

        @Override
        public int hashCode() {
            return key.hashCode() + value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
//            if (obj instanceof Node other) {
//                return ((this.key == other.key) && (this.value == this.value));
//            }
            return false;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int initialSize;
    private double maxLoad;
    private HashSet<K> keys = new HashSet<>();

    /** Constructors */
    public MyHashMap() {
        this.initialSize = 16;
        this.maxLoad = 0.75;
        buckets = createTable(this.initialSize);
    }

    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        this.maxLoad = 0.75;
        buckets = createTable(this.initialSize);

    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.maxLoad = maxLoad;
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
        MyHashMap<K, V> bb = new MyHashMapLLBuckets<>(initialSize, maxLoad);
        return bb.createBucket();
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
        Collection<Node>[] result = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            result[i] = createBucket();
        }
        return result;
    }

    @Override
    public void clear() {
        buckets = createTable(keys.size());
        keys.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    @Override
    public V get(K key) {
        int index = Math.floorMod(asciiToInt((String)key), buckets.length);
        Collection<Node> bucket = buckets[index];
        for (Node node : bucket) {
            if (key.equals(node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return keys.size();
    }

    @Override
    public void put(K key, V value) {
        keys.add(key);
        int index = Math.floorMod(asciiToInt((String)key), buckets.length);
        if (index > buckets.length) {
            resize(index);
        }
        Collection<Node> bucket = buckets[index];
        for (Node node : bucket) {
            if (key.equals(node.key)) {
                node.value = value;
                return;
            }
        }
        Node nodeToPut = createNode(key, value);
        bucket.add(nodeToPut);
    }

    private void resize(int size) {
        Collection<Node>[] newBuckets = createTable(size * 2);
        System.arraycopy(buckets, 0, newBuckets, 0, buckets.length);
        buckets = newBuckets;
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }

    @Override
    public V remove(K key) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    public static int asciiToInt(String s) {
        int intRep = 0;
        for (int i = 0; i < s.length(); i += 1) {
            intRep = intRep * 126;
            intRep = intRep + s.charAt(i);
        }
        return intRep;
    }

}
