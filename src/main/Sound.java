package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Sound {
    static private Clip music, soundEffect;
    static private FloatControl floatControl;

    static {
        try {
            music = AudioSystem.getClip();
            soundEffect = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    static private final Map<String, URL> soundURL = new HashMap<>();
    static private final Map<String, AudioInputStream> audioStreams = new HashMap<>();

    static private String soundFolder = "/sound/";

    static public final String BBA = "BlueBoyAdventure", coin = "coin", powerUp = "powerup", unlock = "unlock",
            fanfare = "fanfare", hitMonster = "hitmonster", receiveDamage = "receivedamage", parry = "parry",
            levelUp = "levelup";

    static public void loadSound() {
        getSound(BBA);
        getSound(coin);
        getSound(powerUp);
        getSound(unlock);
        getSound(fanfare);
        getSound(hitMonster);
        getSound(receiveDamage);
        getSound(parry);
        getSound(levelUp);
    }

    static private void getSound(String file) {
        soundURL.put(file, Sound.class.getResource(soundFolder + file + ".wav"));
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL.get(file));
            audioInputStream.mark(Integer.MAX_VALUE);
            audioStreams.put(file, audioInputStream);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    static public Clip setFile(String music) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStreams.get(music));
            return clip;
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    static public void playMusic(String musicName) {
        music = setFile(musicName);
        floatControl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
        music.start();
        music.loop(Clip.LOOP_CONTINUOUSLY);
    }

    static public void stopMusic() {
        if (music != null)
            music.stop();
    }

    static public void setVolume(int volume) {
        float fcVolume = -40 + 46 * ((float)volume / 100);
        System.out.println("set gain to " + fcVolume + "dB");
        floatControl.setValue(fcVolume);
    }

    static public void playSoundEffect(String sound) {
//        if (!soundEffect.isRunning()) {
//            try {
//                soundEffect = AudioSystem.getClip();
//                AudioInputStream stream = audioStreams.get(sound);
//                stream.reset();
//                soundEffect.open(stream);
//            } catch (LineUnavailableException | IOException e) {
//                e.printStackTrace();
//                throw new RuntimeException();
//            }
//            soundEffect.start();
//        }
    }
}
