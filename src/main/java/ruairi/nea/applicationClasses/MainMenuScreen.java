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

import static ruairi.nea.applicationClasses.Button.ButtonType.MainMenu;

public class MainMenuScreen implements Screen {

    private final Main game;
    private Controller gamepad;
    private boolean wasBButtonPressed = true;

    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;

    private Button selectedButton;
    private Button levelSelectButton, howToPlayButton, exitButton;

    private float lastMovedButton = 0;

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        if (Controllers.getControllers().size > 0) {
            gamepad = Controllers.getControllers().first();
        }

        wasBButtonPressed = true;

        camera = new OrthographicCamera();
        viewport = new FitViewport(Main.UI_WIDTH, Main.UI_HEIGHT, camera);
        viewport.apply();
        camera.position.set(Main.UI_WIDTH / 2f, Main.UI_HEIGHT / 2f, 0);
        camera.update();

        FreeTypeFontGenerator gen =
                new FreeTypeFontGenerator(Gdx.files.internal("assets/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 90;
        p.color = Color.WHITE;
        font = gen.generateFont(p);
        gen.dispose();

        font.getData().setScale(0.33f);

        float centreX = Main.UI_WIDTH / 2f;
        float centreY = Main.UI_HEIGHT / 2f;
        float buttonWidth = 240;
        float buttonHeight = 80;

        levelSelectButton = new Button(centreX - buttonWidth / 2, centreY + buttonHeight/2, buttonWidth, buttonHeight, MainMenu);
        howToPlayButton   = new Button(centreX - buttonWidth / 2, centreY-buttonHeight/2, buttonWidth, buttonHeight, MainMenu);
        exitButton        = new Button(centreX - buttonWidth / 2, centreY - buttonHeight*1.5f, buttonWidth, buttonHeight, MainMenu);

        levelSelectButton.text = "Level Select";
        howToPlayButton.text   = "How to Play";
        exitButton.text        = "Exit";

        levelSelectButton.upButton = exitButton;
        levelSelectButton.downButton = howToPlayButton;
        howToPlayButton.upButton = levelSelectButton;
        howToPlayButton.downButton = exitButton;
        exitButton.upButton = howToPlayButton;
        exitButton.downButton = levelSelectButton;

        levelSelectButton.isSelected = true;
        selectedButton = levelSelectButton;
    }

    @Override
    public void render(float delta) {
        lastMovedButton += delta;
        ScreenUtils.clear(0.3f, 0.1f, 0.1f, 1);

        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);

        Vector2 mouse = viewport.unproject(
                new Vector2(Gdx.input.getX(), Gdx.input.getY())
        );

        levelSelectButton.isHovered = levelSelectButton.isUnderMouse(mouse.x, mouse.y);
        howToPlayButton.isHovered   = howToPlayButton.isUnderMouse(mouse.x, mouse.y);
        exitButton.isHovered        = exitButton.isUnderMouse(mouse.x, mouse.y);

        game.batch.begin();
        levelSelectButton.draw(game.batch, font);
        howToPlayButton.draw(game.batch, font);
        exitButton.draw(game.batch, font);
        game.batch.end();

        // Mouse
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (levelSelectButton.isHovered) game.setScreen(new LevelSelectScreen(game));
            if (howToPlayButton.isHovered) game.setScreen(new HowToPlayScreen(game));
            if (exitButton.isHovered) Gdx.app.exit();
        }

        // Gamepad confirm
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || (gamepad != null && gamepad.getButton(0))) {
            if (selectedButton == levelSelectButton) game.setScreen(new LevelSelectScreen(game));
            if (selectedButton == howToPlayButton) game.setScreen(new HowToPlayScreen(game));
            if (selectedButton == exitButton) Gdx.app.exit();
        }

        // Keyboard
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (gamepad != null && gamepad.getButton(1) && !wasBButtonPressed) {
            Gdx.app.exit();
        }
        else if (gamepad!=null && !gamepad.getButton(1)) wasBButtonPressed = false;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)
                || gamepad != null && gamepad.getAxis(1) < -0.3f && lastMovedButton > 0.1f) {
            selectedButton.isSelected = false;
            selectedButton = selectedButton.upButton;
            selectedButton.isSelected = true;
            lastMovedButton = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
                || gamepad != null && gamepad.getAxis(1) > 0.3f && lastMovedButton > 0.1f) {
            selectedButton.isSelected = false;
            selectedButton = selectedButton.downButton;
            selectedButton.isSelected = true;
            lastMovedButton = 0;
        }
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() { font.dispose(); }
}
