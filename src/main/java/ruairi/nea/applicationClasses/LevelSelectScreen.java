package ruairi.nea.applicationClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import ruairi.nea.gameClasses.GameScreen;

public class LevelSelectScreen implements Screen {
    private Main game;


    Button levelOneButton;
    Button levelTwoButton;
    Button levelThreeButton;
    Button levelFourButton;

    public LevelSelectScreen(Main game) {
        this.game = game;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("assets/font.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.color = Color.WHITE;

        font = generator.generateFont(parameter);
        generator.dispose();
    }

    BitmapFont font;

    @Override
    public void render(float delta) {



        ScreenUtils.clear(0.3f,0.2f,0.05f,1f);

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();


        levelOneButton.isHovered = levelOneButton.isUnderMouse(mouseX, mouseY);
        levelTwoButton.isHovered = levelTwoButton.isUnderMouse(mouseX, mouseY);
        levelThreeButton.isHovered = levelThreeButton.isUnderMouse(mouseX, mouseY);
        levelFourButton.isHovered = levelFourButton.isUnderMouse(mouseX, mouseY);



        game.batch.begin();

        levelOneButton.draw(game.batch,font);
        levelTwoButton.draw(game.batch,font);
        levelThreeButton.draw(game.batch,font);
        levelFourButton.draw(game.batch,font);

        game.batch.end();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (levelOneButton.isUnderMouse(mouseX, mouseY)) game.setScreen(new GameScreen(game, 1));
            if (levelTwoButton.isUnderMouse(mouseX, mouseY)) game.setScreen(new GameScreen(game, 2));
            if (levelThreeButton.isUnderMouse(mouseX, mouseY)) game.setScreen(new GameScreen(game, 3));
            if (levelFourButton.isUnderMouse(mouseX, mouseY)) game.setScreen(new GameScreen(game, 4));
        }

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

    @Override public void show() {

        game.batch.getProjectionMatrix().idt();
        game.batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float centreX = Gdx.graphics.getWidth() / 2;
        float centreY = Gdx.graphics.getHeight() / 2;



        float buttonSize = 128;

        levelOneButton = new Button(centreX/2, centreY+buttonSize/2,buttonSize,buttonSize, Button.ButtonType.LevelSelect);
        levelTwoButton = new Button(centreX*1.5f-buttonSize, centreY+buttonSize/2,buttonSize,buttonSize, Button.ButtonType.LevelSelect);
        levelThreeButton = new Button(centreX/2, centreY-buttonSize*1.5f,buttonSize,buttonSize, Button.ButtonType.LevelSelect);
        levelFourButton = new Button(centreX*1.5f-buttonSize, centreY-buttonSize*1.5f,buttonSize,buttonSize, Button.ButtonType.LevelSelect);

        levelOneButton.text = "1";
        levelTwoButton.text = "2";
        levelThreeButton.text = "3";
        levelFourButton.text = "4";

    }

    @Override public void hide() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}
}