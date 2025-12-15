package ruairi.nea.gameClasses.Entities.Enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class WillOWisp extends PacingEnemy {
    private final String SPRITESHEET_PATH = "assets/WillOWispSpriteSheet.png";
    private Animation<TextureRegion> animation;

    public static final float WIDTH = 12;
    public static final float HEIGHT = 28;
    public static final float SPEED = 250;
    public static final float INTERSECT_TOLERANCE = 10;
    public static final int DAMAGE = 20;

    public float stateTime = 0;

    final float targetPosX;

    public WillOWisp(float posX, float posY, float lowerBound, float upperBound){
        super(posX,posY, WIDTH*ZOOM, HEIGHT*ZOOM,lowerBound,upperBound, SPEED, INTERSECT_TOLERANCE,PaceDirection.VERTICAL);
        targetPosX = posX;
        velocityY = SPEED;

        Texture spriteSheet = new Texture(SPRITESHEET_PATH);
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, 12, 28);

        animation = new Animation<>(0.1f, frames[0]);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        contactDamage = DAMAGE;
        setAffectedByGravity(false);
    }

    @Override
    protected void updateTimers(float delta) {
        super.updateTimers(delta);
        stateTime+=delta;
    }

    @Override
    public void updateVelocity(double delta) {
        super.updateVelocity(delta);
    }

    @Override
    public TextureRegion getCurrentFrame(){
        return animation.getKeyFrame(stateTime);
    }

    public void dispose(){
        super.dispose();
    }

    @Override
    public void draw(Batch batch) {
        float drawX = this.getPosX();
        float drawY = this.getPosY();
        float drawWidth = this.getWidth();
        float drawHeight = this.getHeight();

        if (velocityY<0){
            drawY += drawHeight;
            drawHeight = -1 * drawHeight;
            drawX += drawWidth;
            drawWidth = -1 * drawWidth;
        }
        if (getTimeUntilRemoval()!=null) batch.setColor(0.1f,0.1f,0.1f,0.5f);
        if (getAppearDamagedTimer()!=null) batch.setColor(1,0.8f,0.7f,0.95f);
        batch.draw(getCurrentFrame(),drawX,drawY,drawWidth,drawHeight);
        batch.setColor(Color.WHITE);
    }

}
