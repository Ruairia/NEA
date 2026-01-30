package ruairi.nea.applicationClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button {
    static Texture mainMenuSpriteSheet = new Texture("assets/MainMenuButtonSpriteSheet.png");
    static Texture levelSelectSpriteSheet = new Texture("assets/LevelSelectButtonSpriteSheet.png");
    public static TextureRegion mainMenuRegularTexture;
    public static TextureRegion mainMenuHoverTexture;
    public static TextureRegion levelSelectRegularTexture;
    public static TextureRegion levelSelectHoverTexture;

    public String text;
    private GlyphLayout layout = new GlyphLayout();
 
    public boolean isHovered = false;
    public boolean isSelected = false;

    public Button leftButton = null;
    public Button rightButton = null;
    public Button upButton = null;
    public Button downButton = null;

    public enum ButtonType {
        MainMenu,
        LevelSelect
    }
    public ButtonType type;

    public static void loadTextures() {
        if (mainMenuRegularTexture != null && mainMenuHoverTexture != null) return;
        mainMenuRegularTexture = new TextureRegion(mainMenuSpriteSheet, 0, 0, 48, 16);
        mainMenuHoverTexture = new TextureRegion(mainMenuSpriteSheet, 0, 16, 48, 16);
        levelSelectRegularTexture = new TextureRegion(levelSelectSpriteSheet, 0, 0, 16, 16);
        levelSelectHoverTexture = new TextureRegion(levelSelectSpriteSheet, 0, 16, 16, 16);
    }

    public void draw(Batch batch, BitmapFont font) {
        batch.draw(getTexture(), posX, posY, width, height);
        if (text != null && font != null) {
            layout.setText(font, text);
            float textX = posX + (width - layout.width) / 2;
            float textY = posY + (height + layout.height) / 2;
            font.draw(batch, text, textX, textY);
        }
    }

    float posX;
    float posY;
    float width;
    float height;

    public Button(float posX, float posY, float width, float height, ButtonType type) {
        loadTextures();
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public TextureRegion getTexture(){
        if (type == ButtonType.MainMenu)
           return isHovered||isSelected ? mainMenuHoverTexture : mainMenuRegularTexture;
        if (type == ButtonType.LevelSelect)
            return isHovered||isSelected ? levelSelectHoverTexture : levelSelectRegularTexture;
        return null;
    }

    public boolean isUnderMouse(float mouseX, float mouseY) {
        return mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height;
    }
}
