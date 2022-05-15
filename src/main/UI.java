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

    public String currentDialogue;
    private final int POP_UP_X;
    private final int POP_UP_Y;
    private final int POP_UP_WIDTH;
    private final int POP_UP_HEIGHT;

    public int titleOptionPos = 0;

    public UI(GamePanel gp){
        this.gp = gp;

        POP_UP_X = gp.getTileSize() * 3;
        POP_UP_Y = gp.getTileSize() * 4;
        POP_UP_WIDTH = gp.getScreenWidth() - (gp.getTileSize() * 6);
        POP_UP_HEIGHT = gp.tileSize * 4;

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
        if(gp.gameState == GamePanel.GameState.GAME_PLAY || gp.gameState == GamePanel.GameState.GAME_PAUSE || gp.gameState == GamePanel.GameState.ENCOUNTER_STATE){
            drawGUI();
        }
        if(gp.gameState == GamePanel.GameState.ENCOUNTER_STATE){
            switch(gp.encounterManager.encounter){
                case INN:
                    drawInnPopUp();
                    break;
                case SHOP:
                    drawShopWindow();
                    break;
                case EVENT:
                    drawEventDialogue(gp.encounterManager.currEvent);
                    break;
            }
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
        g2.fillRoundRect(GUI_BG_X,GUI_BG_Y,240,180, 10,10);
        // draw stats
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
        // energy bar
        g2.setColor(Color.white);
        g2.drawString("Energy", GUI_BG_X + GUI_X_PADDING, GUI_BG_Y + 25 );
        g2.setColor(ENERGY_BAR_COLOR);
        g2.fillRect(GUI_BG_X + GUI_X_PADDING + 70, GUI_BG_Y + 20 - (ENERGY_BAR_HEIGHT / 2), (int)Math.round(gp.energy), ENERGY_BAR_HEIGHT);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(GUI_BG_X + GUI_X_PADDING + 70, GUI_BG_Y + 20 - (ENERGY_BAR_HEIGHT / 2), ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT);


        // score and gold
        g2.setColor(Color.white);
        g2.drawString("Traveled: " + ((int)gp.score) +"m", GUI_BG_X+GUI_X_PADDING, GUI_BG_Y+55);
        g2.drawString("Gold: " + gp.gold +"G", GUI_BG_X+GUI_X_PADDING, GUI_BG_Y+80);
        // line separator
        g2.drawLine(GUI_BG_X+GUI_X_PADDING, GUI_BG_Y+100, GUI_BG_X+GUI_X_PADDING + 200, GUI_BG_Y+100);
        // draw items
        g2.drawImage(breadSprite, GUI_BG_X+GUI_X_PADDING, GUI_BG_Y+130-ITEM_ICON_SIZE+5, ITEM_ICON_SIZE, ITEM_ICON_SIZE, null);
        g2.drawString("Bread: x" + (gp.bread) + " [Press E]", GUI_BG_X+GUI_X_PADDING+ITEM_ICON_SIZE+5, GUI_BG_Y+130);
        g2.drawImage(flowerSprite, GUI_BG_X+GUI_X_PADDING, GUI_BG_Y+155-ITEM_ICON_SIZE+5, ITEM_ICON_SIZE, ITEM_ICON_SIZE, null);
        g2.drawString("Flower: x" + (gp.flower), GUI_BG_X+GUI_X_PADDING+ITEM_ICON_SIZE+5, GUI_BG_Y+155);

    }

    public void drawEventDialogue(EncounterManager.Event event){
        // background
        drawWindowBackground(POP_UP_X, POP_UP_Y, POP_UP_WIDTH, POP_UP_HEIGHT);

        // draw text
        int y = POP_UP_Y + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        for(String line : event.text.split("\n")){
            g2.drawString(line, getXForCenteredText(line), y);
            y += 30;
        }


        // draw controls
        if(event.isTransaction){
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
            g2.drawString("[Press Z] Accept", POP_UP_X + gp.getTileSize(), POP_UP_Y + (gp.getTileSize()*3) );
            g2.drawString("[Press X] Decline", POP_UP_X + (gp.getTileSize()*7) - gp.getTileSize(), POP_UP_Y + (gp.getTileSize()*3));
        }
        else{
            String prompt = "[Press Z or X] Move On";
            g2.drawString("[Press Z or X] Move On", getXForCenteredText(prompt), POP_UP_Y + (gp.getTileSize()*3) );
        }
    }

    public void drawInnPopUp(){
        drawWindowBackground(POP_UP_X, POP_UP_Y, POP_UP_WIDTH, POP_UP_HEIGHT);

        String prompt = "Should I rest here and save my game?";
        // draw text
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.drawString(prompt, getXForCenteredText(prompt), POP_UP_Y + gp.getTileSize());
        g2.drawString("[Press Z] Yes", POP_UP_X + gp.getTileSize(), POP_UP_Y + (gp.getTileSize()*3) );
        g2.drawString("[Press X] No", POP_UP_X + (gp.getTileSize()*7) - (gp.getTileSize() / 2), POP_UP_Y + (gp.getTileSize()*3));
    }

    public void drawShopWindow(){
        drawWindowBackground(POP_UP_X, POP_UP_Y, POP_UP_WIDTH, POP_UP_HEIGHT);

        String prompt = "Shop";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.drawString(prompt, getXForCenteredText(prompt), POP_UP_Y + gp.getTileSize() - 10);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16F));
        prompt = "[Press 1] Buy Bread (-"+gp.BREAD_BUY_PRICE+"G)";
        g2.drawString(prompt, getXForCenteredText(prompt), POP_UP_Y + gp.getTileSize() + 30);
        prompt = "[Press 2] Buy Flower (-"+gp.FLOWER_BUY_PRICE+"G)";
        g2.drawString(prompt, getXForCenteredText(prompt), POP_UP_Y + gp.getTileSize() + 60);
        prompt = "[Press 3] Sell Flower (+"+gp.FLOWER_SELL_PRICE+"G)";
        g2.drawString(prompt, getXForCenteredText(prompt), POP_UP_Y + gp.getTileSize() + 90);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16F));
        g2.drawString("[Press X] Exit", POP_UP_X + (gp.getTileSize()*7) - (gp.getTileSize() / 2), POP_UP_Y + (gp.getTileSize()*3) + 20);

    }

    private void drawWindowBackground(int x, int y, int width, int height){
        g2.setColor(new Color(0,0,0,210));
        g2.fillRoundRect(x, y, width, height, 35, 35);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
}
