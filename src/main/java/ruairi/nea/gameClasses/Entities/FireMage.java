package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Combat.Projectile;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class FireMage extends PacingEnemy{

    private final Hero hero;
    private static final String SPRITESHEET_PATH = "assets/texture_unknown.png";


    private static final int FRAME_WIDTH = 16;
    private static final int FRAME_HEIGHT = 16;

    private static final float SPEED = 50*ZOOM;
    private static final float NOTICE_DISTANCE = 300*ZOOM;
    private static final float PROJECTILE_SPEED = 100*ZOOM;
    private static final float INTERSECT_TOLERANCE = 10;
    private static final int DAMAGE = 20;
    private static final float SHOOT_COOLDOWN = 1.25f;

    private static Texture spriteSheet;

    private static ArrayList<Projectile> projectiles = null;


    float stateTime = 0;
    float shootCooldown = 0;

    public static void loadTextures(){
        if (spriteSheet==null) spriteSheet = new Texture(SPRITESHEET_PATH);
    }

    public static void setProjectileArrayList(ArrayList<Projectile> projectilesList){
        if (projectiles==null) projectiles=projectilesList;
    }

    public void dispose(){
        if (spriteSheet!=null) spriteSheet.dispose();
    }

    public FireMage(float posX, float posY, Hero hero, ArrayList<Projectile> projectiles, float leftBound, float rightBound) {
        super(posX, posY, 16*ZOOM, 16*ZOOM, leftBound, rightBound, SPEED,INTERSECT_TOLERANCE);

        this.hero = hero;
        damage = DAMAGE;


        loadTextures();
        setProjectileArrayList(projectiles);

        setTextureRegion(new TextureRegion(spriteSheet, 0, 0, FRAME_WIDTH, FRAME_HEIGHT));
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
        float directionY = 0.5f * displacementToHeroY /(euclDistanceToHero);

        float projectilePosX = posX;

        if (this.getCurrentDirection()==Direction.RIGHT) projectilePosX+=width;

        Projectile projectile = new Projectile(projectilePosX,posY+0.5f*height,directionX*PROJECTILE_SPEED,directionY*PROJECTILE_SPEED,DAMAGE);
        projectiles.add(projectile);
        shootCooldown=SHOOT_COOLDOWN;
    }


}
