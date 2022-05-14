package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TileManager {

    GamePanel gp;
    Tile[] tile;
    final int TILE_TYPES = 10;

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[TILE_TYPES];
        getTileImage();
    }

    public void getTileImage(){
        try{
            tile[0] = new Tile(ImageIO.read(getClass().getResourceAsStream("../res_tiles/ground_top.png")));
            tile[1] = new Tile(ImageIO.read(getClass().getResourceAsStream("../res_tiles/ground_bottom.png")));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){

        g2.drawImage(tile[0].image,0,0, gp.tileSize, gp.tileSize, null);
        g2.drawImage(tile[0].image,gp.tileSize,0, gp.tileSize, gp.tileSize, null);
        g2.drawImage(tile[1].image,0,gp.tileSize, gp.tileSize, gp.tileSize, null);
        g2.drawImage(tile[1].image,gp.tileSize,gp.tileSize, gp.tileSize, gp.tileSize, null);

    }

}
