package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ruairi.nea.gameClasses.Entities.Hero;

public class HealthBar {
    public static final float WIDTH = 128;
    public static final float HEIGHT = 24;
    public static final float XPADDING = 16;
    public static final float YPADDING = 16;
    public static final float BORDERWIDTH = 4;

    float posX;
    float posY;

    public HealthBar(float screenWidth, float screenHeight){
        this.posX=screenWidth-WIDTH-XPADDING;
        this.posY=screenHeight-HEIGHT-YPADDING;
    }

    public void render(ShapeRenderer shapeRenderer, float currentHealth){
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(posX-BORDERWIDTH, posY-BORDERWIDTH, WIDTH+BORDERWIDTH*2, HEIGHT+BORDERWIDTH*2);
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.rect(posX, posY, WIDTH, HEIGHT);
        shapeRenderer.setColor(Color.OLIVE);
        shapeRenderer.rect(posX, posY, WIDTH*(currentHealth)/ Hero.MAXHEALTH, HEIGHT);
        shapeRenderer.end();
    }
}
