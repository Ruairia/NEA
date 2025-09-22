package ruairi.nea;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;

    @Override
    public void create () {
        batch = new SpriteBatch();
    }

    @Override
    public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime();
    }

    @Override
    public void dispose () {
        batch.dispose();
    }
}
