package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.applicationClasses.Main;


public class Staff {

    int maxAmmo;
    int currentAmmo;
    int ammoReserves;

    private Texture spriteSheet;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> inAirAnimation;
    private Animation<TextureRegion> attackAnimation;
    Hero hero;

    public Staff(Hero hero){
        loadAnimations();
        this.hero = hero;
    }



    public void draw(Main game){
        game.batch.setColor(Color.BLUE);
        if (hero.getCurrentDirection() == Entity.Direction.RIGHT)
            game.batch.draw(getCurrentFrame(hero.getStateTime(), hero.getCurrentState()),
                    hero.getPosX(), hero.getPosY(),hero.getWidth(),hero.getHeight());
        else game.batch.draw(getCurrentFrame(hero.getStateTime(),hero.getCurrentState()),
                hero.getPosX()+hero.width, hero.getPosY(),-hero.getWidth(),hero.getHeight());
        game.batch.setColor(Color.WHITE);
    }


    public void reload(){
        int reloadAmount;
        if (ammoReserves>maxAmmo) reloadAmount = maxAmmo-currentAmmo;
        else reloadAmount=ammoReserves;
        currentAmmo+=reloadAmount;
        ammoReserves -= reloadAmount;
    }

    private void loadAnimations(){
        spriteSheet = new Texture("assets/StaffSpriteSheet.png");

        int frameWidth = 16;
        int frameHeight = 16;

        TextureRegion[] idleFrames = Hero.parseFrames(1, frameWidth, 0, frameHeight,spriteSheet,1);


        TextureRegion[] walkFrames =  Hero.parseFrames(2, frameWidth, frameHeight, frameHeight,spriteSheet,2);


        TextureRegion[] attackFrames = Hero.parseFrames(1, frameWidth, frameHeight * 2, frameHeight,spriteSheet,1);


        TextureRegion[] inAirFrames = Hero.parseFrames(1, frameWidth, frameHeight * 3, frameHeight,spriteSheet,1);

        idleAnimation = new Animation<>(1, idleFrames);
        walkAnimation = new Animation<>(0.2f, walkFrames);
        attackAnimation = new Animation<>(1, attackFrames);
        inAirAnimation = new Animation<>(1, inAirFrames);

        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        inAirAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);
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
}
