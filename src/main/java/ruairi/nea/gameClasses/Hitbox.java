package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import ruairi.nea.gameClasses.Entities.Entity;

public class Hitbox {

    static Texture texture = new Texture("assets/TextureUnknown.png");

    public final Entity owner;

    private float posX;
    private float posY;
    private float width;
    private float height;
    private float oldX;
    private float oldY;
    private float offsetX=0;
    private float offsetY=0;


    public Hitbox(float posX, float posY, float width, float height, Entity owner){
        this.posX=posX;
        this.posY=posY;
        oldX=posX;
        oldY=posY;
        this.width=width;
        this.height=height;
        this.owner = owner;
    }

    public void draw(Batch batch){
        batch.setColor(1,1,1,0.3f);
        batch.draw(texture,posX,posY,width,height);
        batch.setColor(Color.WHITE);
    }

    public void updatePositions(){
        setPosX(owner.getPosX() + offsetX);
        setPosY(owner.getPosY() + offsetY);
        setOldX(owner.getOldX());
        setOldY(owner.getOldY());
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

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
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
