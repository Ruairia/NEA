package ruairi.nea.gameClasses.Entities.Enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Entities.Projectile;
import ruairi.nea.gameClasses.Entities.Entity;
import ruairi.nea.gameClasses.Entities.Hero;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class FireMage extends PacingEnemy{

    private final Hero hero;
    private static final String SPRITESHEET_PATH = "assets/FireMageSpriteSheet.png";
    public static final Color COLOUR = new Color(1,0.5f,0.5f,1);

    private static final float SPEED = 50*ZOOM;
    private static final float NOTICE_DISTANCE = 300*ZOOM;
    private static final float PROJECTILE_SPEED = 100*ZOOM;
    private static final float INTERSECT_TOLERANCE = 10;
    private static final int DAMAGE = 20;
    private static final float SHOOT_COOLDOWN = 1.25f;

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
        super(posX, posY, 32*ZOOM, 48*ZOOM, leftBound, rightBound, SPEED,INTERSECT_TOLERANCE,PaceDirection.HORIZONTAL);

        this.hero = hero;
        contactDamage = DAMAGE;

        int frameWidth = 32;
        int frameHeight = 48;

        loadTextures();

        TextureRegion[][] frames = TextureRegion.split(spriteSheet, frameWidth, frameHeight);

        animation = new Animation<>(0.1f, frames[0]);

        animation.setPlayMode(Animation.PlayMode.LOOP);



        setProjectileArrayList(projectiles);

        setFrame(new TextureRegion(spriteSheet, 0, 0, frameWidth, frameHeight));
    }

    @Override
    public void updateTimers(float delta){
        super.updateTimers(delta);
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
        float directionY = 0.75f * displacementToHeroY /(euclDistanceToHero);

        float projectilePosX = posX;

        if (this.getCurrentDirection()== Entity.Direction.LEFT) projectilePosX+=width;

        Projectile projectile = new Projectile(
                projectilePosX,posY+0.5f*height
                ,directionX*PROJECTILE_SPEED,directionY*PROJECTILE_SPEED,
                DAMAGE,null,Projectile
                .projectileType.FIRE_MAGE, Projectile.Origin.FIRE_MAGE);
        projectiles.add(projectile);

        shootCooldown=SHOOT_COOLDOWN;
    }

    @Override
    public TextureRegion getCurrentFrame(){
        return animation.getKeyFrame(stateTime);
    }


    public void dispose(){
        if (spriteSheet!=null) spriteSheet.dispose();
    }
}
