package main;

import java.io.*;

public class MainConfig {
    private final String configFile = "config.txt";

    private int musicVolume;

    public MainConfig() {
        try {
            loadConfig();
        } catch (IOException e) {
            System.out.println("failed to load config");
            e.printStackTrace();
        }
    }

    public void saveConfig() throws IOException{
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("config.txt"));
        bufferedWriter.write(musicVolume);
        bufferedWriter.close();
        System.out.println("Saved mainConfig to config.txt");
    }

    public void loadConfig() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("config.txt"));
        setMusicVolume(bufferedReader.read());
        System.out.println("Loaded mainConfig from config.txt");
    }

    public void setMusicVolume(int volume) {
        musicVolume = volume;
        Sound.setVolume(musicVolume);
    }
    public int getMusicVolume() {
        return musicVolume;
    }
}
