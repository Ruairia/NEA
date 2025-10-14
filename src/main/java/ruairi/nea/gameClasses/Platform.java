package ruairi.nea.gameClasses;

public class Platform extends Sprite{
    public Platform(float posX, float posY, PlatformType type){
        super(posX, posY, 0,0);
        switch (type){


            default -> throw new IllegalArgumentException("Unknown platform type");
        }


    }
}
