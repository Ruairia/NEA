package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Platform extends Entity {

    public enum PlatformType {
        GRASS,
        WIDEGRASS
    }

    private static TextureRegion grassPlatform;
    private static TextureRegion wideGrassPlatform;

    private static void loadTextures() {
        if (grassPlatform == null) {
            grassPlatform = new TextureRegion(new Texture("assets/GrassPlatform.png"));
        }
        if (wideGrassPlatform == null) {
            wideGrassPlatform = new TextureRegion(new Texture("assets/WideGrassPlatform.png"));
        }
    }

    public Platform(float posX, float posY, PlatformType type){
        super(posX, posY, 0,0);

        loadTextures();

        isAffectedByGravity = false;
        switch (type){
            case GRASS -> {
                width = 256;
                height = 64;
                setTextureRegion(grassPlatform);
            }
            case WIDEGRASS -> {
                width = 512;
                height = 64;
                setTextureRegion(wideGrassPlatform);
            }

            default -> throw new IllegalArgumentException("Unknown platform type");
        }


    }
}
