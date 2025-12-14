package ruairi.nea.gameClasses.Entities.Enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

import static ruairi.nea.gameClasses.Entities.Enemies.BossAI.*;
import static ruairi.nea.gameClasses.Entities.Enemies.BossAI.BossState.*;
import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Boss extends Enemy{

    public static final String SPRITESHEET_PATH = "assets/WizardSpriteSheetNoStaff.png";
    public static final Texture spriteSheet = new Texture(SPRITESHEET_PATH);

    HashMap<BossState, Animation<TextureRegion>> animations = new HashMap<>();

    public static final int FRAME_WIDTH = 16;
    public static final int FRAME_HEIGHT = 16;

    private float stateTime = 0;

    private BossAI.BossState previousState = IDLE;
    private BossAI.BossState currentState = IDLE;

    public Boss(float posX, float posY){
        super(posX,posY,FRAME_WIDTH*ZOOM, FRAME_HEIGHT *ZOOM,10);
        loadAllWeights();
        loadAnimations();
        setStateLengthsToOne();
    }


    public void loadAnimations(){
        for (BossState state : BossState.values()){
            TextureRegion[][] frames = TextureRegion.split(spriteSheet,FRAME_WIDTH,FRAME_HEIGHT);
            Animation animation = new Animation<>(1,frames[0]);
            animations.put(state,animation);
        }
    }

    public void setStateLengthsToOne(){
        for (int i = 0; i < values().length; i++) {
            stateLengths.put(values()[i],1f);
        }
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        if (stateTime>stateLengths.get(currentState)){
            previousState=currentState;
            currentState = getNextState(previousState);
            stateTime=0;
        }
    }

    @Override
    protected void updateTimers(float delta) {
        super.updateTimers(delta);
        stateTime+=delta;
    }

    @Override
    public TextureRegion getCurrentFrame() {
        return animations.get(currentState).getKeyFrame(stateTime);
    }

    @Override
    public void draw(Batch batch){
        super.draw(batch, Color.RED);
    }
}
