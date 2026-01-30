package ruairi.nea.applicationClasses;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public SpriteBatch batch;

    public static final float UI_WIDTH = 800;
    public static final float UI_HEIGHT = 450;

    public static boolean drawCollisionBoxes = false;
    public static boolean drawHitboxes = false;
    public static boolean drawHurtboxes = false;
    public static boolean enemiesUpdate = true;
    public static boolean canFly = false;
    public static boolean drawDebugInfo = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
