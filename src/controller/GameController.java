package controller;

import constant.Constants;
import model.RedBlackTree;
import model.Result;

public class GameController {
    public TreeController treeController = new TreeController();

    public GameController() {
        if (Constants.BOT_MODE && Constants.BOT_PLAYER == 1) {
            isMoveCompleted(1, Constants.BOT_PLAYER);
        }
    }

    public boolean isFirstPlayersTurn() {
        return treeController.firstPlayersTurn;
    }

    public boolean isMoveCompleted(Integer number, Integer player) {
        if (number < 1 || number > 32) {
            return false;
        } else if (treeController.exists(number) == -1) {
            return false;
        } else if (treeController.exists(number) == 0) {
            treeController.rbTree.add(number, player);
            treeController.firstPlayersTurn = !treeController.firstPlayersTurn;

            if (Constants.BOT_MODE && player != Constants.BOT_PLAYER) {
                int bestMove = BotController.bestMove(treeController.rbTree);
                isMoveCompleted(bestMove, Constants.BOT_PLAYER);
            }

            return true;
        } else if (treeController.exists(number) == player) {
            treeController.rbTree.delete(number);
            treeController.rbTree.add(number + 32, player);
            treeController.firstPlayersTurn = !treeController.firstPlayersTurn;

            if (Constants.BOT_MODE && player != Constants.BOT_PLAYER) {
                int bestMove = BotController.bestMove(treeController.rbTree);
                isMoveCompleted(bestMove, Constants.BOT_PLAYER);
            }

            return true;
        }

        return false;
    }

    public boolean isGameFinished() {
        return treeController.getTreeNodesCount(treeController.rbTree.root) >= 32 || treeController.getTreeHeight(treeController.rbTree.root) >= 7;
    }

    public int getWinner() {
        Result result = treeController.getGameResult(treeController.rbTree.root);

        return result.penalty1 == result.penalty2 ? 0 : result.penalty1 < result.penalty2 ? 1 : 2;
    }

    public int getFirstPlayersPenalty() {
        return treeController.getGameResult(treeController.rbTree.root).penalty1;
    }

    public int getSecondPlayersPenalty() {
        return treeController.getGameResult(treeController.rbTree.root).penalty2;
    }

    public RedBlackTree<Integer, Integer> getTree() {
        return treeController.rbTree;
    }
}
