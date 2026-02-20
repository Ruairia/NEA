package ruairi.nea.applicationClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HowToPlayScreen implements Screen {

    private final Main game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;
    private GlyphLayout layout;

    private Controller gamepad;

    private static final String TEXT =
            "\n\nHOW TO PLAY\n\n" +
                    "Move: A and D / Left Stick\n" +
                    "Attack: Shift / X\n" +
                    "Attack Downwards: Shift + S / X + Left Stick Down\n" +
                    "Heal: H / D-Pad Down\n" +
                    "Dash: Space / RT\n" +
                    "Jump: W / A\n\n"+
                    "Press ESC or B to return\n";

    public HowToPlayScreen(Main game) {
        this.game = game;
    }
    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Main.UI_WIDTH, Main.UI_HEIGHT, camera);
        viewport.apply();
        camera.position.set(Main.UI_WIDTH / 2f, Main.UI_HEIGHT / 2f, 0);
        camera.update();

        if (Controllers.getControllers().size > 0) {
            gamepad = Controllers.getControllers().first();
        }

        FreeTypeFontGenerator gen =
                new FreeTypeFontGenerator(Gdx.files.internal("assets/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 84;
        p.color = Color.WHITE;

        font = gen.generateFont(p);
        gen.dispose();

        font.getData().setScale(0.33f);

        layout = new GlyphLayout();
        layout.setText(font, TEXT);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CORAL);

        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);

        float x = (Main.UI_WIDTH - layout.width) / 2f;
        float y = (Main.UI_HEIGHT + layout.height) / 2f;

        game.batch.begin();
        font.draw(game.batch, layout, x, y);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || gamepad!=null && gamepad.getButton(1)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() { font.dispose(); }
}