package ruairi.nea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class SettingsScreen implements Screen {
    private Main game;

    public SettingsScreen(Main game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.4f, 0.4f, 1);

        float SCREENHEIGHT = Gdx.graphics.getHeight();
        float SCREENWIDTH = Gdx.graphics.getWidth();
        
        game.batch.begin();
        game.font.draw(game.batch, "SETTINGS", SCREENWIDTH / 2 - 40, SCREENHEIGHT / 2 + 100);
        game.font.draw(game.batch, "Controls:", SCREENWIDTH / 2 - 80, SCREENHEIGHT / 2 + 50);
        game.font.draw(game.batch, "WASD or Arrow Keys - Move", SCREENWIDTH / 2 - 80, SCREENHEIGHT / 2);
        game.font.draw(game.batch, "SHIFT - Sprint", SCREENWIDTH / 2 - 80, SCREENHEIGHT / 2 - 25);
        game.font.draw(game.batch, "SPACE - Attack", SCREENWIDTH / 2 - 80, SCREENHEIGHT / 2 - 50);
        game.font.draw(game.batch, "Press ESC to go back", SCREENWIDTH / 2 - 80, SCREENHEIGHT / 2 - 100);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}
}