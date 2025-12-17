package ruairi.nea.gameClasses.Entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.applicationClasses.Main;
import ruairi.nea.gameClasses.Combat.FireStaff;
import ruairi.nea.gameClasses.Combat.IceStaff;
import ruairi.nea.gameClasses.Combat.MeleeStaff;
import ruairi.nea.gameClasses.Hitbox;
import ruairi.nea.gameClasses.InputHandler;
import ruairi.nea.gameClasses.Combat.Staff;
import ruairi.nea.gameClasses.Level;

import java.util.ArrayList;
import java.util.HashMap;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;
import static ruairi.nea.gameClasses.Utils.*;

public class Hero extends Entity {
    private final InputHandler inputHandler = new InputHandler();

    //Define Constant-like variables
    private static final String SPRITESHEET_PATH = "assets/WizardSpriteSheetNoStaff.png";

    public static final float JUMP_STRENGTH = 110*ZOOM;
    public static final float HOLD_JUMP_STRENGTH = 90*ZOOM;
    public static final float MAX_JUMP_DURATION = 0.25f;
    private static final float DOUBLE_JUMP_STRENGTH = 80 * ZOOM;
    private static final float HOLD_DOUBLE_JUMP_STRENGTH = 60 * ZOOM;
    private static final int MAX_JUMPS = 2;

    public static final float WALK_SPEED = 60*ZOOM;

    public static final int MAX_HEALTH = 100;
    public static final int HEAL_COST = 90;
    private static float healCooldown;
    public static final float MAX_HEAL_COOLDOWN = 0.5f;
    int health;

    public static final int MAX_MANA = 100;
    public static final float MANA_REGENERATION = 30f; //Per second
    float mana;

    public static final float INVINCIBILITY_DURATION = 0.6f;




    private int jumpsRemaining = 2;
    private float currentJumpTime=0;
    float invincibilityPeriodLeft = 0;
    float hurtTimer = 0;

    public static final float DASH_SPEED = 250*ZOOM;
    public static final float MAX_DASH_COOLDOWN = 0.5f;
    public static final float DASH_MANA_COST = 20;
    public static final float HOLD_DASH_MANA_COST = 150;
    float dashCurrentCooldown = 0;
    public static final float MAX_DASH_LENGTH = 0.3f;
    float dashLength = 0;

    private static final float KNOCKBACK_TIMER_MAX=0.15f;
    private float knockbackTimer = 0;
    private static final float KNOCKBACK_STRENGTH_X = 140 * ZOOM;
    private static final float KNOCKBACK_STRENGTH_Y = 80 * ZOOM;

    public enum HeroState {
        IDLE,
        WALKING,
        IN_AIR,
        ATTACKING,
        ATTACKING_DOWNWARDS,
        DASH
    }
    private HeroState currentState = HeroState.IDLE;

    private Texture spriteSheet;
    private HashMap<HeroState, Animation<TextureRegion>> animations = new HashMap<>();
    private float stateTime = 0;
    Animation<TextureRegion> currentAnimation;

    ArrayList<Staff> weapons = new ArrayList<>();
    Staff currentStaff;
    int weaponsIndex;

    float swapCooldown=0;
    public static final float MAX_SWAP_COOLDOWN = 0.5f;

    float spawnPointX = 100;
    float spawnPointY = 100;

    public Hero(Level level) {
        super(0,0, 16* ZOOM, 16* ZOOM);

        health = MAX_HEALTH;
        mana = MAX_MANA;

        loadAnimations();
        currentAnimation = animations.get(HeroState.IDLE);

        weapons.add(new FireStaff(this,level));
        weapons.add(new MeleeStaff(this,level));
        weapons.add(new IceStaff(this,level));

        weaponsIndex=0;
        currentStaff=weapons.get(weaponsIndex);

        hitbox.setLeftOffsetX(2*ZOOM);
        hitbox.setRightOffsetX(4*ZOOM);
        hitbox.setTopOffsetY(2*ZOOM);
        this.setHitbox(hitbox);

    }

    public Hero spawn() {
        setPosX(spawnPointX);
        setPosY(spawnPointY);
        setVelocityX(0);
        setVelocityY(0);
        setInvincibilityPeriodLeft(0.3f);
        return this;
    }

    public void respawn(){
        spawn();
        health = MAX_HEALTH;
        mana = MAX_MANA;
    }


    public void updateTimers(float delta){
        super.updateTimers(delta);
        stateTime+=delta;

        swapCooldown -= delta;
        if (swapCooldown<0) swapCooldown=0;

        healCooldown -= delta;
        if (healCooldown<0) healCooldown=0;

        dashCurrentCooldown -= delta;
        if (dashCurrentCooldown<0) dashCurrentCooldown=0;

        if (knockbackTimer>0) knockbackTimer-= delta;
        else knockbackTimer=0;

        if (invincibilityPeriodLeft>0) invincibilityPeriodLeft-= delta;
        else invincibilityPeriodLeft=0;

        hurtTimer-=delta;
        if (hurtTimer<0) hurtTimer=0;


        if (mana<MAX_MANA) mana += (delta*MANA_REGENERATION);
        else mana=MAX_MANA;

    }

    @Override
    protected void updateVelocity(double delta) {
        moveAndUpdateState(delta);
        super.updateVelocity(delta);
    }


    private void dash(int playerDirection) {
        setCurrentState(HeroState.DASH);
        dashCurrentCooldown = MAX_DASH_COOLDOWN;
        mana-=DASH_MANA_COST;
        dashLength = 0;
        velocityX=playerDirection*DASH_SPEED;
        inputHandler.vibrateController(100,0.8f);
    }

    private void holdDash(int playerDirection, float delta) {
        int manaCost = (int) (HOLD_DASH_MANA_COST * delta);
        if (mana<manaCost) return;
        if (Math.abs(velocityX)<0.8*DASH_SPEED) velocityX= playerDirection * DASH_SPEED*0.8f;
        velocityY*=0.5f;
    }

    @Override
    protected void updateDirection() {
    }

    public void update(double delta){
        super.update(delta);
        currentStaff.update((float) delta);
        if (isOnGround) jumpsRemaining = MAX_JUMPS;
        if (!isOnGround&&jumpsRemaining==2) jumpsRemaining=1;
    }


    private void loadAnimations() {
        spriteSheet = new Texture(SPRITESHEET_PATH);

        int frameWidth = 16;
        int frameHeight = 16;

        TextureRegion[] idleFrames = parseFrames(0,0, frameWidth, frameHeight, spriteSheet, 1);


        TextureRegion[] walkFrames = parseFrames(0,1*frameHeight, frameWidth, frameHeight, spriteSheet, 2);


        TextureRegion[] attackFrames = parseFrames(0,2*frameHeight, frameWidth, frameHeight, spriteSheet, 1);


        TextureRegion[] inAirFrames = parseFrames(0, 3*frameHeight, frameWidth, frameHeight, spriteSheet, 1);

        Animation<TextureRegion> idleAnimation = createAnimation(idleFrames, 0.1f, Animation.PlayMode.LOOP);

        for (HeroState heroState : HeroState.values()){
            animations.put(heroState,idleAnimation);
        }

        animations.put(HeroState.WALKING,createAnimation(walkFrames, 0.2f, Animation.PlayMode.LOOP));
        animations.put(HeroState.IN_AIR,createAnimation(inAirFrames, 0.1f, Animation.PlayMode.LOOP));
        animations.put(HeroState.ATTACKING,createAnimation(attackFrames, 0.25f, Animation.PlayMode.NORMAL));
    }




    @Override
    public TextureRegion getCurrentFrame(){
        if (stateTime>=currentAnimation.getAnimationDuration() || currentAnimation!=animations.get(HeroState.ATTACKING)) {
        currentAnimation = switch (currentState) {
            case IDLE,WALKING,IN_AIR,DASH -> animations.get(currentState);
            case ATTACKING,ATTACKING_DOWNWARDS -> {if (isOnGround) yield animations.get(currentState); else yield animations.get(HeroState.IN_AIR);}
        };
        }
        return  currentAnimation.getKeyFrame(stateTime);
    }


    public void damage(int damage){
        health-=damage;
        hurtTimer=INVINCIBILITY_DURATION;
        inputHandler.vibrateController(200,0.9f);
    }

    public void applyKnockback(Entity entity) {
        velocityX = (this.posX < entity.getPosX()) ? -KNOCKBACK_STRENGTH_X  : KNOCKBACK_STRENGTH_X;

        velocityY = KNOCKBACK_STRENGTH_Y;

        knockbackTimer = KNOCKBACK_TIMER_MAX;
    }




    private void jump(){
        //Handle logic for jumping
        if (jumpsRemaining > 0) {
            velocityY = (jumpsRemaining == MAX_JUMPS) ? JUMP_STRENGTH : DOUBLE_JUMP_STRENGTH;
            isOnGround = false;
            jumpsRemaining--;
            currentJumpTime = 0;
            if (jumpsRemaining == 0) {
                inputHandler.vibrateController(200,0.25f);
            }
        }
    }

    private void holdJump(double delta){
        if (currentJumpTime< MAX_JUMP_DURATION || Main.canFly) {
            currentJumpTime+= (float) delta;
            if (jumpsRemaining == 1) {
                if (velocityY < HOLD_JUMP_STRENGTH)
                    velocityY = (HOLD_JUMP_STRENGTH);
            }
            else if (velocityY<HOLD_DOUBLE_JUMP_STRENGTH)
                velocityY = HOLD_DOUBLE_JUMP_STRENGTH;
        }
    }

    private void moveAndUpdateState(double delta) {
        HeroState previousState = currentState;

        ArrayList<String> input = inputHandler.getInputs();
        if (input.contains("LEFT") && !input.contains("RIGHT")) {
            if (knockbackTimer == 0) velocityX = -WALK_SPEED * inputHandler.horizontalAxisStrength;
            if (isOnGround) setCurrentState(HeroState.WALKING);
            setCurrentDirection(Direction.LEFT);
        } else if (input.contains("RIGHT") && !input.contains("LEFT")) {
            if (knockbackTimer == 0) velocityX = WALK_SPEED * inputHandler.horizontalAxisStrength;
            if (isOnGround) setCurrentState(HeroState.WALKING);
            setCurrentDirection(Direction.RIGHT);
        } else {
            if (isOnGround) setCurrentState(HeroState.IDLE);
            else setCurrentState(HeroState.IN_AIR);
            if (knockbackTimer == 0) velocityX *= 0.5f;
        }
        if (input.contains("JUMP")) {
            jump();
        }
        if (input.contains("HOLD_JUMP")) {
            holdJump(delta);
        } else {
            currentJumpTime = MAX_JUMP_DURATION;
        }
        if (input.contains("ATTACK") && currentStaff.getCooldown() == 0) {
            if (mana >= currentStaff.manaCost) {

                if (input.contains("DOWN") && !isOnGround){
                    setCurrentState(HeroState.ATTACKING_DOWNWARDS);
                    currentStaff.attackDownwards();
                }

                else {
                    setCurrentState(HeroState.ATTACKING);
                    currentStaff.attack();
                }

                mana -= currentStaff.manaCost;
                velocityX /= 10;
            }
        }
        if (input.contains("SWAP")&&swapCooldown<=0){
            weaponsIndex=(weaponsIndex+1)%weapons.size();
            currentStaff=weapons.get(weaponsIndex);
            swapCooldown=MAX_SWAP_COOLDOWN;
        }

        if (input.contains("HEAL") && mana >= HEAL_COST && health<MAX_HEALTH){
            heal();
        }

        if (!isOnGround) {
            setCurrentState(HeroState.IN_AIR);
        }

        if (input.contains("DASH")) {
            if (dashCurrentCooldown == 0 && mana >= DASH_MANA_COST) {
                dash(getPlayerDirection());
            }

        } else if (input.contains("HOLD_DASH")) {
            if (dashLength < MAX_DASH_LENGTH) {
                holdDash(getPlayerDirection(), (float) delta);
                dashLength += (float) delta;
            } else {
                if (dashLength != MAX_DASH_LENGTH) dashCurrentCooldown = MAX_DASH_COOLDOWN;
                dashLength = MAX_DASH_LENGTH;
            }


            if (currentState != previousState) stateTime = 0;
        }
    }

    private void heal() {
        mana-=HEAL_COST;
        health=MAX_HEALTH;
        healCooldown=MAX_HEAL_COOLDOWN;
    }

    public void setJumpsRemaining(int jumpsRemaining) {
        this.jumpsRemaining = jumpsRemaining;
    }
    public int getJumpsRemaining() {
        return jumpsRemaining;
    }

    public HeroState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(HeroState currentState){
        this.currentState = currentState;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getMana() {
        return mana;
    }

    public void setMana(float mana) {
        this.mana = mana;
    }

    public Staff getCurrentWeapon() {
        return currentStaff;
    }

    public void setCurrentWeapon(Staff currentStaff) {
        this.currentStaff = currentStaff;
    }

    public float[] getSpawnPoint() {
        return new float[]{spawnPointX,spawnPointY};
    }

    public Hero setSpawnPoint(float posX, float posY) {
        this.spawnPointX=posX;
        this.spawnPointY=posY;
        return this;
    }

    public float getStateTime() {
        return stateTime;
    }

    public float getInvincibilityPeriodLeft() {
        return invincibilityPeriodLeft;
    }

    public void setInvincibilityPeriodLeft(float invincibilityPeriodLeft) {
        this.invincibilityPeriodLeft = invincibilityPeriodLeft;
    }
    @Override
    public void dispose(){
    spriteSheet.dispose();
    currentStaff.dispose();
    }

    @Override
    public void draw(Batch batch){
        if (invincibilityPeriodLeft>0 && hurtTimer<=0) {
            drawBright(batch, 0.5f);
            currentStaff.draw(batch);
            return;
        }
        if (hurtTimer>0) batch.setColor(Color.SALMON);
        super.draw(batch);
        currentStaff.draw(batch);
        batch.setColor(Color.WHITE);
    }

    public int getPlayerDirection(){
        if (getCurrentDirection()==Direction.LEFT) return -1;
        else return 1;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "currentState=" + currentState +
                ", health=" + health +
                ", mana=" + mana +
                ", jumpsRemaining=" + jumpsRemaining +
                ", currentJumpTime=" + currentJumpTime +
                ", invincibilityPeriodLeft=" + invincibilityPeriodLeft +
                ", knockbackTimer=" + knockbackTimer +
                ", isOnGround=" + isOnGround +
                ", posX=" + posX +
                ", posY=" + posY +
                ", velocityX=" + velocityX +
                ", velocityY=" + velocityY +
                ", width=" + width +
                ", FRAME_HEIGHT=" + height +
                '}';
    }
}
