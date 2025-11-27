package ruairi.nea.gameClasses.Combat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ruairi.nea.gameClasses.Entities.Entity;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Projectile extends Entity {
    public int damage;
    public static Texture spriteSheet;
    public static final String SPRITESHEET_PATH = "assets/ProjectileSpriteSheet.png";


    public Projectile(float posX, float posY, float velocityX, float velocityY, int damage){
        super(posX,posY,8*ZOOM,8*ZOOM);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.damage = damage;
        loadTextures();
        setTextureRegion(new TextureRegion(spriteSheet, 0, 0, 8,8));
        setAffectedByGravity(false);
    }

    private static void loadTextures(){
        if (spriteSheet != null) return;
        spriteSheet = new Texture(SPRITESHEET_PATH);

    }


    public static void disposeTextures(){
        spriteSheet.dispose();
    }
}
