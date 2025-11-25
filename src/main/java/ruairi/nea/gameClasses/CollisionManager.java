package ruairi.nea.gameClasses;

import ruairi.nea.gameClasses.Entities.Entity;
import ruairi.nea.gameClasses.Entities.Fireball;
import ruairi.nea.gameClasses.Entities.Platform;

import java.util.ArrayList;

public class CollisionManager {
    public static void handleCollisions(ArrayList<Entity> mobileEntities, ArrayList<Platform> platformsToBeChecked) {
        for (Entity entity : mobileEntities) {
            boolean hasCollided = false;
            for (Platform platform : platformsToBeChecked) {
                if (entity.intersect(platform)){
                    resolveCollision(entity, platform, entity.getOldX(), entity.getOldY());
                    hasCollided=true;
                }
            }
            if (!hasCollided) entity.setOnGround(false);
        }
    }

    public static void resolveCollision(Entity entity, Platform platform, float oldX, float oldY) {
        float platformTop = platform.getPosY() + platform.getHeight();
        float platformRight = platform.getPosX() + platform.getWidth();

        if ((oldY >= platformTop) &&
                (entity.getPosY() < platformTop)) {
            //Came from above
            if(entity.getVelocityY()<=0){
                entity.setPosY(platform.getPosY() + platform.getHeight());
                entity.setVelocityY(0);
                entity.setLastOnGround(0);
                entity.setOnGround(true);
            }
        }

        else if ((oldY+ entity.getHeight() <= platform.getPosY()) &&
                (entity.getPosY() + entity.getHeight() > platform.getPosY())) {
            //Came from Below
            entity.setPosY(platform.getPosY()- entity.getHeight());
            entity.setVelocityY(0);
        }

        else if ((oldX + entity.getWidth() <= platform.getPosX()) &&
                (entity.getPosX() + entity.getWidth() > platform.getPosX())) {
            // Came from the left
            entity.setPosX(platform.getPosX() - entity.getWidth());
            if (entity instanceof Fireball) entity.setVelocityX(-entity.getVelocityX());
            else entity.setVelocityX(0);

        }

        else if ((oldX >= platform.getPosX() + platform.getWidth()) &&
                (entity.getPosX() < platformRight)) {
            // Came from the right
            entity.setPosX(platformRight);
            if (entity instanceof Fireball) entity.setVelocityX(-entity.getVelocityX());
            else entity.setVelocityX(0);
        }


    }
}
