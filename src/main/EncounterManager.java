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

    class Event{
        String text;
        String gainItem;
        int gainAmount;
        String loseItem;
        int loseAmount;
        boolean isTransaction;

        public Event(String text, String gainItem, int gainAmount, String loseItem, int loseAmount, boolean isTransaction){
            this.text = text;
            this.gainItem = gainItem;
            this.gainAmount = gainAmount;
            this.loseItem = loseItem;
            this.loseAmount = loseAmount;
            this.isTransaction = isTransaction;
        }
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
    Event currEvent= null;

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
        if(i < 6){
            spawnRandomEvent();
        }
        else if(i < 8){
            spawnShop();
        }
        else{
            spawnInn();
        }
        // set x pos to spawn
        posX = gp.getScreenWidth();
    }

    public void generateEvent(){
        int i = random.nextInt(100);

        String text;
        String gainItem;
        int gainAmount;
        String loseItem;
        int loseAmount;
        boolean isTransaction;

        if(i < 35){
            // gain random amount of gold
            int goldGained = 8 + random.nextInt(6);
            text = "I found "+ goldGained +" gold on the grass!";
            gainItem = "gold";
            gainAmount = goldGained;
            loseItem = "";
            loseAmount = 0;
            isTransaction = false;
        }
        else if(i < 45){
            // lose random amount of gold(gets robbed)
            int goldLost = 6 + random.nextInt(8);
            text = "bandits";
            gainItem = "";
            gainAmount = 0;
            loseItem = "gold";
            loseAmount = goldLost;
            isTransaction = false;
        }
        else if(i < 60){
            // gain a flower
            text = "A fairy gave me a flower!";
            gainItem = "flower";
            gainAmount = 1;
            loseItem = "";
            loseAmount = 0;
            isTransaction = false;
        }
        else if(i < 70){
            // somebody ask for flower in exchange for bread
            text = "Somebody wants to trade their bread \n for my flower.";
            gainItem = "bread";
            gainAmount = 1;
            loseItem = "flower";
            loseAmount = 1;
            isTransaction = true;
        }
        else if(i < 90){
            // somebody ask for flower in exchange for gold
            int price = 10 + random.nextInt(6);
            text = "Somebody wants to buy a flower from me \n for " + price + " gold.";
            gainItem = "gold";
            gainAmount = price;
            loseItem = "flower";
            loseAmount = 1;
            isTransaction = true;
        }
        else{
            // gain a bread
            text = "Somebody left a bread here, lucky me!";
            gainItem = "bread";
            gainAmount = 1;
            loseItem = "";
            loseAmount = 0;
            isTransaction = false;
        }

        currEvent = new Event(text, gainItem, gainAmount, loseItem, loseAmount, isTransaction);
    }

    public void handleEvent(Event event){
        if(event.gainItem.equals("gold")){
            gp.gold += event.gainAmount;
        }
        if(event.gainItem.equals("flower")){
            gp.flower += event.gainAmount;
        }
        if(event.gainItem.equals("bread")){
            gp.bread += event.gainAmount;
        }
        if(event.loseItem.equals("gold")){
            gp.gold -= event.loseAmount;
            if(gp.gold < 0) {
                gp.gold = 0;
            }
        }
        if(event.loseItem.equals("flower")){
            gp.flower -= event.loseAmount;
        }

    }

    public void handleTransactionEvent(Event event){
        if(event.loseItem.equals("flower")){
            if(gp.flower < event.loseAmount){
                gp.playSFX(Sound.ERROR_SFX);
                gp.ui.showMessage("I don't have any flower.");
                return;
            }
        }
        if(event.loseItem.equals("gold")){
            if(gp.gold < event.loseAmount){
                gp.playSFX(Sound.ERROR_SFX);
                gp.ui.showMessage("I don't have enough gold.");
                return;
            }
        }
        gp.playSFX(Sound.KEY_SFX);
        handleEvent(event);
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
