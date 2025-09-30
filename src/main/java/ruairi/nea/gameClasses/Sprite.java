package ruairi.nea.gameClasses;


import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;


public abstract class Sprite {
    private Hitbox hitbox;
    private State CurrentState;
    private Direction currentDirection;

    float lastOnGround=0;

    private Texture texture;

    float posX, posY;
    float velocityX = 0;
    float velocityY = 0;
    boolean visibility;

    public Sprite(float posX, float posY, float width, float height){
        this.posX=posX;
        this.posY=posY;
        this.visibility=true;
        hitbox = new Hitbox(posX, posY, width, height);
    }

    public void update(double delta){
        float oldX = posX;
        float oldY = posY;

        velocityY -= (float) (100*delta);

        posX += (float) (velocityX * delta);
        posY += (float) (velocityY * delta);
//        handleCollisions(oldX, oldY, posX, posY);


        lastOnGround+= (float) delta;

        handleBasicCollision(this);

        hitbox.setPosition(posX, posY);
    }



    public void resolveCollision(Hitbox other, float oldX, float oldY){
        if (oldY + hitbox.height <= other.topLeftY && posY + hitbox.height >= other.topLeftY){}
    }

    public void handleCollisions(ArrayList<Sprite> objectsToBeChecked, int oldX, int oldY) {
        for (Sprite other : objectsToBeChecked) {
            if (Hitbox.intersect(other.hitbox, this.hitbox)) {
                resolveCollision(other.hitbox, oldX, oldY);
            }
        }
    }

    public static void handleBasicCollision(Sprite sprite) {
        if (sprite.posY < 0) {
            sprite.velocityY = Math.max(sprite.velocityY, 0);
            sprite.posY = 0;
            sprite.lastOnGround = 0;
        }
    }




    public void setCurrentState(State currentState, Direction direction) {
        CurrentState = currentState;
        currentDirection = direction;
    }


    public String toString(){
        return "posX: " + posX + " posY: " + posY + " velocityX: " + velocityX + " velocityY: " + velocityY + " visibility: " + visibility;
    }

    public void setCurrentState(State currentState){
        CurrentState = currentState;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public State getCurrentState() {
        return CurrentState;
    }

    public Hitbox getHitbox(){
        return hitbox;
    }

    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setHitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }
}
