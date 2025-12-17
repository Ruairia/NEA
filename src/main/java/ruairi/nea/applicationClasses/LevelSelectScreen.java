package ruairi.nea.applicationClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import ruairi.nea.gameClasses.GameScreen;

public class LevelSelectScreen implements Screen {
    private Main game;
    private Controller gamepad;

    Button selectedButton;
    Button levelOneButton;
    Button levelTwoButton;
    Button levelThreeButton;
    Button levelFourButton;

    float lastMovedButton = 0;
    boolean wasButtonPressed = true;

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
        lastMovedButton += delta;

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

        // Mouse input
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (levelOneButton.isUnderMouse(mouseX, mouseY)) game.setScreen(new GameScreen(game, 1));
            if (levelTwoButton.isUnderMouse(mouseX, mouseY)) game.setScreen(new GameScreen(game, 2));
            if (levelThreeButton.isUnderMouse(mouseX, mouseY)) game.setScreen(new GameScreen(game, 3));
            if (levelFourButton.isUnderMouse(mouseX, mouseY)) game.setScreen(new GameScreen(game, 4));
        }

        // Controller/Keyboard input for selecting level
        boolean isButtonPressed = gamepad != null && gamepad.getButton(0);

        if (isButtonPressed && !wasButtonPressed) {
            if (levelOneButton.isSelected) game.setScreen(new GameScreen(game, 1));
            if (levelTwoButton.isSelected) game.setScreen(new GameScreen(game, 2));
            if (levelThreeButton.isSelected) game.setScreen(new GameScreen(game, 3));
            if (levelFourButton.isSelected) game.setScreen(new GameScreen(game, 4));
        }

        wasButtonPressed = isButtonPressed;

        // Keyboard shortcuts
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
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            game.setScreen(new GameScreen(game, 10));
        }

        // Controller/Keyboard navigation
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || gamepad != null && gamepad.getAxis(1) < -0.3f && lastMovedButton > 0.1f) {
            if (selectedButton.upButton != null) {
                selectedButton.isSelected = false;
                selectedButton.upButton.isSelected = true;
                selectedButton = selectedButton.upButton;
                lastMovedButton = 0;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || gamepad != null && gamepad.getAxis(1) > 0.3f && lastMovedButton > 0.1f) {
            if (selectedButton.downButton != null) {
                selectedButton.isSelected = false;
                selectedButton.downButton.isSelected = true;
                selectedButton = selectedButton.downButton;
                lastMovedButton = 0;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || gamepad != null && gamepad.getAxis(0) < -0.3f && lastMovedButton > 0.1f) {
            if (selectedButton.leftButton != null) {
                selectedButton.isSelected = false;
                selectedButton.leftButton.isSelected = true;
                selectedButton = selectedButton.leftButton;
                lastMovedButton = 0;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || gamepad != null && gamepad.getAxis(0) > 0.3f && lastMovedButton > 0.1f) {
            if (selectedButton.rightButton != null) {
                selectedButton.isSelected = false;
                selectedButton.rightButton.isSelected = true;
                selectedButton = selectedButton.rightButton;
                lastMovedButton = 0;
            }
        }
    }

    @Override public void show() {
        // Initialize controller
        if (Controllers.getControllers().size > 0) {
            gamepad = Controllers.getControllers().first();
        }

        game.batch.getProjectionMatrix().idt();
        game.batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float centreX = Gdx.graphics.getWidth() / 2;
        float centreY = Gdx.graphics.getHeight() / 2;

        float buttonSize = 128;

        levelOneButton = new Button(centreX/2, centreY+buttonSize/2, buttonSize, buttonSize, Button.ButtonType.LevelSelect);
        levelTwoButton = new Button(centreX*1.5f-buttonSize, centreY+buttonSize/2, buttonSize, buttonSize, Button.ButtonType.LevelSelect);
        levelThreeButton = new Button(centreX/2, centreY-buttonSize*1.5f, buttonSize, buttonSize, Button.ButtonType.LevelSelect);
        levelFourButton = new Button(centreX*1.5f-buttonSize, centreY-buttonSize*1.5f, buttonSize, buttonSize, Button.ButtonType.LevelSelect);

        levelOneButton.text = "1";
        levelTwoButton.text = "2";
        levelThreeButton.text = "3";
        levelFourButton.text = "4";

        // Set up button navigation (2x2 grid layout)
        levelOneButton.rightButton = levelTwoButton;
        levelOneButton.downButton = levelThreeButton;

        levelTwoButton.leftButton = levelOneButton;
        levelTwoButton.downButton = levelFourButton;

        levelThreeButton.upButton = levelOneButton;
        levelThreeButton.rightButton = levelFourButton;

        levelFourButton.upButton = levelTwoButton;
        levelFourButton.leftButton = levelThreeButton;

        // Set initial selection
        levelOneButton.isSelected = true;
        selectedButton = levelOneButton;
    }

    @Override public void hide() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}
}