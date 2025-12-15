package ruairi.nea.gameClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.lwjgl.opengl.GL20;
import ruairi.nea.applicationClasses.LevelSelectScreen;
import ruairi.nea.applicationClasses.Main;
import ruairi.nea.gameClasses.Entities.Projectile;
import ruairi.nea.gameClasses.Entities.*;
import ruairi.nea.gameClasses.Entities.Enemies.Enemy;
import ruairi.nea.gameClasses.UI.HealthBar;
import ruairi.nea.gameClasses.UI.ManaBar;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    private static Main game;
    private final int levelNumber;
    private Level level;

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
    private BitmapFont bitmapFont;



    public GameScreen(Main game, int levelNumber) {
        GameScreen.game = game;
        this.levelNumber = levelNumber;
    }

    @Override
    public void show() { //Run Once when the screen is shown
        shapeRenderer = new ShapeRenderer();
        bitmapFont = new BitmapFont();

        healthBar = new HealthBar(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        manaBar = new ManaBar(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        level = new Level();
        level.loadLevel(this.levelNumber);
        hero = level.getHero().spawn();



        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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


        perFrameLogic(delta);


        //Rendering

        camera.position.x += (getCameraTargetX() - camera.position.x) * LERP_X;
        camera.position.y += (getCameraTargetY() - camera.position.y) * LERP_Y;
        camera.update();


        Gdx.gl.glClearColor(0, 0, 0, 1);

        drawLevel();
        drawUI();
        drawText();
        drawText();
    }

    private static void returnToMenu() {
        game.setScreen(new LevelSelectScreen(game));
    }



    private void drawLevel() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        level.background.drawBackground(camera,game);

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
        bitmapFont.setColor(1,1,1,1);
        bitmapFont.draw(game.batch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight()-40);
        bitmapFont.draw(game.batch, "Score: "+score, Gdx.graphics.getWidth()-220, Gdx.graphics.getHeight()-20);
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
        for (Entity entity : level.allEntities) {
            entity.update(delta);
        }
    }

    private static void updateProjectiles(Level level, float delta, Hero hero) {

        for (Projectile projectile : level.projectiles) {
            projectile.update(delta);
            if (CollisionManager.handleProjectilePlatformCollisionsAndCheckIfDestroyed(level,projectile)) continue;

            switch (projectile.origin){
                case PLAYER -> CollisionManager.checkProjectileEnemyCollisions(level,projectile);
                case BOSS,FIREMAGE -> CollisionManager.checkProjectileHeroCollisions(hero,projectile);
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
            if (coin.intersect(hero) && coin.getTimeUntilRemoval()==null){
                coin.setTimeUntilRemoval(0.05f);
                score+=coin.getValue();
            }
        }


    }

    public static void checkCheckpoints(Level level){
        ArrayList<Checkpoint> checkpoints = level.checkpoints;
        Hero hero = level.getHero();

        for (Checkpoint checkpoint : checkpoints){
            if (checkpoint.intersect(hero) && checkpoint.getTimeUntilRemoval()==null){
                checkpoint.collect(hero);
                checkpoint.setTimeUntilRemoval(0.05f);
                if (checkpoint instanceof Goal) returnToMenu();
            }
        }

    }



    @Override
    public void resize(int width, int height) {}

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
        bitmapFont.dispose();
    }
}
