package ruairi.nea.applicationClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen{
    private Main game;

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1);
        float SCREENHEIGHT = Gdx.graphics.getHeight();
        float SCREENWIDTH = Gdx.graphics.getWidth();
        game.batch.begin();
        game.font.draw(game.batch, "PIXEL 46", SCREENWIDTH / 2 - 50, SCREENHEIGHT / 2 + 100);
        game.font.draw(game.batch, "Press 1 to Play", SCREENWIDTH / 2 - 80, SCREENHEIGHT / 2);
        game.font.draw(game.batch, "Press 2 for Settings", SCREENWIDTH / 2 - 80, SCREENHEIGHT / 2 - 50);
        game.font.draw(game.batch, "Press ESC to Exit", SCREENWIDTH / 2 - 80, SCREENHEIGHT / 2 - 100);
        game.batch.end();

        // Handle input

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            game.setScreen(new LevelSelectScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            game.setScreen(new SettingsScreen(game));
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            game.setScreen(new SettingsScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}

}
