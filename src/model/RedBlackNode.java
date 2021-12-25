package model;

public class RedBlackNode<K extends Comparable<K>, V> {
    public K key;
    public V value;
    public boolean isBlack, isNull;
    public RedBlackNode<K, V> left, right, parent;

    public RedBlackNode(K key, V value) {
        this.key = key;
        this.value = value;
        isBlack = isNull = false;
        left = new RedBlackNullNode<>(this);
        right = new RedBlackNullNode<>(this);
        parent = new RedBlackNullNode<>(null);
    }

    public RedBlackNode(RedBlackNode<K, V> parent) {
        this.parent = parent;
        isBlack = isNull = true;
    }
}
