package ruairi.nea;


import java.util.ArrayList;

import static com.ruairi.nea2026.State.*;

public class Hero extends Sprite{
    //Define Constants
    public static final float JUMPSTRENGTH = -10;
    public static final float MAXCOYOTETIME = 0.2f;



    double coyoteTime=0;
    int health;
    boolean onGround;
    Weapon currentWeapon;

    public Hero(float posX, float posY, float velocityX, float velocityY, float width, float height) {
        super(posX, posY, velocityX, velocityY, width, height);
        health=100;
    }




    public void jump(){
        //Handle logic for jumping
        if (onGround){
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
        velocityX=0;
        if (input.contains("A")){
            velocityX-=1;
        }
        if (input.contains("D")){
            velocityX+=1;
        }
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
