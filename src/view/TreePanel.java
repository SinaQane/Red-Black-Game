package view;

import model.RedBlackNode;
import model.RedBlackNullNode;
import model.RedBlackTree;

import javax.swing.*;
import java.awt.*;

public class TreePanel extends JPanel {

    private final RedBlackTree<Integer, Integer> rbTree;

    private static final int Y_OFFSET = 50;
    private static final int RADIUS = 20;

    private boolean showLeaves = true;

    public TreePanel(RedBlackTree<Integer, Integer> rbTree) {
        this.rbTree = rbTree;
    }

    public void setShowLeaves(boolean b) {
        showLeaves = b;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (rbTree.root == null || rbTree.root.isNull) {
            return;
        }

        Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setFont(new Font("TimesRoman", Font.PLAIN, 11));
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int depth = rbTree.getDepth();
        int multiplier = 30;
        float exponent = (float) 1.6;

        if (depth >= 7) {
            multiplier += depth * 3;
            exponent += 0.1;
        }

        int gap = (int) Math.pow(depth, exponent) * multiplier;

        paintTree(graphics2d, rbTree.root, getWidth() / 2, 30, gap);
    }

    private void paintTree(Graphics2D g, RedBlackNode<Integer, Integer> root, int x, int y, int xOffset) {
        if (x < 0) {
            setPreferredSize(new Dimension(2 * getWidth(), getHeight()));
        }

        drawNode(g, root, x, y);

        if (root.left != null && !root.left.isNull) {
            join(g, x - xOffset, y + Y_OFFSET, x, y);
            paintTree(g, root.left, x - xOffset, y + Y_OFFSET, xOffset / 2);
        } else {
            if (showLeaves) {
                join(g, x - xOffset, y + Y_OFFSET, x, y);
                drawNode(g, new RedBlackNullNode<>(null), x - xOffset, y + Y_OFFSET);
            }
        }

        if (root.right != null && !root.right.isNull) {
            join(g, x + xOffset, y + Y_OFFSET, x, y);
            paintTree(g, root.right, x + xOffset, y + Y_OFFSET, xOffset / 2);
        } else {
            if (showLeaves) {
                join(g, x + xOffset, y + Y_OFFSET, x, y);
                drawNode(g, new RedBlackNullNode<>(null), x + xOffset, y + Y_OFFSET);
            }
        }
    }

    private void drawNode(Graphics2D g, RedBlackNode<Integer, Integer> node, int x, int y) {
        g.setColor(node.isBlack ? new Color(70, 70, 70) : new Color(250, 70, 70));
        g.fillOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
        g.setColor(new Color(230, 230, 230));

        String text = node.isNull ? "null" : node.key + " - (" + node.value + ")";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (int) (x - fm.getStringBounds(text, g).getWidth() / 2), (y + fm.getMaxAscent() / 2));
        g.setColor(Color.GRAY);
    }

    private void join(Graphics2D g, int x1, int y1, int x2, int y2) {
        double hypot = Math.hypot(Y_OFFSET, x2 - x1);
        int x11 = (int) (x1 + RADIUS * (x2 - x1) / hypot);
        int y11 = (int) (y1 - RADIUS * Y_OFFSET / hypot);
        int x21 = (int) (x2 - RADIUS * (x2 - x1) / hypot);
        int y21 = (int) (y2 + RADIUS * Y_OFFSET / hypot);
        g.drawLine(x11, y11, x21, y21);
    }
}
