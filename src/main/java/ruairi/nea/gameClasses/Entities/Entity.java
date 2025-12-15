package ruairi.nea.gameClasses.Entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;


public abstract class Entity {
    public static final float MAXFALLSPEED = -180*ZOOM; // Pixels per second
    public static final float GRAVITY = 240*ZOOM; //Pixels per second squared

    public enum Direction {
        LEFT,
        RIGHT
    }

    private Platform stoodOnPlatform = null;
    private Float timeUntilRemoval = null;


    private Direction currentDirection = Direction.RIGHT;

    private TextureRegion frame;

    boolean isOnGround;
    boolean isAffectedByGravity=true;


    protected float posX, posY;
    protected float oldX, oldY;
    protected float velocityX = 0;
    protected float velocityY = 0;
    protected float width;
    protected float height;



    public Entity(float posX, float posY, float width, float height){
        this.posX=posX;
        this.posY=posY;
        this.width=width;
        this.height=height;
    }

    public void update(double delta){

        updateTimers((float) delta);
        if (timeUntilRemoval!=null) return;

        oldX = posX;
        oldY = posY;

        applyGravity(delta);
        updatePosition(delta);
        updateVelocity(delta);
        capVerticalVelocity(delta);
        updateDirection();


        if (!isOnGround){
            setStoodOnPlatform(null);
        }
    }

    protected void updateTimers(float delta) {
        if (timeUntilRemoval!=null){
            timeUntilRemoval-=delta;
            if (timeUntilRemoval<0) timeUntilRemoval=0f;
        }
    }

    protected void applyGravity(double delta) {
        if  (isAffectedByGravity) velocityY -= (float) (GRAVITY* delta);
    }

    protected void capVerticalVelocity(double delta){
        if (velocityY < MAXFALLSPEED){
            velocityY = MAXFALLSPEED;
        }
    }

    protected void updatePosition(double delta) {
        posX = (float) (oldX + velocityX * delta);
        posY = (float) (oldY + velocityY * delta);
    }
    protected void updateVelocity(double delta) {
    }

    protected void updateDirection(){
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

    public void draw(Batch batch, Color color){
        batch.setColor(color);
        if (this.getCurrentDirection() == Direction.RIGHT)
            batch.draw(this.getCurrentFrame(), this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight());
        else
            batch.draw(this.getCurrentFrame(), this.getPosX() + this.getWidth(), this.getPosY(), -this.getWidth(), this.getHeight());
        batch.setColor(Color.WHITE);
    }


        public Float getTimeUntilRemoval() {
            return timeUntilRemoval;
        }

        public void setTimeUntilRemoval(Float timeUntilRemoval) {
            this.timeUntilRemoval = timeUntilRemoval;
        }

    public String toString(){
        return "posX: " + posX + " posY: " + posY + " velocityX: " + velocityX + " velocityY: " + velocityY;
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    public void setOnGround(boolean onGround) {
        isOnGround = onGround;
    }

    public float getOldX() {
        return oldX;
    }

    public float getOldY() {
        return oldY;
    }

    public boolean isAffectedByGravity() {
        return isAffectedByGravity;
    }

    public void setAffectedByGravity(boolean affectedByGravity) {
        isAffectedByGravity = affectedByGravity;
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

    public TextureRegion getCurrentFrame() {
        return frame;
    }

    public void setFrame(TextureRegion frame) {
        this.frame = frame;
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

    public Platform getStoodOnPlatform() {
        return stoodOnPlatform;
    }

    public void setStoodOnPlatform(Platform stoodOnPlatform) {
        this.stoodOnPlatform = stoodOnPlatform;
    }

    public void dispose(){this.frame.getTexture().dispose();}
}
