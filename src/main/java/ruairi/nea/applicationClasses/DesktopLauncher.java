package ruairi.nea.applicationClasses;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.useVsync(true);
        config.setForegroundFPS(60);
        config.setWindowedMode(800, 450);
        config.setTitle("2D Platformer");

        new Lwjgl3Application(new Main(), config);
    }
}