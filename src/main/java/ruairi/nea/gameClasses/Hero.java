package ruairi.nea.gameClasses;


import java.util.ArrayList;

import static ruairi.nea.gameClasses.State.*;

public class Hero extends Sprite{
    //Define Constants
    public static final float JUMPSTRENGTH = 200;
    public static final float MAXCOYOTETIME = 0.2f;



    double coyoteTime=0;
    int health;

    Weapon currentWeapon;


    public Hero(float posX, float posY, float width, float height) {
        super(posX, posY, width, height);
        health=100;
    }


    public void update(ArrayList<String> inputs, float delta){
        super.update(delta);

        if (coyoteTime<0) coyoteTime-=delta;

        move(inputs);
    }



    public void jump(){
        //Handle logic for jumping
        if (lastOnGround<1){
            velocityY = JUMPSTRENGTH;
            coyoteTime=MAXCOYOTETIME;
        }
        else if (coyoteTime>0){
            velocityY = JUMPSTRENGTH;
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
