package ruairi.nea.gameClasses.Combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    private static final float HITBOX_WIDTH = 20 * ZOOM;
    private static final float HITBOX_HEIGHT = 28 * ZOOM;

    private static final float TEXTURE_OFFSET_X = 8 * ZOOM;
    private static final float TEXTURE_OFFSET_Y = 0;

    private static final float HITBOX_OFFSET_X = 10 * ZOOM;
    private static final float HITBOX_OFFSET_Y = 0;

    private static final float DOWNWARDS_TEXTURE_OFFSET_X = -4*ZOOM;
    private static final float DOWNWARDS_TEXTURE_OFFSET_Y = -8*ZOOM;
    private static final float DOWNWARDS_HITBOX_OFFSET_X = -4 * ZOOM;
    private static final float DOWNWARDS_HITBOX_OFFSET_Y = -8 * ZOOM;

    private static final float POGO_STRENGTH = 500 * ZOOM;

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
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        updateState();


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

    private void checkHitboxCollisions(){
        if (currentState== Hero.HeroState.ATTACKING) checkAttackCollisions();
        if (currentState == Hero.HeroState.ATTACKING_DOWNWARDS) checkDownwardAttackCollisions();
    }

    private void checkAttackCollisions() {
        float hitboxX = getHitboxX();
        float hitboxY = getHitboxY();

        for (Enemy enemy : level.enemies) {
            if (enemiesHit.contains(enemy) || enemy.getTimeUntilRemoval() != null) {
                continue;
            }

            if (intersectsHitbox(enemy, hitboxX, hitboxY, HITBOX_WIDTH, HITBOX_HEIGHT)) {
                hitEnemy(enemy, DAMAGE);
                enemiesHit.add(enemy);
            }
        }
    }

    private void checkDownwardAttackCollisions() {
        float hitboxX = hero.getPosX() + (hero.getWidth() - HITBOX_WIDTH) / 2;
        float hitboxY = hero.getPosY() + DOWNWARDS_HITBOX_OFFSET_Y;

        for (Enemy enemy : level.enemies) {
            if (enemiesHit.contains(enemy) || enemy.getTimeUntilRemoval() != null) {
                continue;
            }

            if (intersectsHitbox(enemy, hitboxX, hitboxY, HITBOX_WIDTH, HITBOX_HEIGHT)) {
                hitEnemy(enemy, DAMAGE);
                enemiesHit.add(enemy);

                // Bounce hero upward (pogo effect)
                hero.setVelocityY(80 * ZOOM);
            }
        }
    }

    private boolean intersectsHitbox(Enemy enemy, float hitboxX, float hitboxY, float width, float height) {
        return hitboxX < enemy.getPosX() + enemy.getWidth() &&
                hitboxX + width > enemy.getPosX() &&
                hitboxY < enemy.getPosY() + enemy.getHeight() &&
                hitboxY + height > enemy.getPosY();
    }

    private void hitEnemy(Enemy enemy, int damage) {
        enemy.damageEnemy(damage);


        if (currentState== Hero.HeroState.ATTACKING_DOWNWARDS) {
            hero.setVelocityY(POGO_STRENGTH);
            hero.setJumpsRemaining(1);
        }

        if (enemy.getHealth() <= 0 && enemy.getTimeUntilRemoval() == null) {
            enemy.kill(0.2f);
        }
    }

    private float getHitboxX() {
        int hitboxOffsetX;
        if (currentState == Hero.HeroState.ATTACKING_DOWNWARDS) hitboxOffsetX = (int) DOWNWARDS_HITBOX_OFFSET_X;
        else hitboxOffsetX = (int) HITBOX_OFFSET_X;
        if (hero.getCurrentDirection() == Entity.Direction.RIGHT) {
            return hero.getPosX() + hitboxOffsetX;
        } else {
            return hero.getPosX() + hero.getWidth() - HITBOX_WIDTH - hitboxOffsetX;
        }
    }

    private float getHitboxY() {
        int hitboxOffsetY;
        if (currentState == Hero.HeroState.ATTACKING_DOWNWARDS) hitboxOffsetY = (int) DOWNWARDS_HITBOX_OFFSET_Y;
        else hitboxOffsetY = (int) HITBOX_OFFSET_Y;
        return hero.getPosY() + hitboxOffsetY;
    }

    private void drawHitbox(Batch batch){
        batch.setColor(1,1,1,0.5f);
        Texture texture = new Texture("assets/TextureUnknown.png");
        batch.draw(texture,getHitboxX(),getHitboxY(),HITBOX_WIDTH,HITBOX_HEIGHT);
        batch.setColor(Color.WHITE);
    }

    @Override
    public void draw(Batch batch) {



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
