package ruairi.nea.gameClasses;


import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Hero extends Entity {
    private InputHandler inputHandler = new InputHandler();

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

    Texture idle = new Texture("assets/WizardIdle.png");
    Texture walking = new Texture("assets/WizardWalk.png");
    Texture inAir = new Texture("assets/WizardInAir.png");
    Texture attack = new Texture("assets/WizardAttack.png");

    int health;

    float invincibilityPeriodLeft = 0;



    Weapon currentWeapon;



    float spawnPointX = 100;
    float spawnPointY = 100;

    public Hero() {
        super(0,0, 80, 80);
        health=MAXHEALTH;
        setTexture(idle);
        setVisibility(true);
    }

    public Hero spawn() {
        setPosX(spawnPointX);
        setPosY(spawnPointY);
        return this;
    }


    public void update(double delta){
        super.update(delta);

        if (invincibilityPeriodLeft>0) invincibilityPeriodLeft-=delta;
        else invincibilityPeriodLeft=0;

        move();

        setTexture(
                switch (currentState){
            case IDLE -> idle;
            case IN_AIR -> inAir;
            case ATTACKING -> attack;
            case WALKING -> walkFrameSwitcher();

                }
        );

    }



    public Texture walkFrameSwitcher(){
        float walkFrameDuration = 0.2f; // Seconds
        if (((double) System.currentTimeMillis() /1000) % (walkFrameDuration*2)> walkFrameDuration) return walking;
        else return idle;
    }

    public void jump(){
        //Handle logic for jumping
        if (lastOnGround<JUMPTIME){
            velocityY= JUMPSTRENGTH;
            isOnGround=false;
        }
    }

    public void move(){

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
}
