package ruairi.nea.gameClasses.Entities.Enemies;

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


    final float targetPosY;

    public Fireball(float posX, float posY, float leftBound, float rightBound) {
        super(posX, posY, 32* ZOOM, 32* ZOOM, leftBound, rightBound, SPEED,INTERSECT_TOLERANCE,PaceDirection.HORIZONTAL);
        targetPosY = posY;

        spriteSheet = new Texture(SPRITESHEET_PATH);

        int frameWidth = 32;
        int frameHeight = 32;

        TextureRegion[][] frames = TextureRegion.split(spriteSheet, frameWidth, frameHeight);

        animation = new Animation<>(0.3f, frames[0]);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        contactDamage = DAMAGE;
        setAffectedByGravity(false);
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
    public void updateVelocity(double delta) {
        super.updateVelocity(delta);
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
