package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Goal extends Checkpoint{
    public Goal(float posX, float posY) {
        super(posX, posY);
        setTextureRegion(new TextureRegion(new Texture("assets/texture_unknown.png")));
    }

    @Override
    public void draw(Batch batch) {
        batch.setColor(0.4f,0.4f,0.9f,0.9f);
        batch.draw(getCurrentFrame(),posX,posY,width,height);
        batch.setColor(Color.WHITE);
    }
}
