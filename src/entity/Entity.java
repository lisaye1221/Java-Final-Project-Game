package entity;

import java.awt.image.BufferedImage;

public class Entity {
    public int x,y;
    public int speed;
    public Direction direction;

    public enum Direction{
        UP, DOWN, RIGHT, LEFT;
    }
}
