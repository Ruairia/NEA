package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Utils {

    public static Animation<TextureRegion> createAnimation(TextureRegion[] frames, float frameDuration, Animation.PlayMode playMode) {
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(playMode);
        return animation;
    }

    public static TextureRegion[] parseFrames( int x, int y, int frameWidth, int frameHeight, Texture spriteSheet, int framesNumber) {
        TextureRegion[] frames = new TextureRegion[framesNumber];
        for (int i = 0; i < framesNumber; i++) {
            frames[i] = new TextureRegion(spriteSheet, x + i * frameWidth, y, frameWidth, frameHeight);
        }
        return frames;
    }
}
