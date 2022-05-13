
// Lisa Ye
// A Simple Journey
// main.Main class: run this class to start the game

package main;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("A Simple Journey");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack(); // make window size same as panel

        gamePanel.startGameThread();

        window.setLocationRelativeTo(null); // display window on center of screen
        window.setVisible(true);
    }
}


