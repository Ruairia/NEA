package ruairi.nea.gameClasses;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.Direction.*;
import static ruairi.nea.gameClasses.State.*;

public class Hero extends Sprite {
    //Define Constant-like variables
    public static final float JUMPSTRENGTH = 400;
    public static final float JUMPTIME = 0.3f;


    Texture idle = new Texture("assets/WizardIdle.png");
    Texture walking = new Texture("assets/WizardWalk.png");
    Texture inAir = new Texture("assets/WizardInAir.png");
    Texture attack = new Texture("assets/WizardAttack.png");

    private float walkFrameDuration = 0.2f; // change frame every 0.2 seconds

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
        ArrayList<String> inputs = new ArrayList<>();
        System.out.println(getCurrentState()+ " " + getCurrentDirection());
        if (Gdx.input.isKeyPressed(Input.Keys.W)) inputs.add("W");
        if (Gdx.input.isKeyPressed(Input.Keys.A)) inputs.add("A");
        if (Gdx.input.isKeyPressed(Input.Keys.D)) inputs.add("D");
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) inputs.add("SHIFT");

        move(inputs);
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
        if (System.nanoTime() % (walkFrameDuration*2000)> walkFrameDuration*1000) return walking;
        else return idle;
    }

    public void jump(){
        //Handle logic for jumping
        if (lastOnGround<JUMPTIME){
            velocityY= JUMPSTRENGTH;
            isOnGround=false;
        }
    }

    public void move(ArrayList<String> input){

        if (input.contains("A") && !input.contains("D")){
            velocityX= - 100;
            if (isOnGround) setCurrentState(Walking, LEFT);
        }
        else if (input.contains("D") && !input.contains("A")){
            velocityX= 100;
            if (isOnGround) setCurrentState(Walking, RIGHT);
        }
        else {
            if (isOnGround) setCurrentState(Idle);
            else setCurrentState(InAir);
            velocityX=0;
        }
        if (input.contains("W")){
            jump();
            setCurrentState(InAir);
        }
        if (input.contains("SHIFT")) setCurrentState(Attacking);
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
