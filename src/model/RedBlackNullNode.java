package model;

public class RedBlackNullNode<K extends Comparable<K>, V> extends RedBlackNode<K, V> {
    public RedBlackNullNode(RedBlackNode<K, V> parent) {
        super(parent);
    }
}