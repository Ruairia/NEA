package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import ruairi.nea.gameClasses.Combat.Staff;
import ruairi.nea.gameClasses.Entities.Entity;

public class Hitbox {

    static Texture texture = new Texture("assets/TextureUnknown.png");

    private Entity entityOwner = null;
    private Staff staffOwner = null;

    private float posX;
    private float posY;
    private float width;
    private float height;
    private float oldX;
    private float oldY;
    private float leftOffsetX =0;
    private float bottomOffsetY =0;
    private float rightOffsetX =0;
    private float topOffsetY =0;


    public Hitbox(float posX, float posY, float width, float height, Entity entityOwner){
        this.posX=posX;
        this.posY=posY;
        oldX=posX;
        oldY=posY;
        this.width=width;
        this.height=height;
        this.entityOwner = entityOwner;
    }
    public Hitbox(float posX, float posY, float width, float height, Staff staffOwner){
        this.posX=posX;
        this.posY=posY;
        oldX=posX;
        oldY=posY;
        this.width=width;
        this.height=height;
        this.staffOwner=staffOwner;
    }

    public void draw(Batch batch){
        batch.draw(texture,posX,posY,width,height);
    }

    public void update(){
        if (entityOwner==null) return;
        setPosX(entityOwner.getPosX() + leftOffsetX);
        setPosY(entityOwner.getPosY() + bottomOffsetY);
        setWidth(entityOwner.getWidth()-(leftOffsetX+rightOffsetX));
        setHeight(entityOwner.getHeight()-(bottomOffsetY + topOffsetY));
        setOldX(entityOwner.getOldX()+leftOffsetX);
        setOldY(entityOwner.getOldY()+ bottomOffsetY);
    }

    public Entity getEntityOwner() {
        return entityOwner;
    }

    public Staff getStaffOwner() {
        return staffOwner;
    }

    public static boolean staticIntersects(Hitbox a, Hitbox b){
        return a.posX < b.posX + b.width && a.posX + a.width > b.posX && a.posY < b.posY + b.height && a.posY + a.height > b.posY;
    }

    public static boolean staticIntersectsWithTolerance(Hitbox a, Hitbox b, float intersectTolerance){
        return a.posX + intersectTolerance < b.posX + b.width && a.posX + a.width - intersectTolerance > b.posX && a.posY + intersectTolerance < b.posY + b.height && a.posY + a.height - intersectTolerance > b.posY;
    }

    public boolean intersects(Hitbox other){
        return staticIntersects(this,other);
    }

    public float getRightOffsetX() {
        return rightOffsetX;
    }

    public void setRightOffsetX(float rightOffsetX) {
        this.rightOffsetX = rightOffsetX;
    }

    public float getTopOffsetY() {
        return topOffsetY;
    }

    public void setTopOffsetY(float topOffsetY) {
        this.topOffsetY = topOffsetY;
    }

    public float getBottomOffsetY() {
        return bottomOffsetY;
    }

    public void setBottomOffsetY(float bottomOffsetY) {
        this.bottomOffsetY = bottomOffsetY;
    }

    public float getLeftOffsetX() {
        return leftOffsetX;
    }

    public void setLeftOffsetX(float leftOffsetX) {
        this.leftOffsetX = leftOffsetX;
    }

    public float getOldX() {
        return oldX;
    }

    public void setOldX(float oldX) {
        this.oldX = oldX;
    }

    public float getOldY() {
        return oldY;
    }

    public void setOldY(float oldY) {
        this.oldY = oldY;
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
