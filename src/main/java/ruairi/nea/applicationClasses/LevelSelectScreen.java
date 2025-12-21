package ruairi.nea.applicationClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ruairi.nea.gameClasses.GameScreen;

public class LevelSelectScreen implements Screen {

    private final Main game;
    private Controller gamepad;

    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;

    private Button selectedButton;
    private Button levelOneButton;
    private Button levelTwoButton;
    private Button levelThreeButton;
    private Button levelFourButton;
    private Button levelFiveButton;

    private float lastMovedButton = 0;
    private boolean wasAButtonPressed = true;

    public LevelSelectScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        if (Controllers.getControllers().size > 0) {
            gamepad = Controllers.getControllers().first();
        }

        camera = new OrthographicCamera();
        viewport = new FitViewport(Main.UI_WIDTH, Main.UI_HEIGHT, camera);
        viewport.apply();
        camera.position.set(Main.UI_WIDTH / 2f, Main.UI_HEIGHT / 2f, 0);
        camera.update();

        FreeTypeFontGenerator gen =
                new FreeTypeFontGenerator(Gdx.files.internal("assets/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 120;
        p.color = Color.WHITE;
        font = gen.generateFont(p);
        gen.dispose();

        font.getData().setScale(0.33f);

        float centreX = Main.UI_WIDTH / 2f;
        float centreY = Main.UI_HEIGHT / 2f;
        float buttonSize = 128;

        levelOneButton   = new Button(centreX / 2f,           centreY + buttonSize / 2f, buttonSize, buttonSize, Button.ButtonType.LevelSelect);
        levelTwoButton   = new Button(centreX * 1.5f - buttonSize,  centreY + buttonSize / 2f, buttonSize, buttonSize, Button.ButtonType.LevelSelect);
        levelThreeButton = new Button(centreX / 2f, centreY - buttonSize * 0.75f, buttonSize, buttonSize, Button.ButtonType.LevelSelect);
        levelFourButton  = new Button(centreX * 1.5f - buttonSize,  centreY - buttonSize * 0.75f, buttonSize, buttonSize, Button.ButtonType.LevelSelect);
        levelFiveButton = new Button (centreX - buttonSize/2, 0, buttonSize,buttonSize, Button.ButtonType.LevelSelect);

        levelOneButton.text = "1";
        levelTwoButton.text = "2";
        levelThreeButton.text = "3";
        levelFourButton.text = "4";
        levelFiveButton.text = "5";

        levelOneButton.upButton  = levelFiveButton;
        levelOneButton.rightButton = levelTwoButton;
        levelOneButton.downButton  = levelThreeButton;

        levelTwoButton.upButton   = levelFiveButton;
        levelTwoButton.leftButton  = levelOneButton;
        levelTwoButton.downButton  = levelFourButton;

        levelThreeButton.upButton  = levelOneButton;
        levelThreeButton.rightButton = levelFourButton;
        levelThreeButton.downButton  = levelFiveButton;

        levelFourButton.upButton   = levelTwoButton;
        levelFourButton.leftButton = levelThreeButton;
        levelFourButton.downButton  = levelFiveButton;

        levelFiveButton.upButton  = levelFourButton;
        levelFiveButton.downButton = levelOneButton;

        levelOneButton.isSelected = true;
        selectedButton = levelOneButton;
    }

    @Override
    public void render(float delta) {
        lastMovedButton += delta;
        ScreenUtils.clear(0.3f, 0.2f, 0.05f, 1);

        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);

        Vector2 mouse = viewport.unproject(
                new Vector2(Gdx.input.getX(), Gdx.input.getY())
        );

        levelOneButton.isHovered   = levelOneButton.isUnderMouse(mouse.x, mouse.y);
        levelTwoButton.isHovered   = levelTwoButton.isUnderMouse(mouse.x, mouse.y);
        levelThreeButton.isHovered = levelThreeButton.isUnderMouse(mouse.x, mouse.y);
        levelFourButton.isHovered  = levelFourButton.isUnderMouse(mouse.x, mouse.y);

        game.batch.begin();
        levelOneButton.draw(game.batch, font);
        levelTwoButton.draw(game.batch, font);
        levelThreeButton.draw(game.batch, font);
        levelFourButton.draw(game.batch, font);
        levelFiveButton.draw(game.batch, font);
        game.batch.end();

        // Mouse input
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (levelOneButton.isHovered)   game.setScreen(new GameScreen(game, 1));
            if (levelTwoButton.isHovered)   game.setScreen(new GameScreen(game, 2));
            if (levelThreeButton.isHovered) game.setScreen(new GameScreen(game, 3));
            if (levelFourButton.isHovered)  game.setScreen(new GameScreen(game, 4));
            if (levelFiveButton.isHovered)  game.setScreen(new GameScreen(game, 5));
        }

        // Controller confirm
        boolean isAButtonPressed = gamepad != null && gamepad.getButton(0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (isAButtonPressed && !wasAButtonPressed)) {
            if (levelOneButton.isSelected)   game.setScreen(new GameScreen(game, 1));
            if (levelTwoButton.isSelected)   game.setScreen(new GameScreen(game, 2));
            if (levelThreeButton.isSelected) game.setScreen(new GameScreen(game, 3));
            if (levelFourButton.isSelected)  game.setScreen(new GameScreen(game, 4));
            if (levelFiveButton.isSelected)  game.setScreen(new GameScreen(game, 5));
        }
        wasAButtonPressed = isAButtonPressed;

        // Keyboard shortcuts
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || gamepad != null && gamepad.getButton(1)) {
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
            game.setScreen(new GameScreen(game, 5));
        }

        // Navigation
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)
                || gamepad != null && gamepad.getAxis(1) < -0.3f && lastMovedButton > 0.1f) {
            if (selectedButton.upButton != null) {
                selectedButton.isSelected = false;
                selectedButton = selectedButton.upButton;
                selectedButton.isSelected = true;
                lastMovedButton = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
                || gamepad != null && gamepad.getAxis(1) > 0.3f && lastMovedButton > 0.1f) {
            if (selectedButton.downButton != null) {
                selectedButton.isSelected = false;
                selectedButton = selectedButton.downButton;
                selectedButton.isSelected = true;
                lastMovedButton = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
                || gamepad != null && gamepad.getAxis(0) < -0.3f && lastMovedButton > 0.1f) {
            if (selectedButton.leftButton != null) {
                selectedButton.isSelected = false;
                selectedButton = selectedButton.leftButton;
                selectedButton.isSelected = true;
                lastMovedButton = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
                || gamepad != null && gamepad.getAxis(0) > 0.3f && lastMovedButton > 0.1f) {
            if (selectedButton.rightButton != null) {
                selectedButton.isSelected = false;
                selectedButton = selectedButton.rightButton;
                selectedButton.isSelected = true;
                lastMovedButton = 0;
            }
        }
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() { font.dispose(); }
}
