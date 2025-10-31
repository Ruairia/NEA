package ruairi.nea.gameClasses;


import com.badlogic.gdx.graphics.Texture;


public abstract class Sprite {
    //Define Constant-like variables
    public static final float MAXFALLSPEED = -700f; // Pixels per second
    public static final float GRAVITY = 700f; //Pixels per second squared


    private State CurrentState;
    private Direction currentDirection;
    private Texture texture;


    float lastOnGround=0;
    boolean isOnGround;


    float posX, posY;
    float oldX, oldY;
    float velocityX = 0;
    float velocityY = 0;
    float width =0;
    float height =0;
    boolean visibility;
    boolean fixed;


    public Sprite(float posX, float posY, float width, float height){
        this.posX=posX;
        this.posY=posY;
        this.width=width;
        this.height=height;


        this.visibility=true;
    }

    public void update(double delta){
        oldX = posX;
        oldY = posY;

        if  (!fixed) velocityY -= (float) (GRAVITY*delta);

        posX += (float) (velocityX * delta);
        posY += (float) (velocityY * delta);

        if (velocityY < MAXFALLSPEED){
            velocityY = MAXFALLSPEED;
        }

        if (!isOnGround) {
            lastOnGround += (float) delta;
        }
        handleBoundsCollision(this);


        if (velocityX<0){
            currentDirection = Direction.LEFT;
        }
        else if (velocityX>0){
            currentDirection = Direction.RIGHT;
        }

    }


    public static boolean intersect(Sprite first, Sprite second){
        return
                first.posX < second.posX + second.width
                        &&
                        first.posX + first.width > second.posX
                        &&
                        first.posY < second.posY + second.height
                        &&
                        first.posY + first.height > second.posY;
    }






    public static void handleBoundsCollision(Sprite sprite) {
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
}
