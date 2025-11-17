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

    public ArrayList<Sprite> allSprites;
    public ArrayList<Platform> platforms;


    public Hero hero;
    public HealthBar healthBar;


    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private ShapeRenderer shapeRenderer;


    int level;

    public GameScreen(Main game, int level) {
        this.game = game;
        this.level = level;
    }

    @Override
    public void show() { //Run Once when the screen is shown
        shapeRenderer = new ShapeRenderer();



        healthBar = new HealthBar(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        LevelLoader levelLoader = new LevelLoader(this);
        allSprites = levelLoader.loadLevel(level);

        hero = new Hero().setSpawnPoint(levelLoader.getSpawnPointX(), levelLoader.getSpawnPointY()).spawn();
        allSprites.add(hero);
        allSprites.add(new Fireball(200,600));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.position.set(uiCamera.viewportWidth/2, uiCamera.viewportHeight/2,0);

    }



    private void perFrameLogic(float delta){
        updatePositions(delta); //Move hero and sprites

        ArrayList<Sprite> spritesToBeChecked = new ArrayList<>();
        ArrayList<Platform> platformsToBeChecked = new ArrayList<>();
        for (Sprite sprite : allSprites){
            if (sprite instanceof Platform) platformsToBeChecked.add((Platform) sprite);
            else {
                spritesToBeChecked.add(sprite);
            }
        }

        CollisionManager.handleCollisions(spritesToBeChecked, platformsToBeChecked);
        if (hero.getPosY()+hero.getHeight()<-30){
            hero.spawn();
            hero.health-=10;
            if (hero.getHealth()<=0) returnToMenu();
        }
    }


    @Override
    public void render(float delta) {//Game Loop, runs once a frame

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            returnToMenu();
        }

        ScreenUtils.clear(Color.DARK_GRAY); //Draw background

        perFrameLogic(delta);


        //Rendering
        final float LINEARINTERPOLATIONX = 0.2f;
        final float LINEARINTERPOLATIONY = 0.1f;
        camera.position.x += (getTargetX() - camera.position.x) * LINEARINTERPOLATIONX;
        camera.position.y += (getTargetY() - camera.position.y) * LINEARINTERPOLATIONY;
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

        for (Sprite sprite : allSprites) {
            if (sprite.getCurrentDirection() == Direction.RIGHT)
                game.batch.draw(sprite.getTexture(), sprite.getPosX(), sprite.getPosY(), sprite.getWidth(), sprite.getHeight());
            else
                game.batch.draw(sprite.getTexture(), sprite.getPosX() + sprite.getWidth(), sprite.getPosY(), -sprite.getWidth(), sprite.getHeight());
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
        float bufferZone = camera.viewportWidth*(3f/8f);
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
        float upperBufferZone = camera.viewportHeight/6;
        float lowerBufferZone = camera.viewportHeight/3;
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
        for (Sprite sprite : allSprites) {
            sprite.update(delta);
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
        for (Sprite visibleSprite : allSprites) {
            visibleSprite.getTexture().dispose();
        }
    }
}
