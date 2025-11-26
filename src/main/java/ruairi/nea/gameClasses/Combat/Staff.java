package ruairi.nea.gameClasses.Combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Entities.Entity;
import ruairi.nea.gameClasses.Entities.Hero;

import static ruairi.nea.gameClasses.Utils.createAnimation;


public abstract class Staff {
    private static final String SPRITESHEET_PATH = "assets/StaffSpriteSheet.png";
    private static final int FRAME_WIDTH = 16;
    private static final int FRAME_HEIGHT = 16;

    float cooldown=0;
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
    Animation<TextureRegion> currentAnimation;

    public abstract void attack();

    public Staff(Color colour, Hero hero){
        loadAnimations();
        this.hero = hero;
        this.colour = colour;
        currentAnimation = idleAnimation;
    }

    public void  updateCooldownTimer(float delta){
        if (cooldown>0) cooldown-=delta;
        else cooldown=0;
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

        idleAnimation = createAnimation(idleFrames, 0.1f, Animation.PlayMode.LOOP);
        walkAnimation = createAnimation(walkFrames,0.2f, Animation.PlayMode.LOOP);
        inAirAnimation = createAnimation(inAirFrames, 0.1f, Animation.PlayMode.NORMAL);
        attackAnimation = createAnimation(attackFrames,0.28f, Animation.PlayMode.NORMAL);
    }



    public TextureRegion getCurrentFrame(float stateTime, Hero.State currentState){
        if (stateTime>=currentAnimation.getAnimationDuration() || currentAnimation!=attackAnimation){
            currentAnimation = switch (currentState) {
            case IDLE -> idleAnimation;
            case WALKING -> walkAnimation;
            case IN_AIR -> inAirAnimation;
            case ATTACKING -> attackAnimation;
        };
        }
        return  currentAnimation.getKeyFrame(stateTime);
    }

    public float getCooldown() {
        return cooldown;
    }

    public void dispose(){
        spriteSheet.dispose();
    }
}
