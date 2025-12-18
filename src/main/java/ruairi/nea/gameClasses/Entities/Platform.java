package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Platform extends Entity {

    private static TextureRegion singlePlatformTile;
    private static TextureRegion leftPlatformTile;
    private static TextureRegion middlePlatformTile;
    private static TextureRegion rightPlatformTile;
    public static final int tileWidth = 24;
    public static final int tileHeight = 24;

    public enum PlatformType {
        singlePlatform,
        leftPlatform,
        midPlatform,
        rightPlatform
    }

    protected static Texture tileSet;

    public static void loadTextures(String levelType) {
        if (tileSet==null)
            tileSet = new Texture("assets/tileset.png");

        if (levelType == "CAVE"){
            if (singlePlatformTile == null)
                singlePlatformTile = new TextureRegion(tileSet, tileWidth * 12, 0, tileWidth, tileHeight);
            if (leftPlatformTile == null)
                leftPlatformTile = new TextureRegion(tileSet, tileWidth * 9, tileHeight * 3, tileWidth, tileHeight);
            if (middlePlatformTile == null)
                middlePlatformTile = new TextureRegion(tileSet, tileWidth * 12, tileHeight * 10, tileWidth, tileHeight);
            if (rightPlatformTile == null)
                rightPlatformTile = new TextureRegion(tileSet, tileWidth * 11, tileHeight * 3, tileWidth, tileHeight);
        }

        else {
            if (singlePlatformTile ==null)
                singlePlatformTile = new TextureRegion(tileSet, tileWidth*10, 0, tileWidth, tileHeight);
            if (leftPlatformTile ==null)
                leftPlatformTile = new TextureRegion(tileSet, tileWidth*5, tileHeight*3, tileWidth, tileHeight);
            if (middlePlatformTile ==null)
                middlePlatformTile = new TextureRegion(tileSet, tileWidth*6, tileHeight*3, tileWidth, tileHeight);
            if (rightPlatformTile ==null)
                rightPlatformTile = new TextureRegion(tileSet, tileWidth*7, tileHeight*3, tileWidth, tileHeight);
        }
    }

    public static void clearTextures(){
        singlePlatformTile = null;
        leftPlatformTile = null;
        middlePlatformTile = null;
        rightPlatformTile = null;
    }

    public Platform(float posX, float posY, PlatformType type){
        super(posX, posY, 0,0);

        isAffectedByGravity = false;
        width = tileWidth*ZOOM;
        height = tileHeight*ZOOM;
        switch (type){
            case singlePlatform ->{
                setFrame(singlePlatformTile);
            }
            case leftPlatform -> {
                setFrame(leftPlatformTile);
            }
            case midPlatform -> {
                setFrame(middlePlatformTile);
            }
            case rightPlatform -> {
                setFrame(rightPlatformTile);
            }

            default -> throw new IllegalArgumentException("Unknown platform type");
        }


    }
    public Platform(){
        super(0,0,0,0);
    }
}
