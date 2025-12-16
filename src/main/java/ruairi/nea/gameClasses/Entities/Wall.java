package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Wall extends Platform {
    private static TextureRegion singleWallTile;
    private static TextureRegion bottomWallTile;
    private static TextureRegion middleWallTile;
    private static TextureRegion topWallTile;
    public static final int tileWidth = 24;
    public static final int tileHeight = 24;

    public enum WallType {
        singleWall,
        bottomWall,
        middleWall,
        topWall
    }
    public WallType type;

    public Wall(float posX, float posY, WallType type) {
        this.posX = posX;
        this.posY = posY;
        width = tileWidth*ZOOM;
        height = tileHeight*ZOOM;
        isAffectedByGravity = false;

        this.type = type;

        loadTextures();

        switch (type){
            case singleWall -> setFrame(singleWallTile);
            case bottomWall -> setFrame(bottomWallTile);
            case middleWall -> setFrame(middleWallTile);
            case topWall -> setFrame(topWallTile);
        }
    }

    public static void loadTextures(){
        if (tileSet==null)
            tileSet = new Texture("assets/tileset.png");
        if (singleWallTile ==null)
            singleWallTile = new TextureRegion(tileSet, tileWidth*10, 0, tileWidth, tileHeight);
        if (bottomWallTile ==null)
            bottomWallTile = new TextureRegion(tileSet, tileWidth*6, tileHeight*5, tileWidth, tileHeight);
        if (middleWallTile ==null)
            middleWallTile = new TextureRegion(tileSet, tileWidth*6, tileHeight*4, tileWidth, tileHeight);
        if (topWallTile ==null)
            topWallTile = new TextureRegion(tileSet, tileWidth*6, tileHeight*2, tileWidth, tileHeight);
    }

}