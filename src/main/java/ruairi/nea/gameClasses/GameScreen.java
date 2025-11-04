package ruairi.nea.gameClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import ruairi.nea.applicationClasses.LevelSelectScreen;
import ruairi.nea.applicationClasses.Main;

import java.security.Key;
import java.util.ArrayList;

public class GameScreen implements Screen {
    private Main game;

    public ArrayList<Sprite> visibleSprites = new ArrayList<>();


    public Hero hero;


    private OrthographicCamera camera;

    public GameScreen(Main game, int level) {
        this.game = game;
    }

    @Override
    public void show() { //Run Once when the screen is shown
        hero = new Hero(100, 100, 80, 80);

        for (int i = 0; i < 5; i++) {
            createPlatform(256*i,0, PlatformType.GRASS);
        }



        visibleSprites.add(hero);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


    }

    private void createPlatform(float x, float y, PlatformType type) {
        visibleSprites.add(new Platform(x, y, type));
    }

    public void perFrameLogic(float delta){
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
        }
    }


    @Override
    public void render(float delta) {//Game Loop, runs once a frame
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
            game.setScreen(new LevelSelectScreen(game));
        }
        ScreenUtils.clear(0.05f, 0.1f, 0.07f, 1); //Draw background

        perFrameLogic(delta);


        //Rendering

        float bufferZone = 200;
        float leftBound = camera.position.x - (camera.viewportWidth / 2) + bufferZone;
        float rightBound = camera.position.x + (camera.viewportWidth / 2) - bufferZone;
        float targetX = camera.position.x;

        if (hero.getPosX() < leftBound) {
            targetX += hero.getPosX() - leftBound; // move camera left
        } else if (hero.getPosX() + hero.getWidth() > rightBound) {
            targetX += hero.getPosX() + hero.getWidth() - rightBound; // move camera right
        }

        float lerp = 0.1f;
        camera.position.x += (targetX - camera.position.x) * lerp;

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        game.batch.begin();

        for (Sprite visibleSprite : visibleSprites) {
            if (visibleSprite.getCurrentDirection()==Direction.RIGHT)
            game.batch.draw(visibleSprite.getTexture(), visibleSprite.getPosX(), visibleSprite.getPosY(), visibleSprite.getWidth(), visibleSprite.getHeight());
            else
                game.batch.draw(visibleSprite.getTexture(), visibleSprite.getPosX()+visibleSprite.getWidth(), visibleSprite.getPosY(), -visibleSprite.getWidth(), visibleSprite.getHeight());
        }

        game.batch.end();
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
