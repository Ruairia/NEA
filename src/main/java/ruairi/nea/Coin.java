package ruairi.nea;

public class Coin extends Sprite {
    static float COINWIDTH = 10;
    static float COINHEIGHT = 10;


    int value;
    public Coin(float posX, float posY, int value){
        super(posX, posY, COINWIDTH, COINHEIGHT);
        this.value = value;
    }
    public void collect(){
    }
}
