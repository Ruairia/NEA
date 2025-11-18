package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Texture;

public class Fireball extends Enemy{
    float SPEED = 100;
    int DAMAGE = 30;
    float INTERSECT_TOLERANCE = 20;

    Texture frameOne;
    Texture frameTwo;

    float leftBound;
    float rightBound;


    public Fireball(float posX, float posY, float leftBound, float rightBound) {
        super(posX, posY, 0, 0, 80, 100);
        frameOne = new Texture("assets/fireballFrame1.png");
        frameTwo = new Texture("assets/fireballFrame2.png");
        setTexture(frameOne);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        velocityX = SPEED;
        damage = DAMAGE;
        intersectTolerance = INTERSECT_TOLERANCE;
        isAffectedByGravity=false;
    }
    @Override
    public void update(double delta){
        super.update(delta);
        float frameDuration = 0.3f; // Seconds
        if (((double) System.currentTimeMillis() /1000) % (frameDuration*2)> frameDuration) setTexture(frameOne);
        else setTexture(frameTwo);
        if (posX <= leftBound) velocityX = SPEED;
        else if (posX >= rightBound) velocityX = -SPEED;
    }



}
