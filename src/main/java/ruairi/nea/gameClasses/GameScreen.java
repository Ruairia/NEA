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
import ruairi.nea.gameClasses.Combat.Projectile;
import ruairi.nea.gameClasses.Entities.Enemy;
import ruairi.nea.gameClasses.Entities.Entity;
import ruairi.nea.gameClasses.Entities.Hero;
import ruairi.nea.gameClasses.UI.HealthBar;
import ruairi.nea.gameClasses.UI.ManaBar;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    private Main game;
    private int levelNumber;
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




    //Rendering
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private ShapeRenderer shapeRenderer;
    private BitmapFont bitmapFont;



    public GameScreen(Main game, int levelNumber) {
        this.game = game;
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

        CollisionManager.handlePlatformCollisions(level.mobileEntities, level.platforms);

        if (hero.getPosY()+hero.getHeight()< OUT_OF_WORLD_THRESHOLD){
            hero.spawn();
            hero.damage(OUT_OF_WORLD_THRESHOLD_DAMAGE);
        }

        CollisionManager.handleEnemyCollisions(level.enemies,hero);

        if (hero.getHealth()<=0) {hero.setHealth(100); hero.spawn();}

        updateProjectiles(level,delta,hero);
    }


    @Override
    public void render(float delta) {//Game Loop, runs once a frame

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            returnToMenu();
        }


        perFrameLogic(delta);


        //Rendering

        camera.position.x += (getTargetX() - camera.position.x) * LERP_X;
        camera.position.y += (getTargetY() - camera.position.y) * LERP_Y;
        camera.update();


        Gdx.gl.glClearColor(0, 0, 0, 1);

        drawLevel();
        drawUI();
        drawFPS();
    }

    private void returnToMenu() {
        game.setScreen(new LevelSelectScreen(game));
    }



    private void drawLevel() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        level.background.drawBackground(camera,game);

        for (Entity entity : level.allEntities) {
            entity.draw(game.batch);
        }
        for (Projectile projectile : level.playerProjectiles){
            projectile.draw(game.batch);
        }
        for (Projectile projectile : level.enemyProjectiles){
            projectile.draw(game.batch);
        }
        game.batch.end();
    }



    private void drawUI() {
        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        healthBar.draw(shapeRenderer, hero.getHealth());

        if (hero.getCurrentWeapon().requiresMana) manaBar.draw(shapeRenderer,hero.getMana());

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawFPS() {
        game.batch.setProjectionMatrix(uiCamera.combined);
        game.batch.begin();
        bitmapFont.setColor(1,1,1,1);
        bitmapFont.draw(game.batch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight()-40);
        game.batch.end();
    }

    private float getTargetX() {

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

    private float getTargetY(){

        float upperBufferZone = camera.viewportHeight* CAMERA_UPPER_BUFFER_Y;
        float lowerBufferZone = camera.viewportHeight* CAMERA_LOWER_BUFFER_Y;
        float lowerBound = camera.position.y - (camera.viewportHeight / 2) + lowerBufferZone;
        float upperBound = camera.position.y + (camera.viewportHeight / 2) - upperBufferZone;
        float targetY = camera.position.y;

        if (hero.getPosY() < lowerBound) {
            targetY += hero.getPosY() - lowerBound; // move camera down
            if (targetY<camera.viewportHeight/2) targetY=camera.viewportHeight/2;
        } else if (hero.getPosY() + hero.getWidth() > upperBound) {
            targetY += hero.getPosY() + hero.getWidth() - upperBound; // move camera up
        }
        return targetY;
    }


    private void updateEntities(float delta) {
        for (Entity entity : level.allEntities) {
            entity.update(delta);
        }
    }

    private static void updateProjectiles(Level level, float delta, Hero hero) {


        Iterator<Projectile> projectileIteratorHero = level.playerProjectiles.iterator();
        ArrayList<Enemy> deadEnemies = new ArrayList<>();

        while (projectileIteratorHero.hasNext()) {
            Projectile projectile = projectileIteratorHero.next();
            projectile.update(delta);

            boolean destroyed = false;


            destroyed = CollisionManager.handleProjectilePlatformCollisions(level, projectile, projectileIteratorHero, destroyed);
            if (destroyed) continue;


            CollisionManager.checkProjectileEnemyCollisions(level, projectile, deadEnemies, projectileIteratorHero);



        }

        Iterator<Projectile> projectileIteratorEnemy = level.enemyProjectiles.iterator();
        while (projectileIteratorEnemy.hasNext()) {
            Projectile projectile = projectileIteratorEnemy.next();
            projectile.update(delta);

            boolean destroyed = false;

            destroyed = CollisionManager.handleProjectilePlatformCollisions(level, projectile, projectileIteratorEnemy, destroyed);
            if (destroyed) continue;

            CollisionManager.checkProjectileHeroCollisions(hero, projectile ,projectileIteratorEnemy);
        }


        removeDeadEnemies(level, deadEnemies);
    }

    private static void removeDeadEnemies(Level level, ArrayList<Enemy> deadEnemies) {
        for (Enemy dead : deadEnemies) {
            level.enemies.remove(dead);
            level.mobileEntities.remove(dead);
            level.allEntities.remove(dead);
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
