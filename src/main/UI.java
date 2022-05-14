package main;

import java.awt.*;

public class UI {

    GamePanel gp;
    Graphics2D g2;

    public int titleOptionPos = 0;

    public UI(GamePanel gp){
        this.gp = gp;
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;
        // Title
        if(gp.gameState == GamePanel.GameState.TITLE){
            drawTitleScreen();
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

    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
}
