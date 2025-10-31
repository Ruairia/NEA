package ruairi.nea.gameClasses;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.State.*;

public class Hero extends Sprite {
    //Define Constant-like variables
    public static final float JUMPSTRENGTH = 250;
    public static final float JUMPTIME = 0.3f;


    int health;

    Weapon currentWeapon;


    public Hero(float posX, float posY, float width, float height) {
        super(posX, posY, width, height);
        health=100;
    }


    public void update(float delta){
        super.update(delta);
        ArrayList<String> inputs = new ArrayList<>();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) inputs.add("W");
        if (Gdx.input.isKeyPressed(Input.Keys.A)) inputs.add("A");
        if (Gdx.input.isKeyPressed(Input.Keys.S)) inputs.add("S");
        if (Gdx.input.isKeyPressed(Input.Keys.D)) inputs.add("D");

        move(inputs);
    }



    public void jump(){
        //Handle logic for jumping
        if (lastOnGround<JUMPTIME){
            velocityY= JUMPSTRENGTH;
            isOnGround=false;
        }
    }

    public void crouch(){
        setCurrentState(CROUCHING); //Keeps same direction as before
    }

    public void move(ArrayList<String> input){

        if (input.contains("A") && !input.contains("D")){
            velocityX= - 100;
        }
        else if (input.contains("D") && !input.contains("A")){
            velocityX= 100;
        }
        else         velocityX=0;
        if (input.contains("W")){
            jump();
        }
        else if (input.contains("S")){
            crouch();
        }
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
