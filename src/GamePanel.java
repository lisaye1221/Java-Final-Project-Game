
// GamePanel

import javax.swing.*;
import java.awt.*;

class GamePanel extends JPanel implements Runnable {

    // Screen settings
    final int originalTileSize = 16; // 16 x 16 tile
    final int scale = 3; // to scale the tile since our computer resolution is high
    final int tileSize = originalTileSize * scale; // 48 x 48 tile

    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    Thread gameThread;

    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.WHITE);
        setDoubleBuffered(true); // for better rendering performance
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    // The game loop
    public void run() {
        while (gameThread != null) {
            // System.out.println("game is running");
            // 1. UPDATE: update information
            UPDATE();
            // 2. DRAW: draw gui and graphics
            repaint();
        }
    }

    public void UPDATE(){

    }

    // DRAW
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // left off at video #2 , time stamp 6:16
    }
}
