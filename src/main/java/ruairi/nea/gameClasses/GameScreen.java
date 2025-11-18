package ruairi.nea.gameClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import ruairi.nea.applicationClasses.LevelSelectScreen;
import ruairi.nea.applicationClasses.Main;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private Main game;
    private int level;


    final float LERP_X = 0.2f;
    final float LERP_Y = 0.1f;
    final float CAMERA_BUFFER_X = 3f / 8f;
    final float CAMERA_UPPER_BUFFER_Y = 1f / 6f;
    final float CAMERA_LOWER_BUFFER_Y = 1f / 3f;
    final int OUT_OF_WORLD_THRESHOLD = -30;
    final int OUT_OF_WORLD_THRESHOLD_DAMAGE = 10;
    final float INVINCIBILITY_DURATION = 0.6f;

    public ArrayList<Entity> allEntities;
    public ArrayList<Platform> platforms = new ArrayList<>();;
    ArrayList<Enemy> damagingEntities = new ArrayList<>();
    ArrayList<Entity> mobileEntity = new ArrayList<>();

    public Hero hero;

    //UI
    public HealthBar healthBar;

    //Rendering
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private ShapeRenderer shapeRenderer;



    public GameScreen(Main game, int level) {
        this.game = game;
        this.level = level;
    }

    @Override
    public void show() { //Run Once when the screen is shown
        shapeRenderer = new ShapeRenderer();



        healthBar = new HealthBar(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        LevelLoader levelLoader = new LevelLoader(this);
        allEntities = levelLoader.loadLevel(level);

        hero = new Hero().setSpawnPoint(levelLoader.getSpawnPointX(), levelLoader.getSpawnPointY()).spawn();
        allEntities.add(hero);
        allEntities.add(new Fireball(200,100, 0,300));

        for (Entity entity : allEntities){
            if (entity instanceof Platform) platforms.add((Platform) entity);
            else {

                if (entity instanceof Enemy) damagingEntities.add((Enemy) entity);
                mobileEntity.add(entity);
            }
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.position.set(uiCamera.viewportWidth/2, uiCamera.viewportHeight/2,0);

    }



    private void perFrameLogic(float delta){
        updatePositions(delta); //Move hero and entities

        CollisionManager.handleCollisions(mobileEntity, platforms);

        if (hero.getPosY()+hero.getHeight()< OUT_OF_WORLD_THRESHOLD){
            hero.spawn();
            hero.health-=OUT_OF_WORLD_THRESHOLD_DAMAGE;
        }
        for (Enemy enemy : damagingEntities){
            if (hero.invincibilityPeriodLeft==0 && enemy.intersectsHero(hero)) {
                    hero.health-=enemy.damage; hero.setInvincibilityPeriodLeft(INVINCIBILITY_DURATION);}
        }
        if (hero.getHealth()<=0) {hero.setHealth(100); hero.spawn();}
    }


    @Override
    public void render(float delta) {//Game Loop, runs once a frame

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            returnToMenu();
        }

        ScreenUtils.clear(Color.DARK_GRAY); //Draw background

        perFrameLogic(delta);


        //Rendering

        camera.position.x += (getTargetX() - camera.position.x) * LERP_X;
        camera.position.y += (getTargetY() - camera.position.y) * LERP_Y;
        camera.update();

        drawLevel();

        drawUI();

    }

    private void returnToMenu() {
        game.setScreen(new LevelSelectScreen(game));
    }

    private void drawLevel() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        for (Entity entity : allEntities) {
            if (entity.getCurrentDirection() == Entity.Direction.RIGHT)
                game.batch.draw(entity.getTexture(), entity.getPosX(), entity.getPosY(), entity.getWidth(), entity.getHeight());
            else
                game.batch.draw(entity.getTexture(), entity.getPosX() + entity.getWidth(), entity.getPosY(), -entity.getWidth(), entity.getHeight());
        }
        game.batch.end();
    }

    private void drawUI() {
        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        healthBar.render(shapeRenderer, hero.getHealth());

        shapeRenderer.end();
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


    private void updatePositions(float delta) {
        for (Entity entity : allEntities) {
            entity.update(delta);
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
        for (Entity visibleEntity : allEntities) {
            visibleEntity.getTexture().dispose();
        }
    }
}
