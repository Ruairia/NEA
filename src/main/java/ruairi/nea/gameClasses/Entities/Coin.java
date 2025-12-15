package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Coin extends Entity {
    public static int TEXTURE_WIDTH = 8;
    public static int TEXTURE_HEIGHT = 8;

    static float COIN_WIDTH = 8;
    static float COIN_HEIGHT = 8;

    public static final String SPRITESHEET_PATH = "assets/CoinSpriteSheet.png";
    private static Texture spriteSheet;
    private static Animation<TextureRegion> animation;
    private float stateTime = 0;

    private final int value;

    public Coin(float posX, float posY, int value){
        super(posX, posY, COIN_WIDTH *ZOOM, COIN_HEIGHT * ZOOM);
        this.value = value;
        loadAnimation();
        setAffectedByGravity(false);
    }

    private static void loadAnimation(){
        if (spriteSheet!=null) return;
        spriteSheet = new Texture(SPRITESHEET_PATH);
        animation = new Animation<>(0.15f,TextureRegion.split(spriteSheet,TEXTURE_WIDTH,TEXTURE_HEIGHT)[0]);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public int getValue() {
        return value;
    }

    @Override
    protected void updateTimers(float delta) {
        super.updateTimers(delta);
        stateTime+=delta;
    }

    @Override
    public TextureRegion getCurrentFrame() {
        return animation.getKeyFrame(stateTime);
    }
}
