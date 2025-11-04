package ruairi.nea.gameClasses;


import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.Direction.*;
import static ruairi.nea.gameClasses.State.*;

public class Hero extends Sprite {
    private InputHandler inputHandler = new InputHandler();

    //Define Constant-like variables
    public static final float JUMPSTRENGTH = 400;
    public static final float JUMPTIME = 0.3f;
    public static final float MAXSPEED = 200;


    Texture idle = new Texture("assets/WizardIdle.png");
    Texture walking = new Texture("assets/WizardWalk.png");
    Texture inAir = new Texture("assets/WizardInAir.png");
    Texture attack = new Texture("assets/WizardAttack.png");

    int health;

    Weapon currentWeapon;


    public Hero(float posX, float posY, float width, float height) {
        super(posX, posY, width, height);
        health=100;
        setTexture(idle);
        setVisibility(true);
    }


    public void update(float delta){
        super.update(delta);

        move();

        setTexture(
                switch (getCurrentState()){
            case Idle -> idle;
            case InAir -> inAir;
            case Attacking -> attack;
            case Walking -> walkFrameSwitcher();

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
            if (isOnGround) setCurrentState(Walking, LEFT);
        }
        else if (input.contains("RIGHT") && !input.contains("LEFT")){
            velocityX= MAXSPEED*inputHandler.horizontalAxisStrength;
            if (isOnGround) setCurrentState(Walking, RIGHT);
        }
        else {
            if (isOnGround) setCurrentState(Idle);
            else setCurrentState(InAir);
            velocityX=0;
        }
        if (input.contains("JUMP")){
            jump();
        }
        if (input.contains("ATTACK")) setCurrentState(Attacking);
        if (!isOnGround) setCurrentState(InAir);
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
}
