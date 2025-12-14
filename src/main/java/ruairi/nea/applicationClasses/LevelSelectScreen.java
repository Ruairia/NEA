package ruairi.nea.applicationClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import ruairi.nea.gameClasses.GameScreen;

public class LevelSelectScreen implements Screen {
    private Main game;

    public LevelSelectScreen(Main game) {

        this.game = game;
    }

    @Override
    public void render(float delta) {
        float SCREENHEIGHT = Gdx.graphics.getHeight();
        float SCREENWIDTH = Gdx.graphics.getWidth();

        game.batch.getProjectionMatrix().idt();
        game.batch.getProjectionMatrix().setToOrtho2D(0, 0, SCREENWIDTH, SCREENHEIGHT);

        ScreenUtils.clear(Color.CHARTREUSE);



        game.batch.begin();
        game.font.draw(game.batch, "LEVEL SELECT", SCREENWIDTH / 2 - 60, SCREENHEIGHT / 2 + 100);
        game.font.draw(game.batch, "Press 1-4 for Levels 1-4", SCREENWIDTH / 2 - 80, SCREENHEIGHT / 2 + 50);
        game.font.draw(game.batch, "Press ESC to go back", SCREENWIDTH / 2 - 80, SCREENHEIGHT / 2 - 50);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            game.setScreen(new GameScreen(game, 1));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            game.setScreen(new GameScreen(game, 2));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            game.setScreen(new GameScreen(game, 3));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            game.setScreen(new GameScreen(game, 4));
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)){
            game.setScreen(new GameScreen(game,10));
        }
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}
}