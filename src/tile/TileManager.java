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

        for(int i = 0; i < gp.getMaxScreenCol(); i++){
            g2.drawImage(tile[0].image,(gp.getTileSize() * i),gp.getTileSize() * (gp.getMaxScreenRow()-2), gp.getTileSize(), gp.getTileSize(), null);
            g2.drawImage(tile[1].image,(gp.getTileSize() * i),gp.getTileSize() * (gp.getMaxScreenRow()-1), gp.getTileSize(), gp.getTileSize(), null);
        }

    }

}
