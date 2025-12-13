package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Checkpoint extends Entity{
    public Checkpoint(float posX, float posY) {
        super(posX, posY, 64, 64);
        setAffectedByGravity(false);
        setTextureRegion(new TextureRegion(new Texture("assets/texture_unknown.png")));
    }

    public void collect(Hero hero){
        hero.setSpawnPoint(posX,posY);
        hero.setHealth(Hero.MAX_HEALTH);
        hero.setMana(Hero.MAX_MANA);
    }

    @Override
    public void draw(Batch batch){
        batch.setColor(0.5f,1,0.9f,0.9f);
        super.draw(batch);
        batch.setColor(Color.WHITE);
    }
}
