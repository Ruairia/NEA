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
        contactDamage = 10;

        loadAllWeights();
        loadAnimations();
        setStateLengths();
    }

    public void transitionToState(BossState state){
        stateTime=0;


        int directionToPlayer = 1;
        if (level.getHero().getPosX()<posX) directionToPlayer=-1;

        switch (state){
            case IDLE -> velocityX=0;
            case WALK_TOWARDS -> velocityX=100*directionToPlayer;
            case WALK_AWAY -> velocityX=-100*directionToPlayer;
            case DASH -> velocityX=300*directionToPlayer;
            case JUMP -> {
                velocityX=200*directionToPlayer;
                if (isOnGround()) velocityY=500;
            }
            case TELEPORT -> {
                if (Math.abs(level.getHero().getPosX()-posX)>200) return;
                velocityX=0;
                posX=level.getHero().getPosX()+75*directionToPlayer;
                posY=level.getHero().getPosY()+10;
                if (directionToPlayer==1) setCurrentDirection(Direction.LEFT);
                else setCurrentDirection(Direction.RIGHT);
            }
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

    public void setStateLengths(){
        for (int i = 0; i < values().length; i++) {
            stateLengths.put(values()[i],1f);
        }
        stateLengths.put(DASH,0.3f);
        stateLengths.put(JUMP,0.5f);
        stateLengths.put(TELEPORT,0.5f);
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        if (stateTime>stateLengths.get(currentState)){
            previousState=currentState;

            if (currentState!=JUMP||!isOnGround()) currentState = getNextState(previousState);

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
        if (currentState==TELEPORT) super.draw(batch, new Color(0.2f,0.05f,0.05f,0.5f));
        else super.draw(batch, Color.RED);
    }

    public BossState getPreviousState() {
        return previousState;
    }

    public BossState getCurrentState() {
        return currentState;
    }
}
