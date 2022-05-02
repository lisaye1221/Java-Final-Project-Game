

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("A Simple Journey");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack(); // make window size same as panel

        window.setLocationRelativeTo(null); // display window on center of screen
        window.setVisible(true);
    }
}


class GamePanel extends JPanel{

    // Screen settings
    final int originalTileSize = 16; // 16 x 16 tile
    final int scale = 3; // to scale the tile since our computer resolution is high
    final int tileSize = originalTileSize * scale; // 48 x 48 tile

    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.WHITE);
        setDoubleBuffered(true); // for better rendering performance
    }



}