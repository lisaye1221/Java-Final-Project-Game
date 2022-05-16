package graphicObject;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Cloud {

    private class CloudImage{
        BufferedImage image;
        int x;

        public CloudImage(BufferedImage image, int x){
            this.image = image;
            this.x = x;
        }
    }

    GamePanel gp;
    private final ArrayList<CloudImage> cloudImageList;
    private BufferedImage cloud1, cloud2,cloud3;
    private final Random random;
    private final int scrollSpeed = 1;

    public Cloud(GamePanel gp) {
        random = new Random();
        this.gp = gp;
        loadCloudImages();
        cloudImageList = new ArrayList<>();
        // calculate how many tile to fill across screen, add extra for filling in gap
        int numGroundTile = (gp.getScreenWidth() / (cloud1.getWidth()* gp.getScale())) + 2;
        for(int i = 0; i < numGroundTile; i++){
            BufferedImage cloud = getCloudImage();
            cloudImageList.add(new CloudImage(
                    cloud,
                    (i * cloud.getWidth() * gp.getScale())
            ));
        }
    }

    // load images
    void loadCloudImages(){
        try {
            cloud1 = ImageIO.read(getClass().getResourceAsStream("../res_tiles/cloud_1.png"));
            cloud2 = ImageIO.read(getClass().getResourceAsStream("../res_tiles/cloud_2.png"));
            cloud3 = ImageIO.read(getClass().getResourceAsStream("../res_tiles/cloud_3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(){
        // move the clouds
        for(CloudImage image: cloudImageList){
            image.x -= scrollSpeed;
        }
        // making it endless
        CloudImage firstImage = cloudImageList.get(0);
        if(firstImage.x + (firstImage.image.getWidth() * gp.getScale())< 0){
            firstImage.x = cloudImageList.get(cloudImageList.size() - 1).x + (firstImage.image.getWidth() * gp.getScale());
            cloudImageList.add(cloudImageList.get(0));
            cloudImageList.remove(0);
        }
    }

    public void draw(Graphics2D g2){
        for(CloudImage image: cloudImageList){
            g2.drawImage(image.image, image.x, -10, image.image.getWidth() * gp.getScale(), image.image.getHeight()*gp.getScale(), null);
        }
    }

    private BufferedImage getCloudImage() {
        int i = random.nextInt(3);
        if (i == 0) {
            return cloud1;
        } else if (i == 1) {
            return cloud2;
        } else {
            return cloud3;
        }
    }
}

