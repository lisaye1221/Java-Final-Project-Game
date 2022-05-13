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

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        x = 100;
        y = 100;
        speed = 4;
        direction = Direction.RIGHT;
    }

    public void getPlayerImage(){
        try{
            right_sprite_1 = ImageIO.read(getClass().getResource("../player/player_right_1.png"));
            right_sprite_2 = ImageIO.read(getClass().getResource("../player/player_right_2.png"));
            right_sprite_3 = ImageIO.read(getClass().getResource("../player/player_right_3.png"));
            up_sprite = ImageIO.read(getClass().getResource("../player/player_up.png"));
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
    }
    public void draw(Graphics2D g2){
        
        // determine the sprite
        BufferedImage image = null;
        if (direction == Direction.RIGHT) {
            image = right_sprite_1;
        }
        else if(direction == Direction.UP){
            image = up_sprite;
        }

        // draw the sprite
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

    }

}
