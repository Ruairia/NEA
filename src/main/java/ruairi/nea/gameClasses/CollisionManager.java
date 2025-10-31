package ruairi.nea.gameClasses;

import java.util.ArrayList;

public class CollisionManager {
    public static void handleCollisions(ArrayList<Sprite> spritesToBeChecked, ArrayList<Platform> platformsToBeChecked) {
        for (Sprite sprite : spritesToBeChecked) {
            boolean hasCollided = false;
            for (Platform platform : platformsToBeChecked) {
                if (Sprite.intersect(sprite,platform)){
                    resolveCollision(sprite, platform, sprite.getOldX(), sprite.getOldY());
                    hasCollided=true;
                }
            }
            if (!hasCollided) sprite.setOnGround(false);
        }
    }

    public static void resolveCollision(Sprite sprite, Platform platform, float oldX, float oldY) {
        float platformTop = platform.getPosY() + platform.getHeight();
        float platformRight = platform.getPosX() + platform.getWidth();

        if ((oldY >= platformTop) &&
                (sprite.getPosY() < platformTop)) {
            //Came from above
            if(sprite.getVelocityY()<=0){
                sprite.setPosY(platform.getPosY() + platform.getHeight());
                sprite.setVelocityY(0);
                sprite.setLastOnGround(0);
                sprite.setOnGround(true);
            }
        }

        else if ((oldY+sprite.getHeight() <= platform.getPosY()) &&
                (sprite.getPosY() + sprite.getHeight() > platform.getPosY())) {
            //Came from Below
            sprite.setPosY(platform.getPosY()- sprite.getHeight());
            sprite.setVelocityY(0);
        }

        if ((oldX + sprite.getWidth() <= platform.getPosX()) &&
                (sprite.getPosX() + sprite.getWidth() > platform.getPosX())) {
            // Came from the left
            sprite.setPosX(platform.getPosX() - sprite.getWidth());
            sprite.setVelocityX(0);
        }

        else if ((oldX >= platform.getPosX() + platform.getWidth()) &&
                (sprite.getPosX() < platformRight)) {
            // Came from the right
            sprite.setPosX(platformRight);
            sprite.setVelocityX(0);
        }


    }
}
