package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Coin extends Entity {
    static float COINWIDTH = 10;
    static float COINHEIGHT = 10;


    private int value;

    public Coin(float posX, float posY, int value){
        super(posX, posY, COINWIDTH*ZOOM, COINHEIGHT*ZOOM);
        this.value = value;
        setFrame(new TextureRegion(new Texture("assets/TextureUnknown.png")));
        setAffectedByGravity(false);
    }

    public int getValue() {
        return value;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch, Color.YELLOW);
    }
}
