package ruairi.nea.gameClasses.Combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.applicationClasses.Main;
import ruairi.nea.gameClasses.Entities.Enemies.Enemy;
import ruairi.nea.gameClasses.Entities.Entity;
import ruairi.nea.gameClasses.Entities.Hero;
import ruairi.nea.gameClasses.Level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class MeleeStaff extends Staff{
    public static final String SPRITESHEET_PATH = "assets/SwordSpriteSheet.png";
    public static Texture spriteSheet = new Texture(SPRITESHEET_PATH);
    public static final int TEXTURE_WIDTH = 26;
    public static final int TEXTURE_HEIGHT = 28;

    private static HashMap<Hero.HeroState, Animation<TextureRegion>> animations = new HashMap<>();
    private static void loadAnimations(){
        Animation<TextureRegion> idleAnimation = new Animation<>(1f, new TextureRegion(spriteSheet, TEXTURE_WIDTH*2, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT));
        for (Hero.HeroState state : Hero.HeroState.values()){
            animations.put(state,idleAnimation);
        }
        TextureRegion[][]frames = TextureRegion.split(spriteSheet, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        animations.put(Hero.HeroState.ATTACKING, new Animation<>(0.05f, Arrays.copyOfRange(frames[0],2,6)));
        animations.get(Hero.HeroState.ATTACKING).setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        animations.put(Hero.HeroState.ATTACKING_DOWNWARDS, new Animation<>(0.025f,frames[1]));
    }

    private Hero.HeroState currentState = Hero.HeroState.IDLE;
    private float stateTime = 0;
    private static HashMap<Hero.HeroState, Float> stateLengths = new HashMap<>();
    private static void setStateLengths(){
        for (Hero.HeroState state : Hero.HeroState.values()){
            stateLengths.put(state,0f);
        }

        stateLengths.put(Hero.HeroState.ATTACKING,
                animations.get(Hero.HeroState.ATTACKING).getAnimationDuration()*1.7f);
        stateLengths.put(Hero.HeroState.ATTACKING_DOWNWARDS,

                animations.get(Hero.HeroState.ATTACKING_DOWNWARDS).getAnimationDuration());
    }


    private static final int DAMAGE = 30;
    private static final float COOLDOWN = 0.5f;

    private static final float HURTBOX_WIDTH = 20 * ZOOM;
    private static final float HURTBOX_HEIGHT = 28 * ZOOM;

    private static final float TEXTURE_OFFSET_X = 8 * ZOOM;
    private static final float TEXTURE_OFFSET_Y = 0;

    private static final float HURTBOX_OFFSET_X = 10 * ZOOM;
    private static final float HURTBOX_OFFSET_Y = 0;

    private static final float DOWNWARDS_TEXTURE_OFFSET_X = -2*ZOOM;
    private static final float DOWNWARDS_TEXTURE_OFFSET_Y = -10*ZOOM;
    private static final float DOWNWARDS_HURTBOX_OFFSET_X = 1 * ZOOM;
    private static final float DOWNWARDS_HURTBOX_OFFSET_Y = -12 * ZOOM;

    private static final float POGO_STRENGTH = 150 * ZOOM;

    private HashSet<Enemy> enemiesHit = new HashSet<>();

    private final Level level;


    private int width;
    private int height;

    public MeleeStaff(Hero hero, Level level){
        super(hero,false);

        width = (int) (TEXTURE_WIDTH*ZOOM);
        height = (int) (TEXTURE_HEIGHT*ZOOM);

        loadAnimations();
        setStateLengths();

        this.level = level;
        this.manaCost=0;

        textureOffsetX=TEXTURE_OFFSET_X;
        textureOffsetY=TEXTURE_OFFSET_Y;

        hurtbox.setPosX(hero.getPosX()+ HURTBOX_OFFSET_X);
        hurtbox.setPosY(hero.getPosY()+ HURTBOX_OFFSET_Y);
        hurtbox.setWidth(HURTBOX_WIDTH);
        hurtbox.setHeight(HURTBOX_HEIGHT);
        hurtbox.setLeftOffsetX(HURTBOX_OFFSET_X);
        hurtbox.setBottomOffsetY(HURTBOX_OFFSET_Y);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        updateState();
        updateHurtbox();
        checkHitboxCollisions();

    }

    @Override
    public void updateTimers(float delta) {
        super.updateTimers(delta);
        stateTime+=delta;
        if (currentState== Hero.HeroState.ATTACKING_DOWNWARDS){
            textureOffsetX=DOWNWARDS_TEXTURE_OFFSET_X;
            textureOffsetY=DOWNWARDS_TEXTURE_OFFSET_Y;
        }
        else {
            textureOffsetX=TEXTURE_OFFSET_X;
            textureOffsetY=TEXTURE_OFFSET_Y;
        }
    }

    public void updateState(){
        if (stateTime < stateLengths.get(currentState)) return;
        currentState=hero.getCurrentState();
        stateTime=0;

        if (currentState == Hero.HeroState.ATTACKING){

        }
        else if (currentState == Hero.HeroState.ATTACKING_DOWNWARDS){

        }

    }

    @Override
    public void attack() {
        if (cooldown>0) return;

        cooldown = COOLDOWN;
        enemiesHit.clear();
        currentState= Hero.HeroState.ATTACKING;
    }

    @Override
    public void attackDownwards() {
        if (cooldown>0) return;

        cooldown = COOLDOWN;
        enemiesHit.clear();
        currentState= Hero.HeroState.ATTACKING_DOWNWARDS;
    }

    private void updateHurtbox(){

        float boxOffsetX;
        float boxOffsetY;

        if (currentState == Hero.HeroState.ATTACKING_DOWNWARDS){
            boxOffsetX = DOWNWARDS_HURTBOX_OFFSET_X;
            boxOffsetY = DOWNWARDS_HURTBOX_OFFSET_Y;
        }
        else if (currentState == Hero.HeroState.ATTACKING) {
            boxOffsetX = HURTBOX_OFFSET_X;
            boxOffsetY = HURTBOX_OFFSET_Y;
        }
        else {
            // Not attacking, so no hurtbox
            hurtbox.setWidth(0);
            hurtbox.setHeight(0);
            return;
        }

        // 1. Set the dimensions
        hurtbox.setWidth(HURTBOX_WIDTH);
        hurtbox.setHeight(HURTBOX_HEIGHT);

        // 2. Adjust the X offset based on direction
        if (hero.getCurrentDirection() == Entity.Direction.LEFT){
            // Flip the X offset to place the hurtbox on the left side of the hero
            // Formula: HeroWidth - OriginalOffsetX - HurtboxWidth
            boxOffsetX = hero.getWidth() - boxOffsetX - HURTBOX_WIDTH;
        }

        // 3. Apply the final offsets to the hurtbox object
        hurtbox.setLeftOffsetX(boxOffsetX);
        hurtbox.setBottomOffsetY(boxOffsetY);


        // 4. Update the position of the hurtbox
        hurtbox.setPosX(hero.getPosX() + hurtbox.getLeftOffsetX());
        hurtbox.setPosY(hero.getPosY() + hurtbox.getBottomOffsetY());
    }

    private void checkHitboxCollisions(){
        for (Enemy enemy : level.enemies) {

            if (enemiesHit.contains(enemy) || enemy.getTimeUntilRemoval() !=null) continue;

            if (hurtbox.intersects(enemy.getHitbox())){
                hitEnemy(enemy,DAMAGE);
                enemiesHit.add(enemy);
            }

        }
    }



    private void hitEnemy(Enemy enemy, int damage) {
        enemy.damageEnemy(damage);

        if (currentState== Hero.HeroState.ATTACKING_DOWNWARDS){
            hero.setVelocityY(POGO_STRENGTH);
            hero.setJumpsRemaining(1);
        }


        if (enemy.getHealth() <= 0 && enemy.getTimeUntilRemoval() == null) {
            enemy.kill(0.2f);
        }
    }




    @Override
    public void draw(Batch batch) {


        if (Main.drawHurtboxes) hurtbox.draw(batch);

        float drawX = hero.getPosX();
        float drawY = hero.getPosY();

        if (hero.getCurrentDirection()== Entity.Direction.RIGHT){
            drawX+=textureOffsetX;
        }
        else {
            drawX+=(-textureOffsetX+hero.getWidth());
        }

        drawY+=textureOffsetY;

        float drawWidth = width;

        batch.setColor(colour);


        if (hero.getCurrentDirection() == Entity.Direction.LEFT) {
            drawWidth = -width;
        }

        TextureRegion frame = animations.get(currentState).getKeyFrame(stateTime);
        batch.draw(frame, drawX, drawY, drawWidth, height);
        batch.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (spriteSheet != null) {
            spriteSheet.dispose();
        }
    }

}
