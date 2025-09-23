package ruairi.nea;


import java.util.ArrayList;


public abstract class Sprite {
    public Hitbox hitbox;
    public State CurrentState;
    public Direction currentDirection;

    float posX, posY;
    float velocityX, velocityY;
    boolean visibility;

    public Sprite(float posX, float posY, float width, float height){
        this.posX=posX;
        this.posY=posY;
        hitbox = new Hitbox(posX, posY, width, height);
    }

    public void update(double deltaSeconds){
        float oldX = posX;
        float oldY = posY;
        posX += (float) (velocityX * deltaSeconds);
        posY += (float) (velocityY * deltaSeconds);
//        handleCollisions(oldX, oldY, posX, posY);
        hitbox.setPosition(posX, posY);
    }
    public void render(double secondsElapsed, Direction oldDirection){
    }

    public void setCurrentState(State currentState, Direction direction) {
        CurrentState = currentState;
        currentDirection = direction;
    }

    public void setCurrentState(State currentState){
        CurrentState = currentState;
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


}
