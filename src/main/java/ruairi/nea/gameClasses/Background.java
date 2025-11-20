package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import ruairi.nea.applicationClasses.Main;

public class Background {
    final float BACKGROUND_STRETCH_X = 2.5f;
    final float BACKGROUND_STRETCH_Y = 1.7f;

    private final Texture backgroundFar;
    private final Texture backgroundMid;
    private final Texture backgroundNear;

    public Background(int level) {
        switch (level){
            default:
                backgroundFar = new Texture("assets/background_far.png");
                backgroundMid = new Texture("assets/background_mid.png");
                backgroundNear = new Texture("assets/background_near.png");
        }
    }

    public void drawBackground(Camera camera, Main game){
        drawLayer(camera, game, backgroundFar, 0.05f);
        drawLayer(camera, game, backgroundMid, 0.1f);
        drawLayer(camera, game, backgroundNear, 0.2f);
    }

    private void drawLayer(Camera camera, Main game, Texture texture, float parallaxCoefficient) {
        float parallaxOffsetX = camera.position.x * parallaxCoefficient;
        float parallaxOffsetY = camera.position.y * parallaxCoefficient;

        float drawY = camera.position.y - camera.viewportHeight/2 - parallaxOffsetY;
        float drawHeight = camera.viewportHeight * BACKGROUND_STRETCH_Y;

        int textureWidth = (int) (texture.getWidth()* BACKGROUND_STRETCH_X);


        float baseX = camera.position.x - camera.viewportWidth/2 - parallaxOffsetX;


        float x = baseX - absMod(parallaxOffsetX, textureWidth);

        while (x < camera.position.x + camera.viewportWidth/2) {
            game.batch.draw(texture, x, drawY, textureWidth, drawHeight);
            x += textureWidth;
        }
    }


    private float absMod(float a, float b) {
        return ((a % b) + b) % b;
    }

    public void dispose() {
        backgroundFar.dispose();
        backgroundMid.dispose();
        backgroundNear.dispose();
    }
}