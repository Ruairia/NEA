package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Staff {
    private Texture spriteSheet;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> inAirAnimation;
    private Animation<TextureRegion> attackAnimation;
    private float stateTime = 0;


    int maxAmmo;
    int currentAmmo;
    int ammoReserves;

    public void reload(){
        int reloadAmount;
        if (ammoReserves>maxAmmo){
            reloadAmount = maxAmmo-currentAmmo;
        }
        else{
            reloadAmount=ammoReserves;
        }
        currentAmmo+=reloadAmount;
        ammoReserves -= reloadAmount;
    }

    private void loadAnimations(){
        spriteSheet = new Texture("assets/WizardSpriteSheetNoStaff.png");

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

}
