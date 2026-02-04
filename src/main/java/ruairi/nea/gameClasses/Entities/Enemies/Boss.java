package ruairi.nea.gameClasses.Entities.Enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Entities.Goal;
import ruairi.nea.gameClasses.Entities.Platform;
import ruairi.nea.gameClasses.Entities.Projectile;
import ruairi.nea.gameClasses.Hitbox;
import ruairi.nea.gameClasses.Level.Level;

import java.util.Arrays;
import java.util.HashMap;

import static ruairi.nea.gameClasses.Entities.Enemies.BossAI.*;
import static ruairi.nea.gameClasses.Entities.Enemies.BossAI.BossState.*;
import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Boss extends Enemy{

    public static final String SPRITESHEET_PATH = "assets/BossSpriteSheet.png";
    public static final Texture spriteSheet = new Texture(SPRITESHEET_PATH);

    HashMap<BossState, Animation<TextureRegion>> animations = new HashMap<>();
    public static final int FRAME_WIDTH = 160;
    public static final int FRAME_HEIGHT = 128;

    public static final int MAX_HEALTH = 1000;

    public static final int PROJECTILE_DAMAGE = 10;
    public static final float PROJECTILE_SPEED = 200*ZOOM;
    public static final int EXPLOSIVE_PROJECTILE_DAMAGE = 5;
    public static final float EXPLOSIVE_PROJECTILE_SPEED = 180*ZOOM;
    public static final int EXPLOSIVE_PROJECTILE_EXPLOSION_DAMAGE = 40;


    private float stateTime = 0;

    private final Level level;

    private BossAI.BossState previousState = IDLE;
    private BossAI.BossState currentState = IDLE;

    public Boss(float posX, float posY, Level level){
        super(posX,posY,FRAME_WIDTH*ZOOM, FRAME_HEIGHT *ZOOM,10);
        this.level = level;

        health = MAX_HEALTH;
        contactDamage = 10;

        setHitbox(new Hitbox(posX,posY,FRAME_WIDTH*ZOOM,FRAME_HEIGHT*ZOOM,this));

        hitbox.setLeftOffsetX(64*ZOOM);
        hitbox.setRightOffsetX(64*ZOOM);
        hitbox.setTopOffsetY(74*ZOOM);
        hitbox.setBottomOffsetY(12*ZOOM);

        hurtbox.setLeftOffsetX(74*ZOOM);
        hurtbox.setRightOffsetX(74*ZOOM);
        hurtbox.setTopOffsetY(74*ZOOM);
        hurtbox.setBottomOffsetY(12*ZOOM);

        collisionBox.setLeftOffsetX(64*ZOOM);
        collisionBox.setRightOffsetX(64*ZOOM);
        collisionBox.setTopOffsetY(64*ZOOM);
        collisionBox.setBottomOffsetY(12*ZOOM);


        loadAllWeights();
        loadAnimations();
        setStateLengths();
    }

    public void transitionToState(BossState state){
        stateTime=0;
        hasContactDamage=true;
        invulnerable=false;

        int directionToPlayer = getDirectionToPlayer();

        if (previousState==SHOOT) shootAtHero();
        if (previousState==SHOOT_EXPLOSIVE) shootExplosiveAtHero();

        switch (state){
            case IDLE, SHOOT, SHOOT_EXPLOSIVE -> velocityX=0;
            case WALK_TOWARDS -> velocityX=100*directionToPlayer;
            case WALK_AWAY -> velocityX=-100*directionToPlayer;
            case DASH -> velocityX=300*directionToPlayer;
            case JUMP -> jump(directionToPlayer);
            case TELEPORT -> {
                if (Math.abs(level.getHero().getPosX()-posX)>300) return;
                teleport();
            }
        }
    }

    public int getDirectionToPlayer() {
        if (level.getHero().getPosX()<posX+width/2) return -1;
        return 1;
    }

    public void teleport() {
        float oldX = posX;
        float oldY = posY;

        hasContactDamage=false;
        invulnerable=true;
        int directionToPlayer = getDirectionToPlayer();
        velocityX=0;
        posX=level.getHero().getPosX()+75* directionToPlayer - width/2;
        posY=level.getHero().getPosY()+16;
        facePlayer();

        updateHitbox();

        for (Platform platform : level.platforms){
            if (platform.getCollisionBox().intersects(this.getCollisionBox())){
                posX=oldX;
                posY=oldY;
            }
        }
    }

    private void facePlayer() {
        if (getDirectionToPlayer() ==1) setCurrentDirection(Direction.RIGHT);
        else setCurrentDirection(Direction.LEFT);
    }

    public void jump(int directionToPlayer) {
        velocityX=200* directionToPlayer;
        if (isOnGround()) {
            velocityY=500;
        }
    }

    public void shootExplosiveAtHero(){
        facePlayer();
        float displacementToHeroX = level.getHero().getPosX()+level.getHero().getWidth()/2-posX-width/2;
        float displacementToHeroY = level.getHero().getPosY()+level.getHero().getHeight()/2-posY-height/2;
        float euclDistanceToHero = (float) Math.sqrt(Math.pow(displacementToHeroX,2)+Math.pow(displacementToHeroY,2));

        float directionX = displacementToHeroX /(euclDistanceToHero);
        float directionY = 0.75f * displacementToHeroY /(euclDistanceToHero);

        float projectilePosX = posX+width/2;
        float projectilePosY = posY+height/2;



        Projectile projectile =  new Projectile
                (projectilePosX,projectilePosY
                        , directionX *PROJECTILE_SPEED*0.5f, directionY*EXPLOSIVE_PROJECTILE_SPEED,
                        EXPLOSIVE_PROJECTILE_DAMAGE,level,Projectile.projectileType.BOSS_EXPLOSIVE, Projectile.Origin.BOSS);

        level.projectiles.add(projectile);
    }



    public void shootAtHero(){
        facePlayer();
        float displacementToHeroX = level.getHero().getPosX()+level.getHero().getWidth()/2-posX-width/2;
        float displacementToHeroY = level.getHero().getPosY()+level.getHero().getHeight()/2-posY-height/2;
        float euclDistanceToHero = (float) Math.sqrt(Math.pow(displacementToHeroX,2)+Math.pow(displacementToHeroY,2));

        float directionX = displacementToHeroX /(euclDistanceToHero);
        float directionY = 0.75f * displacementToHeroY /(euclDistanceToHero);

        float projectilePosX = posX+width/2;
        float projectilePosY = posY+height/2;

        Projectile projectile;


        projectile = new Projectile
                (projectilePosX,projectilePosY
                        ,directionX*PROJECTILE_SPEED, directionY*PROJECTILE_SPEED,
                        PROJECTILE_DAMAGE, level, Projectile.projectileType.BOSS, Projectile.Origin.BOSS);
        level.projectiles.add(projectile);



    }



    public void loadAnimations(){
        TextureRegion[][] frames = TextureRegion.split(spriteSheet,FRAME_WIDTH,FRAME_HEIGHT);

        Animation<TextureRegion> basicAnimation = new Animation<>(0.1f,Arrays.copyOfRange(frames[0],0,7));

        for (BossState state : BossState.values()){
            animations.put(state, basicAnimation);
        }

        Animation<TextureRegion> walkAnimation = new Animation<>(0.1f, Arrays.copyOfRange(frames[0],0,7));
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        animations.put(WALK_TOWARDS,walkAnimation);
        animations.put(WALK_AWAY,walkAnimation);

        Animation<TextureRegion> shootAnimation = new Animation<>(0.025f, Arrays.copyOfRange(frames[2],0,12));
        animations.put(SHOOT,shootAnimation);
        animations.put(SHOOT_EXPLOSIVE,shootAnimation);

        Animation<TextureRegion> dashAnimation = new Animation<>(0.05f, frames[4]);
        dashAnimation.setPlayMode(Animation.PlayMode.LOOP);
        animations.put(DASH,dashAnimation);
    }

    public void setStateLengths(){
        for (BossState state : BossState.values()){
            stateLengths.put(state,animations.get(state).getAnimationDuration());
        }
        stateLengths.put(DASH,0.8f);
        stateLengths.put(TELEPORT,0.5f);
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        if (Math.abs(level.getHero().getPosX()-posX-width/2)>500) return;
        if (frozenTimer>0) return;

        if (posY<0) {
            teleport();
        }

        if (stateTime>stateLengths.get(currentState)){
            previousState=currentState;

            if (currentState==JUMP&&!isOnGround()) return;

            currentState = getNextState(previousState);
            transitionToState(currentState);
        }
    }

    @Override
    public void kill(float stickAroundTime) {
        super.kill(stickAroundTime);
        spawnGoal();
    }

    private void spawnGoal(){
        Goal goal = new Goal(posX+width/2,posY+getCollisionBox().getBottomOffsetY());
        level.checkpoints.add(goal);
        level.allEntities.add(goal);
    }

    @Override
    protected void updateVelocity(double delta) {
        if (frozenTimer<=0) super.updateVelocity(delta);
    }

    @Override
    protected void updateTimers(float delta) {
        super.updateTimers(delta);
        if (frozenTimer<=0) stateTime+=delta;
    }

    @Override
    public TextureRegion getCurrentFrame() {
        return animations.get(currentState).getKeyFrame(stateTime);
    }

    @Override
    public void draw(Batch batch){
        if (invulnerable) super.draw(batch, new Color(0.2f,0.05f,0.05f,0.5f));
        else super.draw(batch);
    }

    public BossState getPreviousState() {
        return previousState;
    }

    public BossState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(BossState state){
        currentState=state;
    }
}
