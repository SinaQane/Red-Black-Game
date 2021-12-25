import controller.GameController;
import view.GamePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        startTheGame(new JFrame());
    }

    public static void startTheGame(JFrame jFrame) {
        jFrame.setTitle("Red Black Game");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(new GamePanel(new GameController()));
        jFrame.setVisible(true);
        jFrame.pack();
    }
}

