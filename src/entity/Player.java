package entity;

import main.*;

import java.awt.*;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyHandler;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;
        setDefaultValues();
    }

    public void setDefaultValues(){
        x = 100;
        y = 100;
        speed = 4;
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
        g2.setColor(Color.black);
        g2.fillRect(x,y, gp.tileSize, gp.tileSize);
    }

}
