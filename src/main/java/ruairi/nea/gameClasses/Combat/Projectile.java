package ruairi.nea.gameClasses.Combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Entities.Entity;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Projectile extends Entity {

    public int damage;
    public int angleDeg;

    public static Texture spriteSheet;
    public static final String SPRITESHEET_PATH = "assets/ProjectileSpriteSheet.png";
    public Animation<TextureRegion> animation;
    float stateTime = 0;

    public final float intersectTolerance;

    public enum projectileType {
        FIREMAGE,
        FIRESTAFF
    }
    public projectileType type;

    public Projectile(float posX, float posY, float velocityX, float velocityY, int damage, projectileType type){
        super(posX,posY,8*ZOOM,8*ZOOM);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.damage = damage;
        this.type=type;

        int frameWidth = 8;
        int frameHeight = 8;

        loadTextures();
        TextureRegion[] frames = TextureRegion.split(spriteSheet, frameWidth, frameHeight)[0];

        animation = new Animation<>(0.15f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        setAffectedByGravity(false);

        angleDeg = (int) Math.toDegrees(Math.atan2(velocityY, velocityX));

        switch (type){
            case FIREMAGE -> intersectTolerance = 10;
            case FIRESTAFF -> intersectTolerance = 1;
            default -> intersectTolerance = 0;
        }
    }



    private static void loadTextures(){
        if (spriteSheet != null) return;
        spriteSheet = new Texture(SPRITESHEET_PATH);

    }

    @Override
    public void draw(Batch batch) {
        if (type == projectileType.FIREMAGE) batch.setColor(1,0.5f,0.5f,1);
        if (getTimeUntilRemoval()!=null) batch.setColor(0.1f,0.1f,0.1f,0.5f);

        batch.draw(
                getCurrentFrame(),
                posX,
                posY,
                width / 2f,       // originX
                height / 2f,      // originY
                width,
                height,
                1f,               // scaleX
                1f,               // scaleY
                angleDeg          // rotation in degrees
        );

        batch.setColor(Color.WHITE);
    }

    @Override
    public void updateTimers(float delta){
        super.updateTimers(delta);
        stateTime+=delta;
    }

    @Override
    public TextureRegion getCurrentFrame(){
        return animation.getKeyFrame(stateTime);
    }

    public static void disposeTextures(){
        spriteSheet.dispose();
    }
}
