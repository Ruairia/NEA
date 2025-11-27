package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Color;
import ruairi.nea.gameClasses.Entities.Hero;

public class ManaBar extends UIBar{

    public static final float WIDTH = 128;
    public static final float HEIGHT = 24;
    public static final float PADDING_X = 16;
    public static final float PADDING_Y = 40;

    public ManaBar(float screenWidth, float screenHeight) {
        super(WIDTH, HEIGHT, PADDING_X, PADDING_Y, screenWidth, screenHeight, Hero.MAX_MANA);
        barColour = Color.NAVY;
        meterColour = Color.BLUE;
    }
}
