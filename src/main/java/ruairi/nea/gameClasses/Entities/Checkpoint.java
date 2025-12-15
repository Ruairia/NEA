package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Checkpoint extends Entity{

    public static final String SPRITESHEET_PATH = "assets/CheckpointSpriteSheet.png";
    public static final Texture spriteSheet = new Texture(SPRITESHEET_PATH);
    public static Animation<TextureRegion> animation;
    public static final int TEXTURE_WIDTH = 16;
    public static final int TEXTURE_HEIGHT = 24;
    private float stateTime = 0;

    public Checkpoint(float posX, float posY) {
        super(posX, posY, TEXTURE_WIDTH*ZOOM, TEXTURE_HEIGHT*ZOOM);
        loadAnimation();
        setAffectedByGravity(false);
    }

    private static void loadAnimation(){
        if (animation!=null) return;
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        animation = new Animation<>(0.1f, frames[0]);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void collect(Hero hero){
        hero.setSpawnPoint(posX,posY);
        hero.setHealth(Hero.MAX_HEALTH);
        hero.setMana(Hero.MAX_MANA);
    }

    @Override
    protected void updateTimers(float delta) {
        super.updateTimers(delta);
        stateTime+=delta;
    }

    @Override
    public void draw(Batch batch) {
        Color colour = new Color(0.4f, 1, 0.6f, 1f);
        super.draw(batch, colour);
    }

    @Override
    public TextureRegion getCurrentFrame() {
       return animation.getKeyFrame(stateTime);
    }
}
