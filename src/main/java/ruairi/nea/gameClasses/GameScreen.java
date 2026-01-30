package ruairi.nea.gameClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.lwjgl.opengl.GL20;
import ruairi.nea.applicationClasses.LevelSelectScreen;
import ruairi.nea.applicationClasses.Main;
import ruairi.nea.gameClasses.Entities.Projectile;
import ruairi.nea.gameClasses.Entities.*;
import ruairi.nea.gameClasses.Entities.Enemies.Enemy;
import ruairi.nea.gameClasses.Level.Background;
import ruairi.nea.gameClasses.Level.Level;
import ruairi.nea.gameClasses.UI.HealthBar;
import ruairi.nea.gameClasses.UI.ManaBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    private static Main game;
    private final int levelNumber;
    private Level level;

    public static final float V_WIDTH = Main.UI_WIDTH;
    public static final float V_HEIGHT = Main.UI_HEIGHT;

    //Viewport is used so that Windows works in both fullscreen and windowed modes
    private Viewport viewport;
    private Viewport uiViewport;

    //UI&Background
    public HealthBar healthBar;
    public ManaBar manaBar;
    public Background background;

    final float LERP_X = 0.2f;
    final float LERP_Y = 0.1f;
    final float CAMERA_BUFFER_X = 3f / 8f;
    final float CAMERA_UPPER_BUFFER_Y = 1f / 6f;
    final float CAMERA_LOWER_BUFFER_Y = 1f / 3f;
    final int OUT_OF_WORLD_THRESHOLD = -30;
    final int OUT_OF_WORLD_THRESHOLD_DAMAGE = 10;

    public final static float ZOOM =3;




    public Hero hero;
    public static int score=0;



    //Rendering
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;



    public GameScreen(Main game, int levelNumber) {
        GameScreen.game = game;
        this.levelNumber = levelNumber;
    }

    @Override
    public void show() { //Run Once when the screen is shown
        shapeRenderer = new ShapeRenderer();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("assets/font.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 45; // Font size
        parameter.color = Color.WHITE;

        font = generator.generateFont(parameter);
        generator.dispose();

        font.getData().setScale(0.33f);

        healthBar = new HealthBar(V_WIDTH,V_HEIGHT);
        manaBar = new ManaBar(V_WIDTH,V_HEIGHT);

        level = new Level(levelNumber);
        try {
            level.loadLevel(this.levelNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        hero = level.getHero().spawn();



        camera = new OrthographicCamera();
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);
        camera.setToOrtho(false, viewport.getScreenWidth(), viewport.getScreenHeight());

        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(V_WIDTH, V_HEIGHT, uiCamera);
        uiCamera.setToOrtho(false, uiViewport.getScreenWidth(), uiViewport.getScreenHeight());
        uiCamera.position.set(uiCamera.viewportWidth/2, uiCamera.viewportHeight/2,0);

    }



    private void perFrameLogic(float delta){
        updateEntities(delta); //Move hero and entities

        CollisionManager.handleEntityPlatformCollisions(level.mobileEntities, level.platforms);
        removeDeadEntities(level);

        if (hero.getPosY()+hero.getHeight()< OUT_OF_WORLD_THRESHOLD){
            hero.spawn();
            hero.damage(OUT_OF_WORLD_THRESHOLD_DAMAGE);
        }

        CollisionManager.handleEnemyHeroCollisions(level.enemies,hero);

        if (hero.getHealth()<=0) {hero.respawn();}

        updateProjectiles(level,delta,hero);

        checkCheckpoints(level);
        checkCoins(level);
    }


    @Override
    public void render(float delta) {//Game Loop, runs once a frame

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            returnToMenu();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) Main.enemiesUpdate = !Main.enemiesUpdate;
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) Main.canFly = !Main.canFly;


        perFrameLogic(delta);


        //Rendering

        camera.position.x += (getCameraTargetX() - camera.position.x) * LERP_X;
        camera.position.y += (getCameraTargetY() - camera.position.y) * LERP_Y;
        camera.update();


        Gdx.gl.glClearColor(0, 0, 0, 1);

        viewport.apply();
        drawLevel();

        uiViewport.apply();
        drawUI();
        drawText();

    }

    private static void returnToMenu() {
        game.setScreen(new LevelSelectScreen(game));
    }



    private void drawLevel() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        level.background.drawBackgroundBehindCamera(camera,game);

        for (Entity entity : level.allEntities) {
            if (! (entity instanceof Hero)) entity.draw(game.batch);
        }
        for (Projectile projectile : level.projectiles) {
            projectile.draw(game.batch);
        }
        hero.draw(game.batch);
        game.batch.end();
    }



    private void drawUI() {
        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        healthBar.draw(shapeRenderer, hero.getHealth());
        manaBar.draw(shapeRenderer,hero.getMana());

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawText() {
        game.batch.setProjectionMatrix(uiCamera.combined);
        game.batch.begin();
        font.setColor(1,1,1,1);
        font.draw(game.batch, "Score: " + score, V_WIDTH - 220, V_HEIGHT - 10);
        if (Main.drawDebugInfo) {
            font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, V_HEIGHT - 10);
            font.draw(game.batch, "PosX: " + (int) level.getHero().getPosX(), 10, 10);
            font.draw(game.batch, "PosY: " + (int) level.getHero().getPosY(), 10, 30);
        }
        game.batch.end();
    }

    private float getCameraTargetX() {

        float bufferZone = camera.viewportWidth* CAMERA_BUFFER_X;
        float leftBound = camera.position.x - (camera.viewportWidth / 2) + bufferZone;
        float rightBound = camera.position.x + (camera.viewportWidth / 2) - bufferZone;
        float targetX = camera.position.x;

        if (hero.getPosX() < leftBound) {
            targetX += hero.getPosX() - leftBound; // move camera left
        } else if (hero.getPosX() + hero.getWidth() > rightBound) {
            targetX += hero.getPosX() + hero.getWidth() - rightBound; // move camera right
        }
        return targetX;
    }

    private float getCameraTargetY(){

        float upperBufferZone = camera.viewportHeight* CAMERA_UPPER_BUFFER_Y;
        float lowerBufferZone = camera.viewportHeight* CAMERA_LOWER_BUFFER_Y;
        float lowerBound = camera.position.y - (camera.viewportHeight / 2) + lowerBufferZone;
        float upperBound = camera.position.y + (camera.viewportHeight / 2) - upperBufferZone;
        float targetY = camera.position.y;

        if (hero.getPosY() < lowerBound) {
            targetY += hero.getPosY() - lowerBound; // moveAndUpdateState camera down
            if (targetY<camera.viewportHeight/2) targetY=camera.viewportHeight/2;
        } else if (hero.getPosY() + hero.getWidth() > upperBound) {
            targetY += hero.getPosY() + hero.getWidth() - upperBound; // moveAndUpdateState camera up
        }
        return targetY;
    }


    private void updateEntities(float delta) {
        ArrayList<Entity> entitiesToUpdate = new ArrayList<>(level.allEntities);
//Copies allEntities ArrayList to prevent concurrent modification exceptions when entities delete themselves
        for (Entity entity : entitiesToUpdate) {
            if (entity instanceof Enemy && !Main.enemiesUpdate) continue;
            entity.update(delta);
        }
    }

    private static void updateProjectiles(Level level, float delta, Hero hero) {

        for (Projectile projectile : level.projectiles) {
            projectile.update(delta);
            if (CollisionManager.handleProjectilePlatformCollisionsAndCheckIfDestroyed(level,projectile)) continue;

            switch (projectile.origin){
                case PLAYER -> CollisionManager.checkProjectileEnemyCollisions(level,projectile);
                case BOSS, FIRE_MAGE -> CollisionManager.checkProjectileHeroCollisions(hero,projectile);
            }
        }

    }

    private static void removeDeadEntities(Level level) {
        ArrayList<Entity> entitiesToRemove = new ArrayList<>();

        for (Entity entity : level.allEntities) {
            if (entity.getTimeUntilRemoval() != null && entity.getTimeUntilRemoval() <= 0) {
                entitiesToRemove.add(entity);
            }
        }

        for (Entity entity : entitiesToRemove) {
            level.allEntities.remove(entity);
//Removes entities from a type-specific ArrayList if they are in one
            if (entity instanceof Enemy) {
                level.enemies.remove(entity);
                level.mobileEntities.remove(entity);
            }
            else if (entity instanceof Coin) {
                level.coins.remove(entity);
            }
            else if (entity instanceof Checkpoint) {
                level.checkpoints.remove(entity);
            }
            else if (entity instanceof Platform) {
                level.platforms.remove(entity);
            }
            else if (entity instanceof Hero) {
                level.mobileEntities.remove(entity);
            }
        }

        //Use iterator to prevent concurrent modification exceptions
        Iterator<Projectile> playerProjIter = level.projectiles.iterator();
        while (playerProjIter.hasNext()) {
            Projectile proj = playerProjIter.next();
            if (proj.getTimeUntilRemoval() != null && proj.getTimeUntilRemoval() <= 0) {
                playerProjIter.remove();
            }
        }

        Iterator<Projectile> enemyProjIter = level.projectiles.iterator();
        while (enemyProjIter.hasNext()) {
            Projectile proj = enemyProjIter.next();
            if (proj.getTimeUntilRemoval() != null && proj.getTimeUntilRemoval() <= 0) {
                enemyProjIter.remove();
            }
        }
    }


    public static void checkCoins(Level level){
        ArrayList<Coin> coins = level.coins;
        Hero hero = level.getHero();

        for (Coin coin : coins){
            if (coin.getCollisionBox().intersects(hero.getCollisionBox()) && coin.getTimeUntilRemoval()==null){
                coin.setTimeUntilRemoval(0.05f);
                score+=coin.getValue();
            }
        }


    }

    public static void checkCheckpoints(Level level){
        ArrayList<Checkpoint> checkpoints = level.checkpoints;
        Hero hero = level.getHero();

        for (Checkpoint checkpoint : checkpoints){
            if (checkpoint.getCollisionBox().intersects(hero.getCollisionBox()) && checkpoint.getTimeUntilRemoval()==null){
                checkpoint.collect(hero);
                checkpoint.setTimeUntilRemoval(0.05f);
                if (checkpoint instanceof Goal) returnToMenu();
            }
        }

    }



    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        for (Entity visibleEntity : level.allEntities) {
            visibleEntity.dispose();
        }
        Projectile.disposeTextures();
        shapeRenderer.dispose();
        background.dispose();
        font.dispose();
    }
}
