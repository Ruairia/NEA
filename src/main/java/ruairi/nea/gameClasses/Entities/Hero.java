package ruairi.nea.gameClasses.Entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Staffs.FireStaff;
import ruairi.nea.gameClasses.InputHandler;
import ruairi.nea.gameClasses.Staffs.Staff;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Hero extends Entity {
    private final InputHandler inputHandler = new InputHandler();

    //Define Constant-like variables
    private static final String SPRITESHEET_PATH = "assets/WizardSpriteSheetNoStaff.png";
    public static final float JUMPSTRENGTH = 100*ZOOM;
    public static final float MAXJUMPTIME = 0.25f;
    private static final float DOUBLE_JUMP_STRENGTH = 80 * ZOOM;
    public static final float WALKSPEED = 50*ZOOM;
    public static final int MAXHEALTH = 100;
    private static final int MAX_JUMPS = 2;

    public enum State {
        IDLE,
        WALKING,
        IN_AIR,
        ATTACKING
    }
    private State currentState = State.IDLE;

    private Texture spriteSheet;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> inAirAnimation;
    private Animation<TextureRegion> attackAnimation;
    private float stateTime = 0;

    int health;
    private int jumpsRemaining = 2;
    private float currentJumpTime=0;
    float invincibilityPeriodLeft = 0;

    private static final float KNOCKBACK_TIMER_MAX=0.15f;
    private float knockbackTimer = 0;
    private static final float KNOCKBACK_STRENGTH_X = 140 * ZOOM;
    private static final float KNOCKBACK_STRENGTH_Y = 80 * ZOOM;

    Staff currentStaff;

    float spawnPointX = 100;
    float spawnPointY = 100;

    public Hero() {
        super(0,0, 16* ZOOM, 16* ZOOM);
        health=MAXHEALTH;
        loadAnimations();
        setVisibility(true);
        currentStaff = new FireStaff(this);
    }

    public Hero spawn() {
        setPosX(spawnPointX);
        setPosY(spawnPointY);
        setVelocityX(0);
        setVelocityY(0);
        setInvincibilityPeriodLeft(0);
        return this;
    }


    public void update(double delta){
        super.update(delta);

        stateTime += (float) delta;

        if (knockbackTimer>0) knockbackTimer-= (float) delta;
        else knockbackTimer=0;

        if (invincibilityPeriodLeft>0) invincibilityPeriodLeft-= (float) delta;
        else invincibilityPeriodLeft=0;

        move(delta);
    }


    private void loadAnimations(){
        spriteSheet = new Texture(SPRITESHEET_PATH);

        int frameWidth = 16;
        int frameHeight = 16;

        TextureRegion[] idleFrames = parseFrames(1, frameWidth, 0, frameHeight,spriteSheet,1);


        TextureRegion[] walkFrames =  parseFrames(2, frameWidth, frameHeight, frameHeight,spriteSheet,2);


        TextureRegion[] attackFrames = parseFrames(1, frameWidth, frameHeight * 2, frameHeight,spriteSheet,1);


        TextureRegion[] inAirFrames = parseFrames(1, frameWidth, frameHeight * 3, frameHeight,spriteSheet,1);

        idleAnimation = new Animation<>(1, idleFrames);
        walkAnimation = new Animation<>(0.2f, walkFrames);
        attackAnimation = new Animation<>(1, attackFrames);
        inAirAnimation = new Animation<>(1, inAirFrames);

        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        inAirAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public static TextureRegion[] parseFrames(int x, int frameWidth, int y, int frameHeight, Texture spriteSheet, int framesNumber) {
        TextureRegion[] frames = new TextureRegion[framesNumber];
        for (int i = 0; i < x; i++) {
            frames[i] = new TextureRegion(spriteSheet, i * frameWidth, y, frameWidth, frameHeight);
        }
        return frames;
    }

    @Override
    public TextureRegion getCurrentFrame(){
        Animation<TextureRegion> currentAnimation = switch (currentState) {
            case IDLE -> idleAnimation;
            case WALKING -> walkAnimation;
            case IN_AIR -> inAirAnimation;
            case ATTACKING -> attackAnimation;
        };
        return  currentAnimation.getKeyFrame(stateTime);
    }


    public void damage(int damage){
        health-=damage;
    }

    public void applyKnockback(Enemy enemy) {
        velocityX = (this.posX < enemy.getPosX()) ? -KNOCKBACK_STRENGTH_X  : KNOCKBACK_STRENGTH_X;

        velocityY = KNOCKBACK_STRENGTH_Y;

        knockbackTimer = KNOCKBACK_TIMER_MAX;
    }




    public void jump(){
        //Handle logic for jumping
        if (jumpsRemaining > 0) {
            velocityY = (jumpsRemaining == MAX_JUMPS) ? JUMPSTRENGTH : DOUBLE_JUMP_STRENGTH;
            isOnGround = false;
            jumpsRemaining--;
            currentJumpTime = 0;
            if (jumpsRemaining == 0) {
                inputHandler.vibrateController(200,0.25f);
            }
        }
    }

    public void holdJump(double delta){
        if (currentJumpTime<MAXJUMPTIME) {
            currentJumpTime+= (float) delta;
            if (jumpsRemaining == 1) {if (velocityY<JUMPSTRENGTH*0.9) velocityY = (float) (JUMPSTRENGTH*0.9);}
            else if (velocityY<DOUBLE_JUMP_STRENGTH*0.9) velocityY = (float) (DOUBLE_JUMP_STRENGTH*0.9);
        }
    }

    public void move(double delta){
        State previousState = currentState;

        ArrayList<String> input = inputHandler.getInputs();

        if (input.contains("LEFT") && !input.contains("RIGHT")){
            if (knockbackTimer==0) velocityX= -WALKSPEED *inputHandler.horizontalAxisStrength;
            if (isOnGround) setCurrentState(State.WALKING, Direction.LEFT);
        }
        else if (input.contains("RIGHT") && !input.contains("LEFT")){
            if (knockbackTimer==0) velocityX= WALKSPEED *inputHandler.horizontalAxisStrength;
            if (isOnGround) setCurrentState(State.WALKING, Direction.RIGHT);
        }
        else {
            if (isOnGround) setCurrentState(State.IDLE);
            else setCurrentState(State.IN_AIR);
            if (knockbackTimer==0) velocityX=0;
        }
        if (input.contains("JUMP")){
            if (isOnGround) jumpsRemaining = MAX_JUMPS;
            jump();
        }
        if (input.contains("HOLDJUMP")){
            holdJump(delta);
        }
        if (input.contains("ATTACK")) setCurrentState(State.ATTACKING);
        if (!isOnGround) {
            setCurrentState(State.IN_AIR);
            if (jumpsRemaining>0&&currentJumpTime>MAXJUMPTIME) jumpsRemaining=1;
        }
        else currentJumpTime=0;
        if (getCurrentState()==State.ATTACKING) velocityX/=5;
        if (currentState!=previousState) stateTime=0;
    }

    public void setCurrentState(State currentState, Direction direction) {
        this.currentState = currentState;
        setCurrentDirection(direction);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState){
        this.currentState = currentState;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Staff getCurrentWeapon() {
        return currentStaff;
    }

    public void setCurrentWeapon(Staff currentStaff) {
        this.currentStaff = currentStaff;
    }

    public float[] getSpawnPoint() {
        return new float[]{spawnPointX,spawnPointY};
    }

    public Hero setSpawnPoint(float posX, float posY) {
        this.spawnPointX=posX;
        this.spawnPointY=posY;
        return this;
    }

    public float getStateTime() {
        return stateTime;
    }

    public float getInvincibilityPeriodLeft() {
        return invincibilityPeriodLeft;
    }

    public void setInvincibilityPeriodLeft(float invincibilityPeriodLeft) {
        this.invincibilityPeriodLeft = invincibilityPeriodLeft;
    }
    @Override
    public void dispose(){
    spriteSheet.dispose();
    currentStaff.dispose();
    }

    @Override
    public void draw(Batch batch){
        if (invincibilityPeriodLeft>0) batch.setColor(Color.SALMON);
        super.draw(batch);
        currentStaff.draw(batch);
        batch.setColor(Color.WHITE);
    }
}
