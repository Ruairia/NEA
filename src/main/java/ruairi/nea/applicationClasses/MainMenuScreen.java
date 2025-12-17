package ruairi.nea.applicationClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
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

    private Controller gamepad;

    BitmapFont font = new BitmapFont();

    Button selectedButton;

    Button levelSelectButton;
    Button howToPlayButton;
    Button exitButton;

    float lastMovedButton = 0;

    @Override
    public void show() {

        if (Controllers.getControllers().size > 0) {
            gamepad = Controllers.getControllers().first();
        }

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


        levelSelectButton.isSelected=true;
        selectedButton = levelSelectButton;

        levelSelectButton.downButton=howToPlayButton;
        howToPlayButton.downButton=exitButton;
        exitButton.downButton=levelSelectButton;
        levelSelectButton.upButton=exitButton;
        howToPlayButton.upButton=levelSelectButton;
        exitButton.upButton=howToPlayButton;

        levelSelectButton.text = "Level Select";
        howToPlayButton.text = "How to Play";
        exitButton.text = "Exit";
    }

    @Override
    public void render(float delta) {
        lastMovedButton+=delta;

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

        if (gamepad != null && gamepad.getButton(0)){
            if (levelSelectButton.isSelected){
                game.setScreen(new LevelSelectScreen(game));
            }
            if (howToPlayButton.isSelected){
                game.setScreen(new HowToPlayScreen(game));
            }
            if (exitButton.isSelected){
                Gdx.app.exit();
            }
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)|| gamepad != null && gamepad.getAxis(1)<-0.3f && lastMovedButton>0.1f){
            selectedButton.isSelected=false;
            selectedButton.upButton.isSelected=true;
            selectedButton = selectedButton.upButton;
            lastMovedButton=0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || gamepad != null && gamepad.getAxis(1)>0.3f && lastMovedButton>0.1f){
            selectedButton.isSelected=false;
            selectedButton.downButton.isSelected=true;
            selectedButton = selectedButton.downButton;
            lastMovedButton=0;
        }

    }


    @Override public void hide() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}

}
