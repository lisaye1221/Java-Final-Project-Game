package entity;

import main.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyHandler;
    public BufferedImage right_sprite_1,right_sprite_2, right_sprite_3, up_sprite;
    public int imageSpeedThreshold = 10;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        x = 100;
        y = (gp.getMaxScreenRow()-3) * gp.getTileSize();
        speed = 4;
        direction = Direction.RIGHT;
    }

    public void getPlayerImage(){
        try{
            right_sprite_1 = ImageIO.read(getClass().getResource("../res_player/player_right_1.png"));
            right_sprite_2 = ImageIO.read(getClass().getResource("../res_player/player_right_2.png"));
            right_sprite_3 = ImageIO.read(getClass().getResource("../res_player/player_right_3.png"));
            up_sprite = ImageIO.read(getClass().getResource("../res_player/player_up.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void update(){
        // update player position
        if(keyHandler.upPressed) {
            y -= speed;
        }
        else if(keyHandler.downPressed){
            y += speed;
        }
        else if(keyHandler.rightPressed){
            x += speed;
        }
        else if(keyHandler.leftPressed){
            x -= speed;
        }

        spriteCounter++;
        if(spriteCounter > imageSpeedThreshold){
            spriteNum++;
            if(spriteNum > 3){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
    public void draw(Graphics2D g2){

        // determine the sprite
        BufferedImage image = null;
        if (direction == Direction.RIGHT) {
            if(spriteNum == 1) {
                image = right_sprite_1;
            }
            if(spriteNum == 2) {
                image = right_sprite_2;
            }
            if(spriteNum == 3) {
                image = right_sprite_3;
            }
        }
        else if(direction == Direction.UP){
            image = up_sprite;
        }

        // draw the sprite
        g2.drawImage(image, x, y, gp.getTileSize(), gp.getTileSize(), null);

    }

}
