package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Fireball extends Enemy{
    final float SPEED = 100;
    final int DAMAGE = 30;
    final float INTERSECT_TOLERANCE = 10;

    Texture spriteSheet;
    Animation<TextureRegion> animation;
    float stateTime = 0;


    float leftBound;
    float rightBound;


    public Fireball(float posX, float posY, float leftBound, float rightBound) {
        super(posX, posY, 0, 0, 16* ZOOM, 20* ZOOM);

        spriteSheet = new Texture("assets/FireballSpriteSheet.png");

        int frameWidth = 16;
        int frameHeight = 20;

        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(spriteSheet, 0, 0, frameWidth, frameHeight);
        frames[1] = new TextureRegion(spriteSheet, frameWidth, 0, frameWidth, frameHeight);

        animation = new Animation<>(0.3f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

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
        stateTime += (float) delta;
        if (posX <= leftBound) velocityX = SPEED;
        else if (posX >= rightBound) velocityX = -SPEED;
    }

    @Override
    public TextureRegion getCurrentFrame() {
        return animation.getKeyFrame(stateTime);
    }


    @Override
    public void dispose(){
        spriteSheet.dispose();
    }


}
