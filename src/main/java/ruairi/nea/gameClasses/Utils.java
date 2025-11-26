package ruairi.nea.gameClasses;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Utils {

    public static Animation<TextureRegion> createAnimation(TextureRegion[] frames, float frameDuration, Animation.PlayMode playMode) {
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(playMode);
        return animation;
    }
}
