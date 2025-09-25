package ruairi.nea;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import ruairi.nea.gameClasses.Hero;

public class GameScreen implements Screen {
    private Main game;
    public GameScreen(Main game, int level) {this.game=game;}
    public Hero hero;



    @Override
    public void show() {
        hero = new Hero(100,100, 100, 100);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f, 0.5f, 0.8f,1);
        game.update(delta);
        game.batch.begin();

        game.batch.end();
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

    }
}
