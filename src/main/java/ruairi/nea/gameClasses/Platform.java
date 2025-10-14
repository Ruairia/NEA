package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Texture;

public class Platform extends Sprite{
    public Platform(float posX, float posY, PlatformType type){
        super(posX, posY, 0,0);
        fixed = true;
        switch (type){
            case GRASS -> {
                width = 300;
                height = 50;
                setTexture(new Texture("assets/GrassPlatform.png"));
            }
            case WIDEGRASS -> {
                width = 500;
                height = 500;
                setTexture(new Texture("assets/WideGrassPlatform.png"));
            }

            default -> throw new IllegalArgumentException("Unknown platform type");
        }


    }
}
