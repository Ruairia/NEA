package ruairi.nea.gameClasses.Entities.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Explosion extends Enemy{

    public static final String SPRITESHEET_PATH = "assets/ExplosionSpriteSheet.png";
    public final Texture spriteSheet = new Texture(SPRITESHEET_PATH);
    public final int TEXTURE_WIDTH = 40;
    public Animation<TextureRegion> animation;
    public float stateTime = 0;
    public static final float INTERSECT_TOLERANCE = 10;

    public static final float totalAnimationLength = 1.6f;

    public static final float MAX_LIFETIME = 0.4f;
    public float lifetime = MAX_LIFETIME;

    public enum Origin{
        BOSS,
        PLAYER
    }
    private Origin origin;

    public void loadAnimation(){
        if (animation!=null) return;
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, TEXTURE_WIDTH, TEXTURE_WIDTH);
        animation = new Animation<>(0.05f, frames[0]);
    }



    public Explosion(float posX, float posY, int size, int damage, Origin origin){
        super(posX,posY,size*ZOOM,size*ZOOM,damage);
        loadAnimation();
        lifetime = MAX_LIFETIME;
        this.contactDamage=damage;
        this.origin=origin;
        setAffectedByGravity(false);
    }

    @Override
    protected void updateTimers(float delta) {
        super.updateTimers(delta);
        lifetime-=delta;
        stateTime +=delta;
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        if (lifetime<=0&&getTimeUntilRemoval()==null) {
            setTimeUntilRemoval(totalAnimationLength-MAX_LIFETIME);
            if (origin==Origin.BOSS) BossAI.punishMoveEverywhere(BossAI.BossState.SHOOT_EXPLOSIVE,0.01f);
        }
    }


    @Override
    public TextureRegion getCurrentFrame() {
        return animation.getKeyFrame(stateTime);
    }

    public Origin getOrigin() {
        return origin;
    }
}
