package ruairi.nea.gameClasses.Entities.Enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Level;

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

    public static final int MAX_HEALTH = 1000;

    private float stateTime = 0;

    private Level level;

    private BossAI.BossState previousState = IDLE;
    private BossAI.BossState currentState = IDLE;

    public Boss(float posX, float posY, Level level){
        super(posX,posY,FRAME_WIDTH*ZOOM, FRAME_HEIGHT *ZOOM,10);
        this.level = level;

        health = MAX_HEALTH;

        loadAllWeights();
        loadAnimations();
        setStateLengthsToOne();
    }

    public void transitionToState(BossState state){
        stateTime=0;


        int directionToPlayer = 1;
        if (level.getHero().getPosX()<posX) directionToPlayer=-1;

        switch (state){
            case IDLE -> velocityX=0;
            case WALK_TOWARDS -> velocityX=100*directionToPlayer;
            case WALK_AWAY -> velocityX=-100*directionToPlayer;
        }
    }


    public void loadAnimations(){
        TextureRegion[][] frames = TextureRegion.split(spriteSheet,FRAME_WIDTH,FRAME_HEIGHT);
        for (BossState state : BossState.values()){
            Animation animation = new Animation<>(1,frames[0]);
            animations.put(state,animation);
        }
        Animation walkAnimation = new Animation<>(0.1f,frames[1]);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        animations.put(WALK_TOWARDS,walkAnimation);
        animations.put(WALK_AWAY,walkAnimation);
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
            transitionToState(currentState);
        }
    }

    @Override
    protected void updateVelocity(double delta) {
        super.updateVelocity(delta);
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

    public BossState getPreviousState() {
        return previousState;
    }

    public BossState getCurrentState() {
        return currentState;
    }
}
