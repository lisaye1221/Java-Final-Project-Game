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
    private ArrayList<GroundImage> groundImageList;
    private ArrayList<GroundImage> decorImageList;
    private BufferedImage ground;
    private BufferedImage groundDecorGrass, groundDecorFlower;
    int groundScrollSpeed = 1;
    private Random random;

    public Ground(GamePanel gp) {
        random = new Random();
        this.gp = gp;
        getGroundImage();
        groundImageList = new ArrayList<>();
        decorImageList = new ArrayList<>();
        // calculate how many tile to fill across screen, add extra for filling in gap
        int numGroundTile = (gp.getScreenWidth() / ground.getWidth()) + 2;
        for(int i = 0; i < numGroundTile; i++){
            groundImageList.add(new GroundImage(
                    ground,
                    (i * ground.getWidth() * gp.getScale())
            ));
        }
        // do the same for decor
        int numDecorTile = (gp.getScreenWidth() / groundDecorGrass.getWidth()) + 2;
        for(int i = 0; i < numDecorTile; i++){
            decorImageList.add(new GroundImage(
                    getDecorImage(),
                    (i * groundDecorGrass.getWidth() * gp.getScale())
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
        // ground
        for(GroundImage image: groundImageList){
            image.x -= groundScrollSpeed;
        }
        GroundImage firstImage = groundImageList.get(0);
        if(firstImage.x + (firstImage.image.getWidth() * gp.getScale())< 0){
            firstImage.x = groundImageList.get(groundImageList.size() - 1).x + firstImage.image.getWidth();
            groundImageList.add(groundImageList.get(0));
            groundImageList.remove(0);
        }
        // ground decor
        for(GroundImage image: decorImageList){
            image.x -= groundScrollSpeed;
        }
        GroundImage decoFirstImage = decorImageList.get(0);
        if(decoFirstImage.x + (decoFirstImage.image.getWidth() * gp.getScale())< 0){
            decoFirstImage.x = decorImageList.get(decorImageList.size() - 1).x + decoFirstImage.image.getWidth();
            decorImageList.add(decorImageList.get(0));
            decorImageList.remove(0);
        }
    }

    public void draw(Graphics2D g2){
        for(GroundImage image: groundImageList){
            g2.drawImage(image.image, image.x, gp.getTileSize() * (gp.getMaxScreenRow()-2), image.image.getWidth() * gp.getScale(), image.image.getHeight()*gp.getScale(), null);
        }

        for(GroundImage image: decorImageList){
            g2.drawImage(image.image, image.x, gp.getTileSize() * (gp.getMaxScreenRow()-3), image.image.getWidth() * gp.getScale(), image.image.getHeight()*gp.getScale(), null);
        }
//        g2.drawImage(groundDecorGrass, 0, gp.getTileSize() * (gp.getMaxScreenRow()-3), groundDecorGrass.getWidth() * gp.getScale(), groundDecorGrass.getHeight()*gp.getScale(), null);
//        g2.drawImage(groundDecorGrass, groundDecorGrass.getWidth(), gp.getTileSize() * (gp.getMaxScreenRow()-3), groundDecorGrass.getWidth() * gp.getScale(), groundDecorGrass.getHeight()*gp.getScale(), null);
    }

    private BufferedImage getDecorImage(){
        int i = random.nextInt(100);
        if(i < 80){ // 80% grass
            return groundDecorGrass;
        }
        else{
            return groundDecorFlower;
        }
    }
}
