package ruairi.nea.gameClasses.Entities;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.applicationClasses.Main;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;


public abstract class Entity {
    //Define Constant-like variables
    public static final float MAXFALLSPEED = -180*ZOOM; // Pixels per second
    public static final float GRAVITY = 240*ZOOM; //Pixels per second squared

    public enum Direction {
        LEFT,
        RIGHT
    }


    private Direction currentDirection = Direction.RIGHT;
    private TextureRegion textureRegion;


    float lastOnGround=0;
    boolean isOnGround;
    boolean isAffectedByGravity=true;


    protected float posX, posY;
    protected float oldX, oldY;
    protected float velocityX = 0;
    protected float velocityY = 0;
    protected float width =0;
    protected float height =0;
    protected boolean visibility;


    public Entity(float posX, float posY, float width, float height){
        this.posX=posX;
        this.posY=posY;
        this.width=width;
        this.height=height;


        this.visibility=true;
    }

    public void update(double delta){
        oldX = posX;
        oldY = posY;

        if  (isAffectedByGravity) velocityY -= (float) (GRAVITY*delta);

        posX += (float) (velocityX * delta);
        posY += (float) (velocityY * delta);

        if (velocityY < MAXFALLSPEED){
            velocityY = MAXFALLSPEED;
        }

        if (!isOnGround) {
            lastOnGround += (float) delta;
        }


        if (velocityX<0){
            currentDirection = Direction.LEFT;
        }
        else if (velocityX>0){
            currentDirection = Direction.RIGHT;
        }

    }


    public  boolean intersect(Entity other){
        return
                        this.posX < other.posX + other.width
                        &&
                        this.posX + this.width > other.posX
                        &&
                        this.posY < other.posY + other.height
                        &&
                        this.posY + this.height > other.posY;
    }





    public void draw(Batch batch) {
        if (this.getCurrentDirection() == Direction.RIGHT)
            batch.draw(this.getCurrentFrame(), this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight());
        else
            batch.draw(this.getCurrentFrame(), this.getPosX() + this.getWidth(), this.getPosY(), -this.getWidth(), this.getHeight());
    }






    public String toString(){
        return "posX: " + posX + " posY: " + posY + " velocityX: " + velocityX + " velocityY: " + velocityY + " visibility: " + visibility;
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    public void setOnGround(boolean onGround) {
        isOnGround = onGround;
    }

    public float getLastOnGround() {
        return lastOnGround;
    }

    public void setLastOnGround(float lastOnGround) {
        this.lastOnGround = lastOnGround;
    }

    public float getOldX() {
        return oldX;
    }

    public float getOldY() {
        return oldY;
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



    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public TextureRegion getCurrentFrame() {
        return textureRegion;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }


    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void dispose(){this.textureRegion.getTexture().dispose();}
}
