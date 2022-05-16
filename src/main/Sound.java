package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

    Clip clip;
    URL[] soundURL = new URL[10];

    public static final int TITLE_BGM = 0;
    public static final int MAIN_BGM = 1;
    public static final int EAT_SFX = 2;
    public static final int KEY_SFX = 3;
    public static final int CURSOR_SFX = 4;
    public static final int TRANSACTION_SFX = 5;
    public static final int ERROR_SFX = 6;

    public Sound(){
        soundURL[0] = getClass().getResource("/res_audio/the_cafe.wav");
        soundURL[1] = getClass().getResource("/res_audio/panorama.wav");
        soundURL[2] = getClass().getResource("/res_audio/eat.wav");
        soundURL[3] = getClass().getResource("/res_audio/key.wav");
        soundURL[4] = getClass().getResource("/res_audio/cursor.wav");
        soundURL[5] = getClass().getResource("/res_audio/transaction.wav");
        soundURL[6] = getClass().getResource("/res_audio/error.wav");
    }

    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }

}
