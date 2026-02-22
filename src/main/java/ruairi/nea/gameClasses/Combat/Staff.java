package ruairi.nea.gameClasses.Combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Entities.Entity;
import ruairi.nea.gameClasses.Entities.Hero;
import ruairi.nea.gameClasses.Hitbox;

import java.util.HashMap;



public abstract class Staff {
    private static final String SPRITESHEET_PATH = "assets/StaffSpriteSheet.png";
    private static final int FRAME_WIDTH = 16;
    private static final int FRAME_HEIGHT = 16;
    protected float textureOffsetX = 0;
    protected float textureOffsetY = 0;

    float cooldown=0;
    int maxAmmo;
    int currentAmmo;
    int ammoReserves;
    public boolean requiresMana;
    public int manaCost = 0;
    float stateTime = 0;

    Color colour = Color.WHITE;
    final Hero hero;

    private Texture spriteSheet;
    protected HashMap<Hero.HeroState, Animation<TextureRegion>> animations = new HashMap<>();
    Animation<TextureRegion> currentAnimation;

    public abstract void attack();
    public abstract void attackDownwards();

    Hitbox hurtbox = new Hitbox(0,0,0,0);

    public Staff(Hero hero, boolean requiresMana){
        loadAnimations();
        this.requiresMana=requiresMana;
        this.hero = hero;
        currentAnimation = animations.get(Hero.HeroState.IDLE);
    }

    public void update(float delta){
        updateTimers(delta);

        Animation<TextureRegion> previousAnimation = currentAnimation;

        if (animations.get(hero.getCurrentState())==currentAnimation) return;
        if (stateTime<animations.get(Hero.HeroState.ATTACKING).getAnimationDuration() && (hero.getCurrentState()== Hero.HeroState.ATTACKING)) return;

        if (stateTime>=currentAnimation.getAnimationDuration() || currentAnimation!=animations.get(Hero.HeroState.ATTACKING)){
            currentAnimation = animations.get(hero.getCurrentState());
        }


        if (currentAnimation!=previousAnimation) stateTime=0;


    }

    public void updateTimers(float delta){
        if (cooldown>0) cooldown-=delta;
        else cooldown=0;
        stateTime+=delta;
    }

    public void draw(Batch batch){

        float posX = hero.getPosX();
        float posY = hero.getPosY();

        float width = hero.getWidth();
        float height = hero.getHeight();


        batch.setColor(colour);



        if (hero.getCurrentDirection() == Entity.Direction.LEFT){
            posX += width;
            width = -1 * width;
        }

        batch.draw(getCurrentFrame(),posX,posY,width,height);
        batch.setColor(Color.WHITE);
    }




    private void loadAnimations(){
        spriteSheet = new Texture(SPRITESHEET_PATH);

        TextureRegion[][] frames = TextureRegion.split(spriteSheet, FRAME_WIDTH, FRAME_HEIGHT);

        TextureRegion[] idleFrames = frames[0];
        TextureRegion[] walkFrames = frames[1];
        TextureRegion[] attackFrames = frames[2];
        TextureRegion[] inAirFrames = frames[3];

        Animation<TextureRegion> idleAnimation = new Animation<>(0.2f, idleFrames[0]);

        for (Hero.HeroState heroState : Hero.HeroState.values()){
            animations.put(heroState,idleAnimation);
        }

        animations.put(Hero.HeroState.WALKING, new Animation<>(0.3f,walkFrames));
        animations.get(Hero.HeroState.WALKING).setPlayMode(Animation.PlayMode.LOOP);
        animations.put(Hero.HeroState.IN_AIR, new Animation<>(0.1f,inAirFrames[0]));
        animations.put(Hero.HeroState.ATTACKING, new Animation<>(1f, attackFrames[0]));
        animations.get(Hero.HeroState.ATTACKING).setPlayMode(Animation.PlayMode.LOOP);
    }



    public TextureRegion getCurrentFrame(){
        return  currentAnimation.getKeyFrame(stateTime);
    }

    public float getTextureOffsetX() {
        return textureOffsetX;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void dispose(){
        spriteSheet.dispose();
    }

    public void setAnimation(Hero.HeroState state){
        currentAnimation = animations.get(state);
    }
}
