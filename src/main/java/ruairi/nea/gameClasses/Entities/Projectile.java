package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Entities.Enemies.BossAI;
import ruairi.nea.gameClasses.Entities.Enemies.Explosion;
import ruairi.nea.gameClasses.Level;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Projectile extends Entity {

    public int damage;
    public int angleDeg;

    public static Texture spriteSheet;
    public static final String SPRITESHEET_PATH = "assets/ProjectileSpriteSheet.png";
    public Animation<TextureRegion> animation;
    float stateTime = 0;
    static int FRAME_WIDTH = 8;
    static int FRAME_HEIGHT = 8;

    public Float lifetime = null;
    public Level level = null;

    public final float intersectTolerance;

    public enum projectileType {
        FIREMAGE,
        FIRE_STAFF,
        BOSS,
        BOSS_EXPLOSIVE
    }
    public projectileType type;

    public enum Origin {
        PLAYER,
        FIREMAGE,
        BOSS
    }
    public Origin origin;

    public Projectile(float posX, float posY, float velocityX, float velocityY, int damage, Level level, projectileType type, Origin origin){
        this(posX,posY,velocityX,velocityY,damage,type,origin);
        this.level = level;
    }

    public Projectile(float posX, float posY, float velocityX, float velocityY, int damage, projectileType type, Origin origin){
        super(posX,posY,8*ZOOM,8*ZOOM);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.damage = damage;
        this.type=type;
        this.origin = origin;
        lifetime = switch (type){
            case BOSS -> 0.5f;
            case BOSS_EXPLOSIVE -> 0.8f;
            default -> null;
        };

        loadTextures();
        TextureRegion[] frames = TextureRegion.split(spriteSheet, FRAME_WIDTH, FRAME_HEIGHT)[0];

        animation = new Animation<>(0.15f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        setAffectedByGravity(false);

        angleDeg = (int) Math.toDegrees(Math.atan2(velocityY, velocityX));

        switch (type){
            case FIREMAGE -> intersectTolerance = 10;
            case FIRE_STAFF -> intersectTolerance = 1;
            case BOSS -> intersectTolerance = 5;
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
        if (type == projectileType.BOSS) batch.setColor(0.2f,0.7f,1,1);
        if (type == projectileType.BOSS_EXPLOSIVE) batch.setColor(1,0.8f,0.2f,1);
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
    public void update(double delta) {
        super.update(delta);
        if (lifetime!=null && lifetime<=0 && getTimeUntilRemoval()==null){
            setTimeUntilRemoval(0.1f);
            if (type == projectileType.BOSS) BossAI.punishMoveEverywhere(BossAI.BossState.SHOOT,0.05f);
            if (type == projectileType.BOSS_EXPLOSIVE){
                level.createExplosion(posX-32/2+width/2,posY-32/2+height/2,32,32, Explosion.Origin.BOSS);
            }
        }
    }

    @Override
    public void updateTimers(float delta){
        super.updateTimers(delta);
        stateTime+=delta;
        if (lifetime!=null) lifetime-=delta;
    }

    @Override
    public TextureRegion getCurrentFrame(){
        return animation.getKeyFrame(stateTime);
    }

    public static void disposeTextures(){
        spriteSheet.dispose();
    }
}
