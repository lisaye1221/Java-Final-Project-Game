
// main.GamePanel

package main;
import entity.Entity;
import entity.Player;
import graphicObject.Ground;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class GamePanel extends JPanel implements Runnable {
    public enum GameState{
        TITLE, GAME_PLAY, GAME_OVER
    }

    // Screen settings
    final JFrame ownerJF;
    final int originalTileSize = 16; // 16 x 16 tile
    final int scale = 3; // to scale the tile since our computer resolution is high
    final int tileSize = originalTileSize * scale; // 48 x 48 tile

    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    int FPS = 60;
    private Thread gameThread;
    private final int GROUND_SCROLL_SPEED = 2;

    // graphics
    private TileManager tileManager = new TileManager(this);
    private Ground groundGraphics = new Ground(this);

    // Game Logic Managers
    KeyHandler keyHandler = new KeyHandler(this);
    DatabaseManager databaseManager = new DatabaseManager();
    EncounterManager encounterManager = new EncounterManager(this);
    UI ui = new UI(this);

    // player
    public final int PLAYER_X = 100;
    Player player = new Player(this, keyHandler);

    // game data
    private String username = null;
    private String PIN = null;
    public int groundScrollSpeed = GROUND_SCROLL_SPEED;
    public boolean isPaused = false;
    public GameState gameState;
    // game stats
    public double timer = 0;
    public int gold = 0;
    public int hunger = 100;

    public GamePanel(JFrame jf) {
        this.ownerJF = jf;
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(new Color(145,240,255));
        setDoubleBuffered(true); // for better rendering performance
        this.addKeyListener(keyHandler);
        this.setFocusable(true); // allow to be panel to be focused to get key input
    }

    private void setupGame(){
        gameState = GameState.TITLE;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        setupGame();
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
            // increment all the timers
            timer += ((currentTime - lastTime) / drawInterval) / FPS;
            if(encounterManager.shouldSpawnEncounter){
                encounterManager.timer += ((currentTime - lastTime) / drawInterval) / FPS;
            }
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

        if(gameState == GameState.GAME_PLAY) {
            groundGraphics.update();
            encounterManager.update();
            player.update();

            // press X to unpause game(for testing)
            if (keyHandler.cancelPressed) {
                encounterManager.hasPlayerInteracted = true;
                encounterManager.shouldSpawnEncounter = true;
                isPaused = false;

                player.direction = Entity.Direction.RIGHT;
            }

            if (encounterManager.xForCollisionDetection <= PLAYER_X && !encounterManager.hasPlayerInteracted) {
                isPaused = true;
                player.direction = Entity.Direction.UP;
            }
            if (isPaused) {
                groundScrollSpeed = 0;
            } else {
                groundScrollSpeed = GROUND_SCROLL_SPEED;
            }
        } // end of GAME_PLAY

    }

    // DRAW
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Title
        if(gameState == GameState.TITLE){
            ui.draw(g2);
        }
        else{
            groundGraphics.draw(g2);
            encounterManager.draw(g2);
            player.draw(g2);
            // GUI
            g2.drawString(Integer.toString((int)timer), 10, 10);
        }

        // when drawing is done, dispose to save memory
        g2.dispose();

    }

    // getters
    public int getMaxScreenCol() {
        return maxScreenCol;
    }
    public int getMaxScreenRow() {
        return maxScreenRow;
    }
    public int getScreenWidth(){
        return screenWidth;
    }
    public int getScreenHeight(){
        return screenHeight;
    }
    public int getScale(){
        return scale;
    }
    public int getTileSize(){
        return tileSize;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPIN(String PIN){
        this.PIN = PIN;
    }

    public void saveGame(){
        String saveFileName;
        // if new game(don't know username)
        if(username == null && PIN == null) {
            // TODO: pause game
            // ask for username and PIN
            askForInfo();
            // create file, save data to file
            saveFileName = username + PIN;
            //TODO: add entry to database
        }
        // not new game
        else {
            // get filename from database
            saveFileName = databaseManager.getSaveFileNameFromDatabase(username, PIN);
        }
        // will create new save file or overwrite existing one
        saveToSaveFile(saveFileName);
    }

    public void loadGame(){
        String username = "lisa";
        String PIN = "1234";
    }

    public void askForInfo(){
        InfoDialog infoDialog = new InfoDialog(ownerJF, this);
        infoDialog.setVisible(true);
    }

    private void saveToSaveFile(String fileName){
        try {
            PrintStream fout = new PrintStream("./saves/" + fileName + ".txt");
            fout.println("score " + (int) timer);
            fout.println("gold " + gold);
            fout.println("hunger " + hunger);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
