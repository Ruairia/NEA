package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Fireball extends PacingEnemy{
    private static final String SPRITESHEET_PATH = "assets/FireballSpriteSheet.png";
    final static float SPEED = 50*ZOOM;
    final static int DAMAGE = 30;
    final static float INTERSECT_TOLERANCE = 10;

    Texture spriteSheet;
    Animation<TextureRegion> animation;
    float stateTime = 0;



    public Fireball(float posX, float posY, float leftBound, float rightBound) {
        super(posX, posY, 16* ZOOM, 20* ZOOM, leftBound, rightBound, SPEED,INTERSECT_TOLERANCE);

        spriteSheet = new Texture(SPRITESHEET_PATH);

        int frameWidth = 16;
        int frameHeight = 20;

        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(spriteSheet, 0, 0, frameWidth, frameHeight);
        frames[1] = new TextureRegion(spriteSheet, frameWidth, 0, frameWidth, frameHeight);

        animation = new Animation<>(0.3f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        damage = DAMAGE;
        isAffectedByGravity=false;
    }

    @Override
    public void update(double delta){
        super.update(delta);
    }

    @Override
    public void updateTimers(float delta){
        stateTime +=delta;
        super.updateTimers(delta);
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
