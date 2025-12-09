package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Combat.Projectile;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class FireMage extends PacingEnemy{

    private final Hero hero;
    private static final String SPRITESHEET_PATH = "assets/FireballSpriteSheet.png";


    private static final float SPEED = 50*ZOOM;
    private static final float NOTICE_DISTANCE = 300*ZOOM;
    private static final float PROJECTILE_SPEED = 100*ZOOM;
    private static final float INTERSECT_TOLERANCE = 10;
    private static final int DAMAGE = 20;
    private static final float SHOOT_COOLDOWN = 1.25f;
    private static final int PROJECTILE_INTERSECT_TOLERANCE = 16;

    private static Texture spriteSheet;

    private static ArrayList<Projectile> projectiles = null;

    private final Animation<TextureRegion> animation;
    
    float stateTime = 0;
    float shootCooldown = 0;


    public static void loadTextures(){
        if (spriteSheet==null) spriteSheet = new Texture(SPRITESHEET_PATH);
    }

    public static void setProjectileArrayList(ArrayList<Projectile> projectilesList){
        projectiles=projectilesList;
    }



    public FireMage(float posX, float posY, Hero hero, ArrayList<Projectile> projectiles, float leftBound, float rightBound) {
        super(posX, posY, 16*ZOOM, 20*ZOOM, leftBound, rightBound, SPEED,INTERSECT_TOLERANCE);

        this.hero = hero;
        damage = DAMAGE;

        int frameWidth = 16;
        int frameHeight = 20;

        loadTextures();

        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(spriteSheet, 0, 0, frameWidth, frameHeight);
        frames[1] = new TextureRegion(spriteSheet, frameWidth, 0, frameWidth, frameHeight);

        animation = new Animation<>(0.3f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);



        setProjectileArrayList(projectiles);

        setTextureRegion(new TextureRegion(spriteSheet, 0, 0, frameWidth, frameHeight));
    }

    @Override
    public void updateTimers(float delta){
        stateTime+= delta;
        shootCooldown = Math.max(shootCooldown-delta,0);
    }



    @Override
    public void update(double delta){
        super.update(delta);

        float displacementToHeroX = (hero.getPosX()+hero.getWidth()*0.5f-posX-width*0.5f);
        float displacementToHeroY = (hero.getPosY()+hero.getHeight()*0.5f-posY-height*0.5f);
        float euclDistanceToHero = (float) Math.sqrt(Math.pow(displacementToHeroX,2)+Math.pow(displacementToHeroY,2));

        if (euclDistanceToHero<NOTICE_DISTANCE && shootCooldown<=0) shootAtHero(displacementToHeroX,displacementToHeroY,euclDistanceToHero);

    }

    private void shootAtHero(float displacementToHeroX, float displacementToHeroY, float euclDistanceToHero){
        float directionX = displacementToHeroX /(euclDistanceToHero);
        float directionY = 0.9f * displacementToHeroY /(euclDistanceToHero);

        float projectilePosX = posX;

        if (this.getCurrentDirection()==Direction.RIGHT) projectilePosX+=width;

        Projectile projectile = new Projectile(projectilePosX,posY+0.5f*height,directionX*PROJECTILE_SPEED,directionY*PROJECTILE_SPEED,DAMAGE,Projectile.projectileType.FIREMAGE);
        projectiles.add(projectile);
        shootCooldown=SHOOT_COOLDOWN;
    }

    @Override
    public TextureRegion getCurrentFrame(){
        return animation.getKeyFrame(stateTime);
    }

    @Override
    public void draw(Batch batch){
        batch.setColor(Color.RED);
        super.draw(batch);
        batch.setColor(Color.WHITE);
    }

    public void dispose(){
        if (spriteSheet!=null) spriteSheet.dispose();
    }
}
