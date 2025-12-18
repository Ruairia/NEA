package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import ruairi.nea.applicationClasses.Main;

import java.util.ArrayList;

public class Background {

    private static class BackgroundLayer{
        Texture texture;
        float parallax;
        boolean inFrontOfCamera = false;

        BackgroundLayer(Texture texture, float parallax,boolean inFrontOfCamera) {
            this.texture = texture;
            this.parallax = parallax;
            this.inFrontOfCamera = inFrontOfCamera;
        }
    }

    private final ArrayList<BackgroundLayer> layers = new ArrayList<>();

    final float OAK_BACKGROUND_STRETCH_X = 2.5f;
    final float OAK_BACKGROUND_STRETCH_Y = 1.7f;
    final float CAVE_BACKGROUND_STRETCH_X = 2.5f;
    final float CAVE_BACKGROUND_STRETCH_Y = 1.7f;

    final String backgroundType;

    public Background(int level) {

        switch (level){
            case 3,4:
                addLayer(("assets/CaveBackgroundFar.png"),0.05f, false);
                addLayer(("assets/CaveBackgroundMid.png"),0.1f, false);
                addLayer(("assets/CaveBackgroundNearA.png"),0.2f, false);
                backgroundType = "Cave";
                break;
            default:
                addLayer(("assets/OakBackgroundFar.png"),0.05f, false);
                addLayer(("assets/OakBackgroundMid.png"),0.1f, false);
                addLayer(("assets/OakBackgroundNear.png"),0.2f, false);
                backgroundType = "Oak";
        }
    }

    private void addLayer(String texturePath, float parallax, boolean inFrontOfCamera) {
        layers.add(new BackgroundLayer(new Texture(texturePath), parallax, inFrontOfCamera));
    }

    public void drawBackgroundBehindCamera(Camera camera, Main game){
        for (BackgroundLayer layer : layers) {
            if (layer.inFrontOfCamera) continue;
            drawLayer(camera, game, layer, layer.parallax);
        }
    }

    public void drawBackgroundInFrontOfCamera(Camera camera, Main game){
        for (BackgroundLayer layer : layers) {
            if (!layer.inFrontOfCamera) continue;
            drawLayer(camera, game, layer, layer.parallax);
        }
    }

    private void drawLayer(Camera camera, Main game, BackgroundLayer layer, float parallaxCoefficient) {

        float backgroundStretchX;
        float backgroundStretchY;

        switch (backgroundType){
            case "Cave":
                backgroundStretchX = CAVE_BACKGROUND_STRETCH_X;
                backgroundStretchY = CAVE_BACKGROUND_STRETCH_Y;
                break;
            default:
                backgroundStretchX = OAK_BACKGROUND_STRETCH_X;
                backgroundStretchY = OAK_BACKGROUND_STRETCH_Y;
        }

        float parallaxOffsetX = camera.position.x * parallaxCoefficient;
        float parallaxOffsetY = camera.position.y * parallaxCoefficient;

        float drawY = camera.position.y - camera.viewportHeight/2 - parallaxOffsetY;
        float drawHeight = camera.viewportHeight * backgroundStretchY;

        int textureWidth = (int) (layer.texture.getWidth()* backgroundStretchX);


        float baseX = camera.position.x - camera.viewportWidth/2 - parallaxOffsetX;


        float x = baseX - absMod(parallaxOffsetX, textureWidth);

        while (x < camera.position.x + camera.viewportWidth/2) {
            game.batch.draw(layer.texture, x, drawY, textureWidth, drawHeight);
            x += textureWidth;
        }
    }




    private float absMod(float a, float b) {
        return ((a % b) + b) % b;
    }

    public void dispose() {
        for (BackgroundLayer layer : layers) {
            layer.texture.dispose();
        }
    }
}