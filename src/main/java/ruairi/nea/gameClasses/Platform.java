package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Texture;

public class Platform extends Entity {
    public Platform(float posX, float posY, PlatformType type){
        super(posX, posY, 0,0);
        fixed = true;
        switch (type){
            case GRASS -> {
                width = 256;
                height = 64;
                setTexture(new Texture("assets/GrassPlatform.png"));
            }
            case WIDEGRASS -> {
                width = 512;
                height = 64;
                setTexture(new Texture("assets/WideGrassPlatform.png"));
            }

            default -> throw new IllegalArgumentException("Unknown platform type");
        }


    }
}
