package view;

import controller.GameController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GamePanel extends JPanel {

    private TreePanel treePanel;
    private GameController controller;


    private final JLabel gameInfoLabel = new JLabel();
    private final JLabel playerOneLabel = new JLabel();
    private final JLabel playerTwoLabel = new JLabel();

    public GamePanel(GameController gameController) {
        controller = gameController;
        treePanel = new TreePanel(controller.getTree());
        treePanel.setBackground(Color.WHITE);
        initialize();
    }

    private void initialize() {
        super.setLayout(new BorderLayout());
        setTopPanel();
        setBottomPanel();
        setMainPane();
    }

    private void setTopPanel() {
        JPanel panel = new JPanel();
        gameInfoLabel.setText(String.format("Red Black Game - Player %d's turn", controller.isFirstPlayersTurn() ? 1 : 2));
        playerOneLabel.setText(String.format("Player 1: %d                    ", controller.getFirstPlayersPenalty()));
        playerTwoLabel.setText(String.format("                    Player 2: %d", controller.getSecondPlayersPenalty()));
        gameInfoLabel.setForeground(new Color(50, 50, 50));
        playerOneLabel.setForeground(new Color(20, 60, 250));
        playerTwoLabel.setForeground(new Color(250, 60, 20));
        gameInfoLabel.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        playerOneLabel.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        playerTwoLabel.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        panel.setBackground(new Color(230, 230, 230));
        panel.add(playerOneLabel, BorderLayout.WEST);
        panel.add(gameInfoLabel, BorderLayout.NORTH);
        panel.add(playerTwoLabel, BorderLayout.EAST);
        panel.setBorder(new EmptyBorder(10, 5, 10, 5));
        add(panel, BorderLayout.NORTH);
    }

    private void setBottomPanel() {
        JTextField textField = new JTextField(15);

        JButton moveButton = new JButton();
        moveButton.setText("Move");
        moveButton.setFocusPainted(true);
        moveButton.setBorderPainted(true);
        moveButton.setContentAreaFilled(false);

        JCheckBox checkBox = new JCheckBox("Show null leaves", true);
        checkBox.setBounds(100,150, 50,50);

        JPanel panel = new JPanel();
        panel.add(textField);
        panel.add(moveButton);
        panel.add(checkBox);
        panel.setBackground(new Color(230, 230, 230));
        add(panel, BorderLayout.SOUTH);

        moveButton.addActionListener(actionEvent -> {
            if (textField.getText().equals("")) {
                return;
            }

            Integer number = Integer.parseInt(textField.getText());
            Integer player = controller.isFirstPlayersTurn() ? 1 : 2;

            if (!controller.isMoveCompleted(number, player)) {
                JOptionPane.showMessageDialog(null, String.format("Move for player %d is not valid", player));
            } else {
                treePanel.repaint();
                textField.requestFocus();
                textField.selectAll();
            }

            if (controller.isGameFinished()) {
                if (controller.isMoveCompleted(number, player)) {
                    int winner = controller.getWinner();
                    int penalty1 = controller.getFirstPlayersPenalty();
                    int penalty2 = controller.getSecondPlayersPenalty();

                    String message = "";

                    switch (winner) {
                        case 0 -> message = String.format("Tie! Player 1 and Player 2 both have the same penalty %d", penalty1);
                        case 1 -> message = String.format("Game Over! Player 1 is the winner with %d penalty against player 2 with %d penalty", penalty1, penalty2);
                        case 2 -> message = String.format("Game Over! Player 2 is the winner with %d penalty against player 1 with %d penalty", penalty2, penalty1);
                    }

                    JOptionPane.showMessageDialog(null, message);

                    controller = new GameController();
                    treePanel = new TreePanel(controller.getTree());
                    treePanel.setBackground(Color.WHITE);
                    initialize();
                    treePanel.repaint();
                    // repaint();
                }
            }

            gameInfoLabel.setText(String.format("Red Black Game - Player %d's turn", controller.isFirstPlayersTurn() ? 1 : 2));
            playerOneLabel.setText(String.format("Player 1: %d                    ", controller.getFirstPlayersPenalty()));
            playerTwoLabel.setText(String.format("                    Player 2: %d", controller.getSecondPlayersPenalty()));
        });

        checkBox.addActionListener(actionEvent -> {
            treePanel.setShowLeaves(checkBox.isSelected());
            treePanel.repaint();
        });

        textField.addActionListener(actionEvent -> moveButton.doClick());
        textField.addActionListener(actionEvent -> checkBox.doClick());
    }

    private void setMainPane() {
        treePanel.setPreferredSize(new Dimension(3840, 2160));
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(treePanel);
        scroll.setPreferredSize(new Dimension(1280, 720));
        scroll.getViewport().setViewPosition(new Point((3840 - 1280) / 2, 0));
        add(scroll, BorderLayout.CENTER);
    }
}
