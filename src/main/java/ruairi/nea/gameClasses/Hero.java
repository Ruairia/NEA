package ruairi.nea.gameClasses;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class Hero extends Entity {
    private final InputHandler inputHandler = new InputHandler();

    //Define Constant-like variables
    public static final float JUMPSTRENGTH = 500;
    public static final float JUMPTIME = 0.25f;
    public static final float MAXSPEED = 250;
    public static final int MAXHEALTH = 100;

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
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> attackAnimation;
    private float stateTime = 0;

    int health;

    float invincibilityPeriodLeft = 0;

    Weapon currentWeapon;

    float spawnPointX = 100;
    float spawnPointY = 100;

    public Hero() {
        super(0,0, 80, 80);
        health=MAXHEALTH;
        loadAnimations();
        setVisibility(true);
    }

    public Hero spawn() {
        setPosX(spawnPointX);
        setPosY(spawnPointY);
        return this;
    }


    public void update(double delta){
        super.update(delta);

        stateTime += (float) delta;

        if (invincibilityPeriodLeft>0) invincibilityPeriodLeft-= (float) delta;
        else invincibilityPeriodLeft=0;

        move();
    }

    private void loadAnimations(){
        spriteSheet = new Texture("assets/WizardSpriteSheet.png");

        int frameWidth = 16;
        int frameHeight = 16;

        TextureRegion[] idleFrames = new TextureRegion[1];
        for (int i = 0; i < 1; i++) {
            idleFrames[i] = new TextureRegion(spriteSheet, i * frameWidth, 0, frameWidth, frameHeight);
        }


        TextureRegion[] walkFrames = new TextureRegion[2];
        for (int i = 0; i < 2; i++) {
            walkFrames[i] = new TextureRegion(spriteSheet, i * frameWidth, frameHeight, frameWidth, frameHeight);
        }


        TextureRegion[] attackFrames = new TextureRegion[1];
        for (int i = 0; i < 1; i++) {
            attackFrames[i] = new TextureRegion(spriteSheet, i * frameWidth, frameHeight * 2, frameWidth, frameHeight);
        }


        TextureRegion[] jumpFrames = new TextureRegion[1];
        for (int i = 0; i < 1; i++) {
            jumpFrames[i] = new TextureRegion(spriteSheet, i * frameWidth, frameHeight * 3, frameWidth, frameHeight);
        }

        idleAnimation = new Animation<>(1, idleFrames);
        walkAnimation = new Animation<>(0.2f, walkFrames);
        attackAnimation = new Animation<>(1, attackFrames);
        jumpAnimation = new Animation<>(1, jumpFrames);

        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        jumpAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    @Override
    public TextureRegion getCurrentFrame(){
        Animation<TextureRegion> currentAnimation = switch (currentState) {
            case IDLE -> idleAnimation;
            case WALKING -> walkAnimation;
            case IN_AIR -> jumpAnimation;
            case ATTACKING -> attackAnimation;
        };
        return  currentAnimation.getKeyFrame(stateTime);
    }



    public void jump(){
        //Handle logic for jumping
        if (lastOnGround<JUMPTIME){
            velocityY= JUMPSTRENGTH;
            isOnGround=false;
        }
    }

    public void move(){
        State previousState = currentState;

        ArrayList<String> input = inputHandler.getInputs();

        if (input.contains("LEFT") && !input.contains("RIGHT")){
            velocityX= - MAXSPEED*inputHandler.horizontalAxisStrength;
            if (isOnGround) setCurrentState(State.WALKING, Direction.LEFT);
        }
        else if (input.contains("RIGHT") && !input.contains("LEFT")){
            velocityX= MAXSPEED*inputHandler.horizontalAxisStrength;
            if (isOnGround) setCurrentState(State.WALKING, Direction.RIGHT);
        }
        else {
            if (isOnGround) setCurrentState(State.IDLE);
            else setCurrentState(State.IN_AIR);
            velocityX=0;
        }
        if (input.contains("JUMP")){
            jump();
        }
        if (input.contains("ATTACK")) setCurrentState(State.ATTACKING);
        if (!isOnGround) setCurrentState(State.IN_AIR);
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

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

    public float[] getSpawnPoint() {
        return new float[]{spawnPointX,spawnPointY};
    }

    public Hero setSpawnPoint(float posX, float posY) {
        this.spawnPointX=posX;
        this.spawnPointY=posY;
        return this;
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
    }
}
