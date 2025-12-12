package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Platform extends Entity {

    private static TextureRegion singleTile;
    private static TextureRegion leftTile;
    private static TextureRegion middleTile;
    private static TextureRegion rightTile;
    public static final int tileWidth = 24;
    public static final int tileHeight = 24;

    public enum PlatformType {
        singlePlatform,
        leftPlatform,
        midPlatform,
        rightPlatform
    }

    private static Texture tileSet;

    private static void loadTextures() {
        if (tileSet==null)
            tileSet = new Texture("assets/tileset.png");
        if (singleTile==null)
            singleTile = new TextureRegion(tileSet, tileWidth*10, 0, tileWidth, tileHeight);
        if (leftTile==null)
            leftTile = new TextureRegion(tileSet, tileWidth*5, tileHeight*3, tileWidth, tileHeight);
        if (middleTile==null)
            middleTile = new TextureRegion(tileSet, tileWidth*6, tileHeight*3, tileWidth, tileHeight);
        if (rightTile==null)
            rightTile = new TextureRegion(tileSet, tileWidth*7, tileHeight*3, tileWidth, tileHeight);
    }

    public Platform(float posX, float posY, PlatformType type){
        super(posX, posY, 0,0);

        loadTextures();

        isAffectedByGravity = false;
        width = tileWidth;
        height = tileHeight;
        switch (type){
            case singlePlatform ->{
                width = tileWidth * ZOOM;
                height = tileHeight * ZOOM;
                setTextureRegion(singleTile);
            }
            case leftPlatform -> {
                width = tileWidth * ZOOM;
                height = tileHeight * ZOOM;
                setTextureRegion( leftTile);
            }
            case midPlatform -> {
                width = tileWidth * ZOOM;
                height = tileHeight * ZOOM;
                setTextureRegion(middleTile);
            }
            case rightPlatform -> {
                width = tileWidth * ZOOM;
                height = tileHeight * ZOOM;
                setTextureRegion(rightTile);
            }

            default -> throw new IllegalArgumentException("Unknown platform type");
        }


    }
}
