package controller;

import constant.Constants;
import model.RedBlackTree;
import model.Result;

public class BotController {
    public static int bestMove(RedBlackTree<Integer, Integer> rbTree) {

        int bot = Constants.BOT_PLAYER;
        int rival = Constants.BOT_PLAYER % 2 + 1;

        int bestMove = 0;
        int bestMid = 1000000;

        for (int i = 1; i <= 32; i++) {
            int cnt = 0;
            int sum = 0;

            TreeController treeController1 = new TreeController();
            treeController1.rbTree = rbTree.copy();
            treeController1.firstPlayersTurn = bot == 1;

            if (isMoveCompleted(treeController1, i, bot)) {
                for (int j = 1; j <= 32; j++) {
                    TreeController treeController2 = new TreeController();
                    treeController2.rbTree = treeController1.rbTree.copy();
                    treeController2.firstPlayersTurn = rival == 1;

                    if (isMoveCompleted(treeController2, j, rival)) {
                        for (int k = 1; k <= 32; k++) {
                            TreeController treeController3 = new TreeController();
                            treeController3.rbTree = treeController2.rbTree.copy();
                            treeController3.firstPlayersTurn = bot == 1;

                            if (isMoveCompleted(treeController3, k, bot)) {
                                for (int l = 1; l <= 32; l++) {
                                    TreeController treeController4 = new TreeController();
                                    treeController4.rbTree = treeController3.rbTree.copy();
                                    treeController4.firstPlayersTurn = rival == 1;

                                    if (isMoveCompleted(treeController4, l, rival)) {
                                        Result result = treeController4.getGameResult(treeController4.rbTree.root);
                                        int difference = bot == 1 ? result.penalty2 - result.penalty1 : result.penalty1 - result.penalty2;
                                        sum += difference;
                                        cnt++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (cnt != 0) {
                int mid = sum / cnt;
                if (mid < bestMid) {
                    bestMid = mid;
                    bestMove = i;
                }
            }
        }

        return bestMove;
    }

    private static boolean isMoveCompleted(TreeController treeController, int number, int player) {
        if (number < 1 || number > 32) {
            return false;
        } else if (treeController.exists(number) == -1) {
            return false;
        } else if (treeController.exists(number) == 0) {
            treeController.rbTree.add(number, player);
            treeController.firstPlayersTurn = !treeController.firstPlayersTurn;
            return true;
        } else if (treeController.exists(number) == player) {
            treeController.rbTree.delete(number);
            treeController.rbTree.add(number + 32, player);
            treeController.firstPlayersTurn = !treeController.firstPlayersTurn;
            return true;
        }
        return false;
    }
}
