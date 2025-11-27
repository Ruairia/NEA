package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class UIBar {
    public static final float BORDER_WIDTH = 4;

    float posX;
    float posY;
    float width;
    float height;

    int maxValue;

    protected Color outlineColour = Color.BLACK;
    protected Color barColour = Color.GRAY;
    protected Color meterColour = Color.WHITE;

    public UIBar(float width, float height, float paddingX, float paddingY, float screenWidth, float screenHeight, int maxValue){

        this.width=width;
        this.height=height;
        this.posX=screenWidth - width - paddingX;
        this.posY=screenHeight - height - paddingY;
        this.maxValue = maxValue;
    }

    public void draw(ShapeRenderer shapeRenderer, float currentValue){
        shapeRenderer.setColor(outlineColour);
        shapeRenderer.rect(posX - BORDER_WIDTH, posY- BORDER_WIDTH, width + BORDER_WIDTH *2, height + BORDER_WIDTH *2);
        shapeRenderer.setColor(barColour);
        shapeRenderer.rect(posX, posY, width, height);
        shapeRenderer.setColor(meterColour);
        shapeRenderer.rect(posX, posY, width *(currentValue)/ maxValue, height);

    }
}
