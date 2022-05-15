package graphicObject;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
    private final ArrayList<GroundImage> groundImageList;
    private final ArrayList<GroundImage> decorImageList;
    private BufferedImage bg;
    private BufferedImage ground;
    private BufferedImage groundDecorGrass, groundDecorFlower;
    private final Random random;

    public Ground(GamePanel gp) {
        random = new Random();
        this.gp = gp;
        getGroundImage();
        groundImageList = new ArrayList<>();
        decorImageList = new ArrayList<>();
        // calculate how many tile to fill across screen, add extra for filling in gap
        int numGroundTile = (gp.getScreenWidth() / (ground.getWidth()* gp.getScale())) + 2;
        for(int i = 0; i < numGroundTile; i++){
            groundImageList.add(new GroundImage(
                    ground,
                    (i * ground.getWidth() * gp.getScale())
            ));
        }
        // do the same for decor
        int numDecorTile = (gp.getScreenWidth() / (groundDecorGrass.getWidth()* gp.getScale())) + 2;
        for(int i = 0; i < numDecorTile; i++){
            BufferedImage newDecor = getDecorImage();
            decorImageList.add(new GroundImage(
                    newDecor,
                    (i * newDecor.getWidth() * gp.getScale())
            ));
        }
    }

    // load images
    void getGroundImage(){
        try {
            ground = ImageIO.read(getClass().getResourceAsStream("../res_tiles/ground.png"));
            groundDecorGrass = ImageIO.read(getClass().getResourceAsStream("../res_tiles/ground_decor_grass.png"));
            groundDecorFlower = ImageIO.read(getClass().getResourceAsStream("../res_tiles/ground_decor_flower.png"));
            bg = ImageIO.read(getClass().getResourceAsStream("../res_tiles/bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // shift ground
    public void update(){
        // ground
        for(GroundImage image: groundImageList){
            image.x -= gp.groundScrollSpeed;
        }
        GroundImage firstImage = groundImageList.get(0);
        if(firstImage.x + (firstImage.image.getWidth() * gp.getScale())< 0){
            firstImage.x = groundImageList.get(groundImageList.size() - 1).x + (firstImage.image.getWidth() * gp.getScale());
            groundImageList.add(groundImageList.get(0));
            groundImageList.remove(0);
        }
        // ground decor
        for(GroundImage image: decorImageList){
            image.x -= gp.groundScrollSpeed;
        }
        GroundImage decoFirstImage = decorImageList.get(0);
        if(decoFirstImage.x + (decoFirstImage.image.getWidth() * gp.getScale())< 0){
            decoFirstImage.x = decorImageList.get(decorImageList.size() - 1).x + (decoFirstImage.image.getWidth() * gp.getScale());
            decorImageList.add(decorImageList.get(0));
            decorImageList.remove(0);
        }
    }

    public void draw(Graphics2D g2){
        g2.drawImage(bg, 0, gp.getTileSize() * 2, bg.getWidth(), bg.getHeight(), null);
        for(GroundImage image: groundImageList){
            g2.drawImage(image.image, image.x, gp.getTileSize() * (gp.getMaxScreenRow()-2), image.image.getWidth() * gp.getScale(), image.image.getHeight()*gp.getScale(), null);
        }

        for(GroundImage image: decorImageList){
            g2.drawImage(image.image, image.x, gp.getTileSize() * (gp.getMaxScreenRow()-3), image.image.getWidth() * gp.getScale(), image.image.getHeight()*gp.getScale(), null);
        }
  }

    private BufferedImage getDecorImage(){
        int i = random.nextInt(10);
        if(i < 7){ // more grass than flower
            return groundDecorGrass;
        }
        else{
            return groundDecorFlower;
        }
    }
}
