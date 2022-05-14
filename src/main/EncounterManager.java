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
    private final int SPAWN_INTERVAL_TIME = 5;
    Encounter encounter = null;
    BufferedImage innImage, shopImage,eventImage;
    int posX;
    boolean shouldSpawnEncounter = true;
    double timer = 0;

    public EncounterManager(GamePanel gp){
        random = new Random();
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
    }

    void spawnInn(){

        encounter = Encounter.INN;
    }

    void spawnRandomEvent(){
        encounter = Encounter.EVENT;
    }

    public void update(){
        if(timer >= SPAWN_INTERVAL_TIME && shouldSpawnEncounter){
            spawnEncounter();
            // reset variables
            shouldSpawnEncounter = false;
            timer = 0;
        }
        if(!shouldSpawnEncounter){
            if(gp.keyHandler.confirmPressed){
                shouldSpawnEncounter = true;
            }
        }

        posX -= gp.groundScrollSpeed;
    }

    public void draw(Graphics2D g2){

        if(encounter != null) {
            switch (encounter) {
                case INN:
                    g2.drawImage(
                            innImage,
                            posX,
                            (gp.getTileSize() * (gp.getMaxScreenRow() - 2)) - (innImage.getHeight() * gp.getScale()) + 15 ,
                            innImage.getWidth() * gp.getScale(),
                            innImage.getHeight() * gp.getScale(),
                            null
                    );
                    break;
                case SHOP:
                    g2.drawImage(
                            shopImage,
                            posX,
                            (gp.getTileSize() * (gp.getMaxScreenRow() - 2)) - (shopImage.getHeight() * gp.getScale()) + 15,
                            shopImage.getWidth() * gp.getScale(),
                            shopImage.getHeight() * gp.getScale(),
                            null
                    );
                    break;
                case EVENT:
                    g2.drawImage(
                            eventImage,
                            posX,
                            (gp.getTileSize() * (gp.getMaxScreenRow() - 2)) - (eventImage.getHeight() * gp.getScale()),
                            eventImage.getWidth() * gp.getScale(),
                            eventImage.getHeight() * gp.getScale(),
                            null
                    );
                    break;
            }
        }
    }


}
