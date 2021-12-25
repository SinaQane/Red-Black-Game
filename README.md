# Red-Black-Game
A simple game that uses an implementation of Red Black Tree with a graphical interface.

In each move, a player can do one of the following things:
1. Insert a node with the number between 1 and 32,
2. Remove a node that he/she inserted it before with the number `k` and add a node with the number `k + 32`.

The game is finished when the hieght of the tree reaches 7 or all the nodes from 1 to 32 have been inserted at least once.

The score of each player is the negative value of penalty, which is the sum of the number of their red nodes, multiplied by that node's height. The winner is the player with the highest score (lowest penalty).
