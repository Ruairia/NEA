package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Staff {
    private static final String SPRITESHEET_PATH = "assets/StaffSpriteSheet.png";
    private static final int FRAME_WIDTH = 16;
    private static final int FRAME_HEIGHT = 16;

    int maxAmmo;
    int currentAmmo;
    int ammoReserves;

    final Color colour;
    final Hero hero;

    private Texture spriteSheet;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> inAirAnimation;
    private Animation<TextureRegion> attackAnimation;


    public Staff(Color colour, Hero hero){
        loadAnimations();
        this.hero = hero;
        this.colour = colour;
    }


    public void draw(Batch batch){

        float posY = hero.getPosY();
        float height = hero.getHeight();
        float stateTime = hero.getStateTime();

        batch.setColor(colour);

        float posX = hero.getPosX();
        float width = hero.getWidth();

        if (hero.getCurrentDirection() == Entity.Direction.LEFT){
            posX += width;
            width = -1 * width;
        }

        batch.draw(getCurrentFrame(stateTime,hero.getCurrentState()),posX,posY,width,height);
        batch.setColor(Color.WHITE);
    }


    public void reload(){
        int reloadAmount;
        if (ammoReserves>maxAmmo) reloadAmount = maxAmmo-currentAmmo;
        else reloadAmount=ammoReserves;
        currentAmmo+=reloadAmount;
        ammoReserves -= reloadAmount;
    }

    private void loadAnimations(){
        spriteSheet = new Texture(SPRITESHEET_PATH);

        TextureRegion[] idleFrames = Hero.parseFrames(1, FRAME_WIDTH, 0, FRAME_HEIGHT,spriteSheet,1);
        TextureRegion[] walkFrames =  Hero.parseFrames(2, FRAME_WIDTH, FRAME_HEIGHT, FRAME_HEIGHT,spriteSheet,2);
        TextureRegion[] attackFrames = Hero.parseFrames(1, FRAME_WIDTH, FRAME_HEIGHT * 2, FRAME_HEIGHT,spriteSheet,1);
        TextureRegion[] inAirFrames = Hero.parseFrames(1, FRAME_WIDTH, FRAME_HEIGHT * 3, FRAME_HEIGHT,spriteSheet,1);

        idleAnimation = createAnimation(1, idleFrames, Animation.PlayMode.LOOP);
        walkAnimation = createAnimation(0.2f, walkFrames, Animation.PlayMode.LOOP);
        attackAnimation = createAnimation(1,attackFrames, Animation.PlayMode.NORMAL);
        inAirAnimation = createAnimation(1,inAirFrames, Animation.PlayMode.NORMAL);
    }

    public Animation<TextureRegion> createAnimation(float frameDuration, TextureRegion[] frames, Animation.PlayMode playMode) {
        Animation<TextureRegion> animation= new Animation<>(frameDuration, frames);
        animation.setPlayMode(playMode);
        return animation;
    }


    public TextureRegion getCurrentFrame(float stateTime, Hero.State currentState){
        Animation<TextureRegion> currentAnimation = switch (currentState) {
            case IDLE -> idleAnimation;
            case WALKING -> walkAnimation;
            case IN_AIR -> inAirAnimation;
            case ATTACKING -> attackAnimation;
        };
        return  currentAnimation.getKeyFrame(stateTime);
    }

    public void dispose(){
        spriteSheet.dispose();
    }
}
