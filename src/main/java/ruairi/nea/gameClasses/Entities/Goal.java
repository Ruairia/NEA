package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Goal extends Checkpoint{
    public Goal(float posX, float posY) {
        super(posX, posY);
    }

    @Override
    public void draw(Batch batch) {
        Color colour = new Color(0.4f,0.4f,0.9f,1f);
        super.draw(batch,colour);
        batch.setColor(Color.WHITE);
    }
}
