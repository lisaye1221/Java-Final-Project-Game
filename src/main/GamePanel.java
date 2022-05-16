
// main.GamePanel

package main;
import graphicObject.Cloud;
import graphicObject.Ground;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class GamePanel extends JPanel implements Runnable {
    public enum GameState{
        TITLE, GAME_PLAY, GAME_PAUSE, ENCOUNTER_STATE, GAME_OVER
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

    double FPS = 60;

    // graphics
    private final Ground groundGraphics = new Ground(this);
    private final Cloud cloudGraphics = new Cloud(this);

    // SYSTEM
    KeyHandler keyHandler = new KeyHandler(this);
    DatabaseManager databaseManager = new DatabaseManager();
    EncounterManager encounterManager = new EncounterManager(this);
    Sound sound = new Sound();
    UI ui = new UI(this);
    private Thread gameThread;
    public boolean isLoadGame = false;

    // player
    public final int PLAYER_X = 100;
    Player player = new Player(this, keyHandler);

    // game data & stats
    private String username = null;
    private String PIN = null;
    private final int GROUND_SCROLL_SPEED = 2;
     private final double ENERGY_DEPLETION_RATE = 0.5;
    private final int BREAD_ENERGY = 15;
    public final int BREAD_BUY_PRICE = 5;
    public final int FLOWER_BUY_PRICE = 10;
    public final int FLOWER_SELL_PRICE = 8;
    public int groundScrollSpeed = GROUND_SCROLL_SPEED;
    public boolean isPaused = false;
    public GameState gameState;
    // game stats
    public double timer = 0;
    public double score = 0;
    public int gold = 10;
    public double energy = 100;
    public int bread = 2;
    public int flower = 1;

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

            // game over
            if(energy <= 0){
                gameState = GameState.GAME_OVER;
                stopMusic(Sound.MAIN_BGM);
            }

            // gain score, lose energy
            score += 1/ FPS;
            energy -= (ENERGY_DEPLETION_RATE / FPS);

            // reaches an encounter
            if (encounterManager.xForCollisionDetection <= PLAYER_X && !encounterManager.hasPlayerInteracted) {
                isPaused = true;
                player.direction = Player.Direction.UP;
                // do the corresponding stuff
                switch(encounterManager.encounter){
                    case INN:
                        break;
                    case EVENT:
                        encounterManager.generateEvent();
                        if(encounterManager.currEvent.text.equals("bandits")){
                            int goldLost = encounterManager.currEvent.loseAmount;
                            if(gold > goldLost){
                                encounterManager.currEvent.text = "I lost "+ goldLost +" gold to some bandits.";
                            }
                            else{
                                if(gold == 0){
                                    encounterManager.currEvent.text = "I encountered some bandits.\n But I had no gold on me to lose.";
                                }
                                else {
                                    encounterManager.currEvent.text = "I lost "+ gold +" gold to some bandits.\n I have nothing left.";
                                }
                            }
                        }
                        break;
                    case SHOP:
                        break;
                }
                gameState = GameState.ENCOUNTER_STATE;
            }
            if (isPaused) {
                groundScrollSpeed = 0;
            } else {
                groundScrollSpeed = GROUND_SCROLL_SPEED;
            }
        } // end of GAME_PLAY
        cloudGraphics.update();
    }

    // DRAW
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Title
        if(gameState != GameState.TITLE){
            groundGraphics.draw(g2);
            encounterManager.draw(g2);
            player.draw(g2);
        }
        cloudGraphics.draw(g2);
        ui.draw(g2);
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

    // audio related
    public void playMusic(int i ){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    public void stopMusic(int i){
        sound.stop();
    }
    public void playSFX(int i){
        sound.setFile(i);
        sound.play();
    }

    public void eatBread(){
        if(bread > 0) {
            playSFX(Sound.EAT_SFX);
            bread--;
            energy += BREAD_ENERGY;
        }
    }

    public void exitEncounter(){
        encounterManager.hasPlayerInteracted = true;
        encounterManager.shouldSpawnEncounter = true;
        isPaused = false;
        gameState = GameState.GAME_PLAY;
        player.direction = Player.Direction.RIGHT;
    }

    public void saveGame(){
        String saveFileName;
        // if new game(don't know username)
        if(username == null && PIN == null) {
            gameState = GameState.GAME_PAUSE;
            // ask for username and PIN
            askForInfo();
            if(username == null || PIN == null) {
                System.out.println("Something went wrong with entering username/PIN");
                ui.showMessage("Something went wrong with saving.");
                return;
            }
            // create file, save data to file
            saveFileName = username + PIN;
            databaseManager.saveNewGameToDatabase(username, PIN, saveFileName);
        }
        // not new game
        else {
            // get filename from database
            saveFileName = databaseManager.getSaveFileNameFromDatabase(username, PIN);
        }
        // will create new save file or overwrite existing one
        saveToSaveFile(saveFileName);
    }

    public void askForInfo(){
        InfoDialog infoDialog = new InfoDialog(ownerJF, this);
        infoDialog.setVisible(true);
    }

    private void saveToSaveFile(String fileName){
        try {
            PrintStream fout = new PrintStream("./saves/" + fileName + ".txt");
            fout.println("score," + (int) score);
            fout.println("gold," + gold);
            fout.println("energy," + energy);
            fout.println("bread," + bread);
            fout.println("flower," + flower);
        } catch (IOException e) {
            ui.showMessage("Something went wrong with saving.");
            e.printStackTrace();
        }
    }

    // called from title screen
    public void loadGame(){
        isLoadGame = true;
        askForInfo();
        if(username != null && PIN != null){
            String saveFileName = databaseManager.getSaveFileNameFromDatabase(username, PIN);
            if(!saveFileName.equals("")){
                try {
                    // use filename to get file
                    Scanner fin = new Scanner(new File("./saves/" + saveFileName + ".txt"));
                    // read each line of file to get game info
                    ArrayList<String> gameData = new ArrayList<>();
                    while (fin.hasNext()) {
                        gameData.add(fin.next().split(",")[1]);
                    }
                    double score_load = Double.parseDouble(gameData.get(0));
                    int gold_load = Integer.parseInt(gameData.get(1));
                    double energy_load = Double.parseDouble(gameData.get(2));
                    int bread_load = Integer.parseInt(gameData.get(3));
                    int flower_load = Integer.parseInt(gameData.get(4));

                    score = score_load;
                    gold = gold_load;
                    energy = energy_load;
                    bread = bread_load;
                    flower = flower_load;
                    // load game
                }
                catch(FileNotFoundException e){
                    System.out.println("File cannot be found");
                }
            }
            else{
                System.out.println("No savefile found");
                ui.showMessage("No Save File Found. Starting New Game.");
            }
        }
        else{
            System.out.println("could not get username or PIN");
        }
    }

}
