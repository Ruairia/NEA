package ruairi.nea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import ruairi.nea.gameClasses.Hero;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private Main game;
    public Hero hero;
    private OrthographicCamera camera;
    private Texture heroTexture;

    public GameScreen(Main game, int level) {
        this.game=game;
    }

    @Override
    public void show() {
        hero = new Hero(100,100, 100, 100);
        hero.setVisibility(true);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        heroTexture = new Texture("assets/fireWizard.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.5f, 0.8f,1);

        handleInput();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.update(delta);
        hero.update(delta);

        game.batch.begin();

        if (hero.isVisible()) {
            game.batch.draw(heroTexture,hero.getPosX(),hero.getPosY(),hero.getHitbox().width,hero.getHitbox().height);
        }

        game.batch.end();
    }


    private void handleInput() {
        ArrayList<String> input = new ArrayList<>();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            input.add("W");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            input.add("A");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            input.add("S");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            input.add("D");
        }

        hero.move(input);
    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (heroTexture != null) {
            heroTexture.dispose();
        }
    }
}
