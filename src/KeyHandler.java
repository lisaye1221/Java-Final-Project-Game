import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed, confirmPressed, cancelPressed;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if(key == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if(key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if(key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if(key == KeyEvent.VK_Z){
            confirmPressed = true;
        }
        if(key == KeyEvent.VK_X){
            cancelPressed = true;
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
