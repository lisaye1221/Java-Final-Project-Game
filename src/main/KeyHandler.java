package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed, confirmPressed, cancelPressed;
    GamePanel gp;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // TITLE
        if(gp.gameState == GamePanel.GameState.TITLE){
            if (key == KeyEvent.VK_UP) {
                gp.ui.titleOptionPos--;
                if(gp.ui.titleOptionPos < 0) {
                    gp.ui.titleOptionPos = 1;
                }
            }
            if (key == KeyEvent.VK_DOWN) {
                gp.ui.titleOptionPos++;
                gp.ui.titleOptionPos %= 2;
            }
            if (key == KeyEvent.VK_ENTER) {
                // new game
                if(gp.ui.titleOptionPos == 0){
                    gp.stopMusic(Sound.TITLE_BGM);
                    // gp.playMusic(Sound.MAIN_BGM);
                    gp.gameState = GamePanel.GameState.GAME_PLAY;
                }
                // load game
                else{
                    // add later
                }
            }
        }
        else if(gp.gameState == GamePanel.GameState.GAME_PLAY) {
            if (key == KeyEvent.VK_UP) {
                upPressed = true;
            }
            if (key == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
            if (key == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (key == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (key == KeyEvent.VK_Z) {
                confirmPressed = true;
            }
            if (key == KeyEvent.VK_X) {
                cancelPressed = true;
            }
            // for testing
            if (key == KeyEvent.VK_S) {
               gp.saveGame();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if(key == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if(key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if(key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if(key == KeyEvent.VK_Z){
            confirmPressed = false;
        }
        if(key == KeyEvent.VK_X){
            cancelPressed = false;
        }

    }
}
