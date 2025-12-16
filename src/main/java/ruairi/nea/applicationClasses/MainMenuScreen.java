package ruairi.nea.applicationClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;

import static ruairi.nea.applicationClasses.Button.ButtonType.MainMenu;

public class MainMenuScreen implements Screen{
    private Main game;

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    BitmapFont font = new BitmapFont();

    Button levelSelectButton;
    Button howToPlayButton;
    Button exitButton;

    @Override
    public void show() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("assets/font.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30; // Font size
        parameter.color = Color.WHITE;

        font = generator.generateFont(parameter);
        generator.dispose();

        float centreX = Gdx.graphics.getWidth() / 2;
        float centreY = Gdx.graphics.getHeight() / 2;

        float buttonWidth = 48*5;
        float buttonHeight = 16*5;

        levelSelectButton = new Button(centreX-buttonWidth/2,centreY+buttonHeight/2,buttonWidth,buttonHeight, MainMenu);
        howToPlayButton = new Button(centreX-buttonWidth/2,centreY-buttonHeight,buttonWidth,buttonHeight, MainMenu);
        exitButton = new Button(centreX-buttonWidth/2,centreY-buttonHeight*2.5f,buttonWidth,buttonHeight, MainMenu);

        levelSelectButton.text = "Level Select";
        howToPlayButton.text = "How to Play";
        exitButton.text = "Exit";
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.3f,0.1f,0.1f,1);

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();


        levelSelectButton.isHovered = levelSelectButton.isUnderMouse(mouseX, mouseY);
        howToPlayButton.isHovered = howToPlayButton.isUnderMouse(mouseX, mouseY);
        exitButton.isHovered = exitButton.isUnderMouse(mouseX, mouseY);


        game.batch.begin();
        levelSelectButton.draw(game.batch,font);
        howToPlayButton.draw(game.batch,font);
        exitButton.draw(game.batch,font);
        game.batch.end();



        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            if (levelSelectButton.isUnderMouse(mouseX,mouseY)){
                game.setScreen(new LevelSelectScreen(game));
            }
            if (howToPlayButton.isUnderMouse(mouseX,mouseY)){
                game.setScreen(new HowToPlayScreen(game));
            }
            if (exitButton.isUnderMouse(mouseX,mouseY)){
                Gdx.app.exit();
            }
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }
    }


    @Override public void hide() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}

}
