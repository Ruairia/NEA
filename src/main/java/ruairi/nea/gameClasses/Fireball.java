package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Texture;

import java.io.IOException;

public class Fireball extends Enemy{
    Texture frameOne;
    Texture frameTwo;
    public Fireball(float posX, float posY) {
        super(posX, posY, 0, 0, 80, 100);
        frameOne = new Texture("assets/fireballFrame1.png");
        frameTwo = new Texture("assets/fireballFrame2.png");
        setTexture(frameOne);
    }
    @Override
    public void update(double delta){
        super.update(delta);
        float frameDuration = 0.3f; // Seconds
        if (((double) System.currentTimeMillis() /1000) % (frameDuration*2)> frameDuration) setTexture(frameOne);
        else setTexture(frameTwo);
    }
}
