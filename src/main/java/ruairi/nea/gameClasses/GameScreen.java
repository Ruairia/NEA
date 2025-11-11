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

    public ArrayList<Sprite> visibleSprites;


    public Hero hero;
    public HealthBar healthBar;


    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private ShapeRenderer shapeRenderer;
    private static final float LINEARINTERPOLATION = 0.2f;

    public GameScreen(Main game, int level) {
        this.game = game;
    }

    @Override
    public void show() { //Run Once when the screen is shown
        shapeRenderer = new ShapeRenderer();


        hero = new Hero(100, 100, 80, 80);
        healthBar = new HealthBar(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        LevelLoader levelLoader = new LevelLoader(this);
        visibleSprites = levelLoader.loadLevel(1);

        visibleSprites.add(hero);

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
        for (Sprite sprite : visibleSprites){
            if (sprite instanceof Platform) platformsToBeChecked.add((Platform) sprite);
            else spritesToBeChecked.add(sprite);
        }

        CollisionManager.handleCollisions(spritesToBeChecked, platformsToBeChecked);
        if (hero.getPosY()+hero.getHeight()<-30){
            hero.setPosX(100); hero.setPosY(100);
            hero.health-=10;
            if (hero.getHealth()<=0) returnToMenu();
        }
    }


    @Override
    public void render(float delta) {//Game Loop, runs once a frame

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            returnToMenu();
        }

        ScreenUtils.clear(Color.OLIVE); //Draw background

        perFrameLogic(delta);


        //Rendering

        float targetX = getTargetX();
        camera.position.x += (targetX - camera.position.x) * LINEARINTERPOLATION;
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

        for (Sprite visibleSprite : visibleSprites) {
            if (visibleSprite.getCurrentDirection() == Direction.RIGHT)
                game.batch.draw(visibleSprite.getTexture(), visibleSprite.getPosX(), visibleSprite.getPosY(), visibleSprite.getWidth(), visibleSprite.getHeight());
            else
                game.batch.draw(visibleSprite.getTexture(), visibleSprite.getPosX() + visibleSprite.getWidth(), visibleSprite.getPosY(), -visibleSprite.getWidth(), visibleSprite.getHeight());
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
        float bufferZone = 300;
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

    private void updatePositions(float delta) {
        hero.update(delta);
        for (Sprite visibleSprite : visibleSprites) {
            if (!(visibleSprite instanceof Hero)) visibleSprite.update(delta);
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
        for (Sprite visibleSprite : visibleSprites) {
            visibleSprite.getTexture().dispose();
        }
    }
}
