package entity;

import java.awt.image.BufferedImage;

public class Entity {
    public enum Direction{
        UP, DOWN, RIGHT, LEFT;
    }
    public int x,y;
    public int speed;
    public Direction direction;
    public int spriteCounter = 0;
    public int spriteNum = 0;


}
