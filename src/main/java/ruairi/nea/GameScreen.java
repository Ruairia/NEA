package ruairi.nea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import ruairi.nea.gameClasses.Hero;
import ruairi.nea.gameClasses.Sprite;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private Main game;

    public ArrayList<Sprite> visibleSprites = new ArrayList<>();

    public Hero hero;


    private OrthographicCamera camera;

    public GameScreen(Main game, int level) {
        this.game=game;
    }

    @Override
    public void show() {
        hero = new Hero(100,100, 200, 200);
        hero.setVisibility(true);
        hero.setTexture(new Texture("assets/fireWizard.png"));

        visibleSprites.add(hero);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);


    }

    @Override
    public void render(float delta) {//Game Loop
        ScreenUtils.clear(0.3f, 0.5f, 0.8f,1); //Draw background

        System.out.println(hero);

        update(delta); //Move hero and sprites


        //Rendering
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

       for (Sprite visibleSprite : visibleSprites){
           game.batch.draw(visibleSprite.getTexture(), visibleSprite.getPosX(), visibleSprite.getPosY());
       }

        game.batch.end();
    }


    private void update(float delta) {
        hero.update(getInputs(), delta);
        for (Sprite visibleSprite : visibleSprites){
            visibleSprite.update(delta);
        }
    }

    private ArrayList<String> getInputs() {
        ArrayList<String> inputs = new ArrayList<>();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            inputs.add("W");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            inputs.add("A");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            inputs.add("S");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            inputs.add("D");
        }
        return inputs;
    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (hero.getTexture() != null) {
            hero.getTexture().dispose();
        }
    }
}
