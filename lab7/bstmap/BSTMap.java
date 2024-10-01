package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private int size = 0;
    private BSTNode root = null;

    private class BSTNode {
        public K key;
        public V value;
        public BSTNode left;
        public BSTNode right;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }

    private boolean containsKey(BSTNode node, K key) {
        if (node == null) {
            return false;
        }
        return node.key.equals(key) || containsKey(node.left, key) || containsKey(node.right, key);
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            return get(node.left, key);
        }
        if (key.compareTo(node.key) > 0) {
            return get(node.right, key);
        }
        return node.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
        size++;
    }

    private BSTNode put(BSTNode node, K key, V value) {
        if (node == null) {
            return new BSTNode(key, value);
        }
        if (key.compareTo(node.key) < 0) {
            node.left = put(node.left, key, value);
        }
        else if (key.compareTo(node.key) > 0) {
            node.right = put(node.right, key, value);
        }
        return node;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<K>();
        keyset(root, keys);
        return keys;
    }

    private void keyset(BSTNode node, Set<K> keys) {
        if (node == null) {
            return;
        }
        keys.add(node.key);
        keyset(node.left, keys);
        keyset(node.right, keys);
    }

    @Override
    public V remove(K key) {
        V value = get(key);
        if (value == null) {
            return null;
        }
        root = remove(root, key);
        size--;
        return value;
    }

    private BSTNode predecessor(BSTNode node) {
        BSTNode current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }

    private BSTNode remove(BSTNode node, K key) {
        if (isLeaf(node)) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            node.left = remove(node.left, key);
        }
        if (key.compareTo(node.key) > 0) {
            node.right = remove(node.right, key);
        }
        if (key.compareTo(node.key) == 0) {
            if ((node.left == null) && (node.right != null)) {
                node = node.right;
            }
            if ((node.left != null) && (node.right == null)) {
                node = node.left;
            }
            if ((node.left != null) && (node.right != null)) {
                BSTNode predecessorNode = predecessor(node.left);
                node.left = remove(node.left, predecessorNode.key);
                predecessorNode.left = node.left;
                predecessorNode.right = node.right;
                node = predecessorNode;
            }
        }
        return node;
    }

    private boolean isLeaf(BSTNode node) {
        return (node.left == null) && (node.right == null);
    }

    private boolean oneChild(BSTNode node) {
        return ((node.left == null) && (node.right != null))
                || ((node.left != null) && (node.right == null));
    }


    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {

    }
}
