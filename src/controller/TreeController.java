package controller;

import model.RedBlackNode;
import model.RedBlackTree;
import model.Result;

public class TreeController {
    public RedBlackTree<Integer, Integer> rbTree = new RedBlackTree<>();
    public boolean firstPlayersTurn = true;

    public int exists(Integer key) {
        RedBlackNode<Integer, Integer> node = rbTree.getNode(key);
        RedBlackNode<Integer, Integer> replace = rbTree.getNode(key + 32);

        if (node == null || node.isNull) {
            if (replace == null || replace.isNull) {
                return 0;
            }

            return -1;
        }

        return node.value;
    }

    int getTreeNodesCount(RedBlackNode<Integer, Integer> node) {
        if (node == null || node.isNull) {
            return 0;
        }

        return getTreeNodesCount(node.right) + getTreeNodesCount(node.left) + 1;
    }

    int getTreeHeight(RedBlackNode<Integer, Integer> node) {
        if (node == null || node.isNull) {
            return 0;
        }

        return Math.max(getTreeHeight(node.left), getTreeHeight(node.right)) + 1;
    }

    Result getGameResult(RedBlackNode<Integer, Integer> node) {
        if (node == null || node.isNull) {
            return new Result(0, 0, 0);
        }

        Result right = getGameResult(node.right);
        Result left = getGameResult(node.left);

        int height = Math.max(right.height, left.height) + 1;
        int penalty1 = right.penalty1 + left.penalty1;
        int penalty2 = right.penalty2 + left.penalty2;

        if (node.value == 1 && !node.isBlack) {
            penalty1 += height * node.key;
        } else if (node.value == 2 && !node.isBlack) {
            penalty2 += height * node.key;
        }

        return new Result(height, penalty1, penalty2);
    }
}