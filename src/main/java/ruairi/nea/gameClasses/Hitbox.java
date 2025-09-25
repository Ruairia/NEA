package ruairi.nea.gameClasses;

public class Hitbox {
    public float topLeftX, topLeftY, width, height;
    public Hitbox(float topLeftX, float topLeftY, float width, float height){
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.width = width;
        this.height = height;
    }
    public static boolean intersect(Hitbox first, Hitbox second){
        return
                first.topLeftX < second.topLeftX + second.width
                &&
                first.topLeftX + first.width > second.topLeftX
                &&
                first.topLeftY < second.topLeftY + second.height
                &&
                first.topLeftY + first.height > second.topLeftY;
    }
    public void setPosition(float x, float y){
        this.topLeftX = x;
        this.topLeftY = y;
    }
    public void setSize(float width, float height){
        this.width = width;
        this.height = height;
    }
    public void setSizeAndMoveUp(float width, float height){
        this.width = width;
        this.height = height;
        this.topLeftY -= height;
    }
}
