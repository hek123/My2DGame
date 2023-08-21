package main;

public interface SwitchScreen {
    void switchScreen(String screen);

    void fullScreen();

    void exitFullScreen();

    String main = "main", game = "game", tileEditor = "tile";
}
