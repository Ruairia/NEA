package ruairi.nea.gameClasses.UI;

import com.badlogic.gdx.graphics.Color;
import ruairi.nea.gameClasses.Entities.Hero;


public class HealthBar extends UIBar {

    public static final float WIDTH = 128;
    public static final float HEIGHT = 24;
    public static final float PADDING_X = 16;
    public static final float PADDING_Y = 16;

    public HealthBar(float screenWidth, float screenHeight) {
        super(WIDTH,HEIGHT,PADDING_X,PADDING_Y,screenWidth, screenHeight, Hero.MAX_HEALTH);
        barColour = Color.FIREBRICK;
        meterColour = Color.FOREST;
    }
}
