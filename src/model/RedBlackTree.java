package model;

public class RedBlackTree<K extends Comparable<K>, V> {
    public RedBlackNode<K, V> root;
    public int size;

    public RedBlackTree<K, V> copy() {
        RedBlackTree<K, V> clone = new RedBlackTree<>();
        clone.root = new RedBlackNode<>(null);
        clone.root.parent = root.parent;
        clone.root.key = root.key;
        clone.root.value = root.value;
        clone.root.isBlack = root.isBlack;
        clone.root.isNull = root.isNull;

        copy(new RedBlackNullNode<>(null), clone.root, root);
        return clone;
    }

    private void copy(RedBlackNode<K,V> parent, RedBlackNode<K,V> clone, RedBlackNode<K,V> original) {
        clone.parent = parent;
        clone.key = original.key;
        clone.value = original.value;
        clone.isBlack = original.isBlack;
        clone.isNull = original.isNull;
        clone.left = new RedBlackNode<>(clone);
        clone.right = new RedBlackNode<>(clone);

        if (original.left != null && !original.left.isNull) {
            copy(clone, clone.left, original.left);
        }
        if (original.right != null && !original.right.isNull) {
            copy(clone, clone.right, original.right);
        }
    }

    public int getDepth() {
        return this.getDepth(root);
    }

    private int getDepth(RedBlackNode<K, V> node) {
        if (node == null || node.isNull) {
            return 0;
        }

        return Math.max(getDepth(node.left), getDepth(node.right)) + 1;
    }

    public RedBlackNode<K, V> getNode(K key) {
        RedBlackNode<K, V> node = root;

        while (node != null && !node.isNull) {
            if (key == node.key) {
                return node;
            } else if (key.compareTo(node.key) > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        return null;
    }

    public void add(K key, V value) {
        add(new RedBlackNode<>(key, value));
        size++;
    }

    private void add(RedBlackNode<K, V> node) {
        RedBlackNode<K, V> search = root;
        RedBlackNode<K, V> parent = new RedBlackNullNode<>(null);

        while (search != null && !search.isNull) {
            parent = search;

            if (node.key.compareTo(search.key) > 0) {
                search = search.right;
            } else if (node.key.compareTo(search.key) < 0) {
                search = search.left;
            }
        }

        if (parent.isNull) {
            root = node;
        } else if (node.key.compareTo(parent.key) < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }

        node.parent = parent;

        addFixUp(node);

        root.isBlack = true;
    }

    private void addFixUp(RedBlackNode<K, V> node) {
        while (node != root && !node.parent.isBlack) {
            RedBlackNode<K, V> uncle = getUncle(node.parent);

            if (!uncle.isBlack) {
                node.parent.parent.isBlack = false;
                node.parent.isBlack = true;
                uncle.isBlack = true;
                node = node.parent.parent;
            } else {
                if (node.parent.parent != null && !node.parent.parent.isNull) {
                    if (node.parent == node.parent.parent.left) {
                        if (node == node.parent.right) {
                            node = node.parent;
                            leftRotate(node);
                        }

                        node.parent.parent.isBlack = false;
                        rightRotate(node.parent.parent);
                    } else {
                        if (node == node.parent.left) {
                            node = node.parent;
                            rightRotate(node);
                        }

                        node.parent.parent.isBlack = false;
                        leftRotate(node.parent.parent);
                    }
                }

                node.parent.isBlack = true;
            }
        }

        root.isBlack = true;
    }

    private RedBlackNode<K, V> getUncle(RedBlackNode<K, V> parent) {
        if (parent == null || parent.parent == null) {
            return new RedBlackNullNode<>(null);
        }

        return parent == parent.parent.left ? parent.parent.right : parent.parent.left;
    }

    private void leftRotate(RedBlackNode<K, V> node) {
        RedBlackNode<K, V> temp = node.parent;
        RedBlackNode<K, V> right = node.right;
        node.right = right.left;

        if (right.left != null && !right.left.isNull) {
            right.left.parent = node;
        }

        right.left = node;
        node.parent = right;

        if (temp == null || temp.isNull) {
            root = right;
        } else if (temp.left == node) {
            temp.left = right;
        } else if (temp.right == node) {
            temp.right = right;
        }

        if (!right.isNull) {
            right.parent = temp;
        }

        root.isBlack = true;
    }

    private void rightRotate(RedBlackNode<K, V> node) {
        RedBlackNode<K, V> parent = node.parent;
        RedBlackNode<K, V> left = node.left;
        node.left = left.right;

        if (left.right != null && !left.right.isNull) {
            left.right.parent = node;
        }

        left.right = node;
        node.parent = left;

        if (parent == null || parent.isNull) {
            root = left;
        } else if (parent.left == node) {
            parent.left = left;
        } else if (parent.right == node) {
            parent.right = left;
        }

        if (!left.isNull) {
            left.parent = parent;
        }

        root.isBlack = true;
    }

    public void delete(K key) {
        RedBlackNode<K, V> node = getNode(key);

        if (node != null && !node.isNull) {
            size--;
            delete(node);
        }
    }

    private void delete(RedBlackNode<K, V> node) {
        RedBlackNode<K, V> successor;
        RedBlackNode<K, V> replacement;

        if (node.left == null || node.left.isNull || node.right == null || node.right.isNull) {
            successor = node;
        } else {
            successor = treeSuccessor(node);
        }

        if (successor.left != null && !successor.left.isNull) {
            replacement = successor.left;
        } else {
            replacement = successor.right;
        }

        replacement.parent = successor.parent;

        if (successor.parent == null || successor.parent.isNull) {
            root = replacement;
        } else {
            if (successor == successor.parent.left) {
                successor.parent.left = replacement;
            } else {
                successor.parent.right = replacement;
            }
        }

        if (successor != node) {
            node.key = successor.key;
            node.value = successor.value;
        }

        if (successor.isBlack) {
            deleteFixUp(replacement);
        }

        root.isBlack = true;
    }

    private void deleteFixUp(RedBlackNode<K, V> node) {
        while (node != root && node.isBlack) {
            RedBlackNode<K, V> sibling;

            if (node == node.parent.left) {
                sibling = node.parent.right;

                if (sibling == null || sibling.isNull) {
                    node = node.parent;
                } else {
                    if (!sibling.isBlack) {
                        sibling.isBlack = true;
                        node.parent.isBlack = false;
                        leftRotate(node.parent);
                        sibling = node.parent.right;
                    }

                    if (sibling.left.isBlack && sibling.right.isBlack) {
                        sibling.isBlack = false;
                        node = node.parent;
                    } else {
                        if (sibling.right.isBlack) {
                            sibling.left.isBlack = true;
                            sibling.isBlack = false;
                            rightRotate(sibling);
                            sibling = node.parent.right;
                        }

                        sibling.isBlack = node.parent.isBlack;
                        node.parent.isBlack = true;
                        sibling.right.isBlack = true;
                        leftRotate(node.parent);
                        node = root;
                    }
                }
            } else {
                sibling = node.parent.left;

                if (sibling == null || sibling.isNull) {
                    node = node.parent;
                } else {
                    if (!sibling.isBlack) {
                        sibling.isBlack = true;
                        node.parent.isBlack = false;
                        rightRotate(node.parent);
                        sibling = node.parent.left;
                    }

                    if (sibling.right.isBlack && sibling.left.isBlack) {
                        sibling.isBlack = false;
                        node = node.parent;
                    } else {
                        if (sibling.left.isBlack) {
                            sibling.right.isBlack = true;
                            sibling.isBlack = false;
                            leftRotate(sibling);
                            sibling = node.parent.left;
                        }

                        sibling.isBlack = node.parent.isBlack;
                        node.parent.isBlack = true;
                        sibling.left.isBlack = true;
                        rightRotate(node.parent);
                        node = root;
                    }
                }
            }
        }

        node.isBlack = true;
    }

    private RedBlackNode<K, V> treeSuccessor(RedBlackNode<K, V> node) {
        if (node.right != null && !node.right.isNull) {
            RedBlackNode<K, V> right = node.right;

            while (right.left != null && !right.left.isNull) {
                right = right.left;
            }

            return right;
        }

        RedBlackNode<K, V> temp = node.parent;

        while (temp != null && !temp.isNull && node == temp.right) {
            node = temp;
            temp = temp.parent;
        }

        return temp;
    }
}