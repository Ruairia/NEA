package ruairi.nea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import ruairi.nea.gameClasses.*;

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
    public void show() { //Run Once when screen is shown
        hero = new Hero(100, 100, 160, 160);


        visibleSprites.add(new Platform(0, 0, PlatformType.GRASS));
        visibleSprites.add(new Platform(256, 0, PlatformType.GRASS));
        visibleSprites.add(new Platform(512, 0, PlatformType.GRASS));
        visibleSprites.add(new Platform(768, 0, PlatformType.GRASS));
        visibleSprites.add(new Platform(512, 128, PlatformType.GRASS));



        visibleSprites.add(hero);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);


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
    }


    @Override
    public void render(float delta) {//Game Loop, runs once a frame
        ScreenUtils.clear(0.05f, 0.1f, 0.07f, 1); //Draw background

        perFrameLogic(delta);


        //Rendering
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
