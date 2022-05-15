package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    private final Color GUI_BG_COLOR = new Color(1,135,46,70);
    private final Color ENERGY_BAR_COLOR = new Color(237,205,116);
    private final int GUI_BG_X = 10;
    private final int GUI_BG_Y = 10;
    private final int GUI_X_PADDING = 20;
    private final int ENERGY_BAR_WIDTH = 100;
    private final int ENERGY_BAR_HEIGHT = 20;
    private final int ITEM_ICON_SIZE = 32;
    private BufferedImage breadSprite;
    private BufferedImage flowerSprite;

    public int titleOptionPos = 0;

    public UI(GamePanel gp){
        this.gp = gp;
        try{
            breadSprite = ImageIO.read(getClass().getResource("../res_tiles/bread.png"));
            flowerSprite = ImageIO.read(getClass().getResource("../res_tiles/flower.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;
        // Title
        if(gp.gameState == GamePanel.GameState.TITLE){
            drawTitleScreen();
        }
        else{
            drawGUI();
        }

    }

    private void drawTitleScreen(){
        // Title name
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 64F));
        String text = "A Simple Journey";
        int x = getXForCenteredText(text);
        int y = gp.tileSize * 3;
        // shadow
        g2.setColor(Color.gray);
        g2.drawString(text, x+3, y+3);
        // main color
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // Menu
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        text = "New Journey";
        x = getXForCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x,y);
        if(titleOptionPos == 0){
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Continue Journey";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text,x,y);
        if(titleOptionPos == 1){
            g2.drawString(">", x - gp.tileSize, y);
        }


    }

    private void drawGUI(){
        // draw background box
        g2.setColor(GUI_BG_COLOR);
        g2.fillRect(GUI_BG_X,GUI_BG_Y,250,180);
        // draw stats
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
        // energy bar
        g2.setColor(Color.white);
        g2.drawString("Energy", GUI_BG_X + GUI_X_PADDING, GUI_BG_Y + 25 );
        g2.setColor(ENERGY_BAR_COLOR);
        g2.drawRect(GUI_BG_X + GUI_X_PADDING + 70, GUI_BG_Y + 20 - (ENERGY_BAR_HEIGHT / 2), ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT);
        g2.fillRect(GUI_BG_X + GUI_X_PADDING + 70, GUI_BG_Y + 20 - (ENERGY_BAR_HEIGHT / 2), (int)Math.round(gp.energy), ENERGY_BAR_HEIGHT);

        // score and gold
        g2.setColor(Color.white);
        g2.drawString("Score: " + ((int)gp.timer), GUI_BG_X+GUI_X_PADDING, GUI_BG_Y+55);
        g2.drawString("Gold: " + gp.gold, GUI_BG_X+GUI_X_PADDING, GUI_BG_Y+80);
        // line separator
        g2.drawLine(GUI_BG_X+GUI_X_PADDING, GUI_BG_Y+100, GUI_BG_X+GUI_X_PADDING + 200, GUI_BG_Y+100);
        // draw items
        g2.drawImage(breadSprite, GUI_BG_X+GUI_X_PADDING, GUI_BG_Y+130-ITEM_ICON_SIZE+5, ITEM_ICON_SIZE, ITEM_ICON_SIZE, null);
        g2.drawString("Bread: x" + (gp.bread), GUI_BG_X+GUI_X_PADDING+ITEM_ICON_SIZE+5, GUI_BG_Y+130);
        g2.drawImage(flowerSprite, GUI_BG_X+GUI_X_PADDING, GUI_BG_Y+155-ITEM_ICON_SIZE+5, ITEM_ICON_SIZE, ITEM_ICON_SIZE, null);
        g2.drawString("Flower: x" + (gp.flower), GUI_BG_X+GUI_X_PADDING+ITEM_ICON_SIZE+5, GUI_BG_Y+155);

    }

    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
}
