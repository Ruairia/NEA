package ruairi.nea.gameClasses.Entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Combat.FireStaff;
import ruairi.nea.gameClasses.InputHandler;
import ruairi.nea.gameClasses.Combat.Staff;
import ruairi.nea.gameClasses.Level;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;
import static ruairi.nea.gameClasses.Utils.*;

public class Hero extends Entity {
    private final InputHandler inputHandler = new InputHandler();

    //Define Constant-like variables
    private static final String SPRITESHEET_PATH = "assets/WizardSpriteSheetNoStaff.png";
    public static final float JUMP_STRENGTH = 100*ZOOM;
    public static final float MAX_JUMP_DURATION = 0.25f;
    private static final float DOUBLE_JUMP_STRENGTH = 80 * ZOOM;
    public static final float WALK_SPEED = 50*ZOOM;
    public static final int MAX_HEALTH = 100;
    public static final int MAX_MANA = 10;
    public static final float MANA_REGENERATION = 1f;
    private static final int MAX_JUMPS = 2;
    public static final float INVINCIBILITY_DURATION = 0.6f;
    public static final int CAST_AMOUNT = 2;

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
    Animation<TextureRegion> currentAnimation;

    int health;
    float mana;

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

    public Hero(Level level) {
        super(0,0, 16* ZOOM, 16* ZOOM);

        health = MAX_HEALTH;
        mana = MAX_MANA;

        loadAnimations();
        currentAnimation = idleAnimation;
        currentStaff = new FireStaff(this,level.projectiles);
    }

    public Hero spawn() {
        setPosX(spawnPointX);
        setPosY(spawnPointY);
        setVelocityX(0);
        setVelocityY(0);
        setInvincibilityPeriodLeft(0);
        return this;
    }

    public void updateTimers(float delta){
        super.updateTimers(delta);
        stateTime+=delta;

        if (knockbackTimer>0) knockbackTimer-= delta;
        else knockbackTimer=0;

        if (invincibilityPeriodLeft>0) invincibilityPeriodLeft-= delta;
        else invincibilityPeriodLeft=0;

        if (mana<MAX_MANA) mana += (delta*MANA_REGENERATION);
        else mana=MAX_MANA;

        currentStaff.updateCooldownTimer(delta);
    }

    @Override
    protected void updateVelocity(double delta) {
        move(delta);
        super.updateVelocity(delta);
    }

    public void update(double delta){
        super.update(delta);
    }


    private void loadAnimations() {
        spriteSheet = new Texture(SPRITESHEET_PATH);

        int frameWidth = 16;
        int frameHeight = 16;

        TextureRegion[] idleFrames = parseFrames(0,0, frameWidth, frameHeight, spriteSheet, 1);


        TextureRegion[] walkFrames = parseFrames(0,1*frameHeight, frameWidth, frameHeight, spriteSheet, 2);


        TextureRegion[] attackFrames = parseFrames(0,2*frameHeight, frameWidth, frameHeight, spriteSheet, 1);


        TextureRegion[] inAirFrames = parseFrames(0, 3*frameHeight, frameWidth, frameHeight, spriteSheet, 1);

        idleAnimation = createAnimation(idleFrames, 0.1f, Animation.PlayMode.LOOP);
        walkAnimation = createAnimation(walkFrames, 0.2f, Animation.PlayMode.LOOP);
        inAirAnimation = createAnimation(inAirFrames, 0.1f, Animation.PlayMode.LOOP);
        attackAnimation = createAnimation(attackFrames, 0.25f, Animation.PlayMode.NORMAL);
    }




    @Override
    public TextureRegion getCurrentFrame(){
        if (stateTime>=currentAnimation.getAnimationDuration() || currentAnimation!=attackAnimation) {
        currentAnimation = switch (currentState) {
            case IDLE -> idleAnimation;
            case WALKING -> walkAnimation;
            case IN_AIR -> inAirAnimation;
            case ATTACKING -> {if (isOnGround) yield attackAnimation; else yield inAirAnimation;}
        };
        }
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
            velocityY = (jumpsRemaining == MAX_JUMPS) ? JUMP_STRENGTH : DOUBLE_JUMP_STRENGTH;
            isOnGround = false;
            jumpsRemaining--;
            currentJumpTime = 0;
            if (jumpsRemaining == 0) {
                inputHandler.vibrateController(200,0.25f);
            }
        }
    }

    public void holdJump(double delta){
        if (currentJumpTime< MAX_JUMP_DURATION) {
            currentJumpTime+= (float) delta;
            if (jumpsRemaining == 1) {if (velocityY < JUMP_STRENGTH *0.8) velocityY = (float) (JUMP_STRENGTH *0.8);}
            else if (velocityY<DOUBLE_JUMP_STRENGTH*0.8) velocityY = (float) (DOUBLE_JUMP_STRENGTH*0.8);
        }
    }

    public void move(double delta){
        State previousState = currentState;

        ArrayList<String> input = inputHandler.getInputs();

        if (input.contains("LEFT") && !input.contains("RIGHT")){
            if (knockbackTimer==0) velocityX= -WALK_SPEED *inputHandler.horizontalAxisStrength;
            if (isOnGround) setCurrentState(State.WALKING, Direction.LEFT);
        }
        else if (input.contains("RIGHT") && !input.contains("LEFT")){
            if (knockbackTimer==0) velocityX= WALK_SPEED *inputHandler.horizontalAxisStrength;
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
        } else {
            currentJumpTime=MAX_JUMP_DURATION;
        }
        if (input.contains("ATTACK") && currentStaff.getCooldown()==0) {
            if (mana>=CAST_AMOUNT) {
            setCurrentState(State.ATTACKING);

                currentStaff.attack();
                mana-=CAST_AMOUNT;
            velocityX/=10;
            }
        }
        if (!isOnGround) {
            if (currentState!=State.ATTACKING) setCurrentState(State.IN_AIR);
            if (jumpsRemaining>0&&currentJumpTime> MAX_JUMP_DURATION) jumpsRemaining=1;
        }
        else currentJumpTime=0;
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

    public float getMana() {
        return mana;
    }

    public void setMana(float mana) {
        this.mana = mana;
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

    @Override
    public String toString() {
        return "Hero{" +
                "currentState=" + currentState +
                ", health=" + health +
                ", mana=" + mana +
                ", jumpsRemaining=" + jumpsRemaining +
                ", currentJumpTime=" + currentJumpTime +
                ", invincibilityPeriodLeft=" + invincibilityPeriodLeft +
                ", knockbackTimer=" + knockbackTimer +
                ", lastOnGround=" + lastOnGround +
                ", isOnGround=" + isOnGround +
                ", posX=" + posX +
                ", posY=" + posY +
                ", velocityX=" + velocityX +
                ", velocityY=" + velocityY +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
