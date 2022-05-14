package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class EncounterManager {

    enum Encounter{
        INN, SHOP, EVENT
    }

    GamePanel gp;
    private final Random random;
    private int spawnIntervalTime = 5;
    Encounter encounter = null;
    BufferedImage innImage, shopImage,eventImage;
    BufferedImage encounterImage;
    int posX;
    int xForCollisionDetection;
    boolean shouldSpawnEncounter = true;
    boolean hasPlayerInteracted = true;
    double timer = 0;

    public EncounterManager(GamePanel gp){
        random = new Random();
        posX = gp.getScreenWidth();
        this.gp = gp;
        getImages();
    }

    private void getImages(){
        try {
            innImage = ImageIO.read(getClass().getResourceAsStream("../res_tiles/inn.png"));
            shopImage = ImageIO.read(getClass().getResourceAsStream("../res_tiles/shop.png"));
            eventImage = ImageIO.read(getClass().getResourceAsStream("../res_tiles/random_event_sign.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void spawnEncounter(){
        hasPlayerInteracted = false;
        // generate a random encounter
        int i = random.nextInt(10);
        if(i < 4){
            spawnRandomEvent();
        }
        else if(i < 7){
            spawnShop();
        }
        else{
            spawnInn();
        }
        // set x pos to spawn
        posX = gp.getScreenWidth();
    }

    void spawnShop(){

        encounter = Encounter.SHOP;
        encounterImage = shopImage;
    }

    void spawnInn(){

        encounter = Encounter.INN;
        encounterImage = innImage;
    }

    void spawnRandomEvent(){
        encounter = Encounter.EVENT;
        encounterImage = eventImage;
    }

    public void update(){
        if(timer >= spawnIntervalTime && shouldSpawnEncounter){
            spawnEncounter();
            // reset variables
            shouldSpawnEncounter = false;
            timer = 0;
        }

//        // press Z to spawn (for testing)
//        if(!shouldSpawnEncounter){
//            if(gp.keyHandler.confirmPressed){
//                shouldSpawnEncounter = true;
//                spawnIntervalTime = random.nextInt(4) + 3; // 3-6 seconds
//            }
//        }

        if(encounter != null) {
            posX -= gp.groundScrollSpeed;
            switch (encounter) {
                case INN:
                    xForCollisionDetection = posX + encounterImage.getWidth() + 15;
                    break;
                case SHOP:
                    xForCollisionDetection = posX + encounterImage.getWidth() + 15;
                    break;
                case EVENT:
                    xForCollisionDetection = posX;
                    break;

            }
        }
    }

    public void draw(Graphics2D g2){
        if(encounter != null) {
            switch (encounter) {
                case INN:
                case SHOP:
                    g2.drawImage(
                            encounterImage,
                            posX,
                            (gp.getTileSize() * (gp.getMaxScreenRow() - 2)) - (encounterImage.getHeight() * gp.getScale()) + 15 ,
                            encounterImage.getWidth() * gp.getScale(),
                            encounterImage.getHeight() * gp.getScale(),
                            null
                    );
                    break;
                case EVENT:
                    g2.drawImage(
                            encounterImage,
                            posX,
                            (gp.getTileSize() * (gp.getMaxScreenRow() - 2)) - (encounterImage.getHeight() * gp.getScale()),
                            encounterImage.getWidth() * gp.getScale(),
                            encounterImage.getHeight() * gp.getScale(),
                            null
                    );
                    break;
            }
        }
    }


}
