
// main.GamePanel

package main;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // Screen settings
    final int originalTileSize = 16; // 16 x 16 tile
    final int scale = 3; // to scale the tile since our computer resolution is high
    final int tileSize = originalTileSize * scale; // 48 x 48 tile

    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    // set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.WHITE);
        setDoubleBuffered(true); // for better rendering performance
        this.addKeyListener(keyHandler);
        this.setFocusable(true); // allow to be panel to be focused to get key input
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    // The game loop
    public void run() {

        // 1/60 sec
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                // 1. UPDATE: update information
                UPDATE();
                // 2. DRAW: draw gui and graphics
                repaint();
                delta--;
            }

        }
    }

    public void UPDATE(){

        // update player position
        if(keyHandler.upPressed) {
            playerY -= playerSpeed;
        }
        else if(keyHandler.downPressed){
            playerY += playerSpeed;
        }
        else if(keyHandler.rightPressed){
            playerX += playerSpeed;
        }
        else if(keyHandler.leftPressed){
            playerX -= playerSpeed;
        }



    }

    // DRAW
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.black);
        g2.fillRect(playerX,playerY, tileSize, tileSize);

        // when drawing is done, dispose to save memory
        g2.dispose();

    }
}
