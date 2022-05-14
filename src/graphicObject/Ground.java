package graphicObject;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Ground {

    private class GroundImage{
        BufferedImage image;
        int x;

        public GroundImage(BufferedImage image, int x){
            this.image = image;
            this.x = x;
        }
    }

    GamePanel gp;
    private ArrayList<GroundImage> groundImageList;
    private BufferedImage ground;
    private BufferedImage groundDecorGrass, groundDecorFlower;
    int groundScrollSpeed = 1;

    public Ground(GamePanel gp) {
        this.gp = gp;
        getGroundImage();
        groundImageList = new ArrayList<>();
        // calculate how many tile to fill across screen, add extra for filling in gap
        int numGroundTile = (gp.getScreenWidth() / ground.getWidth()) + 2;
        for(int i = 0; i < numGroundTile; i++){
            groundImageList.add(new GroundImage(
                    ground,
                    (i * ground.getWidth())
            ));
        }
    }

    // load images
    void getGroundImage(){
        try {
            ground = ImageIO.read(getClass().getResourceAsStream("../res_tiles/ground.png"));
            groundDecorGrass = ImageIO.read(getClass().getResourceAsStream("../res_tiles/ground_decor_grass.png"));
            groundDecorFlower = ImageIO.read(getClass().getResourceAsStream("../res_tiles/ground_decor_flower.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // shift ground
    public void update(){
        for(GroundImage image: groundImageList){
            image.x -= groundScrollSpeed;
        }
        GroundImage firstImage = groundImageList.get(0);
        if(firstImage.x + firstImage.image.getWidth() < 0){
            firstImage.x = groundImageList.get(groundImageList.size() - 1).x + firstImage.image.getWidth();
            groundImageList.add(groundImageList.get(0));
            groundImageList.remove(0);
        }
    }

    public void draw(Graphics2D g2){
        for(GroundImage image: groundImageList){
            g2.drawImage(image.image, image.x, gp.getTileSize() * (gp.getMaxScreenRow()-2), ground.getWidth() * gp.getScale(), ground.getHeight()*gp.getScale(), null);
        }
    }
}
