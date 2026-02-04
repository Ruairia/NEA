package ruairi.nea.applicationClasses;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.useVsync(false);
        config.setForegroundFPS(512); //Frame Rate Capped at 120
        config.setWindowedMode(800, 450); //16:9 Aspect Ratio
        config.setTitle("2D Platformer");
        config.setWindowIcon("assets/icon.png");
        config.setResizable(false); //Prevents any undefined behaviour that may occur due to window resizing

        new Lwjgl3Application(new Main(), config);
    }
}