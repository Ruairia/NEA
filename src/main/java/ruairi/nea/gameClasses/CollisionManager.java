package ruairi.nea.gameClasses;

import ruairi.nea.gameClasses.Combat.Projectile;
import ruairi.nea.gameClasses.Entities.*;

import java.util.ArrayList;
import java.util.Iterator;

public class CollisionManager {
    public static void handlePlatformCollisions(ArrayList<Entity> mobileEntities, ArrayList<Platform> platformsToBeChecked) {
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
            if (entity instanceof PacingEnemy) entity.setVelocityX(-entity.getVelocityX());
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

    public static void handleEnemyCollisions(ArrayList<Enemy> enemies, Hero hero){
        for (Enemy enemy : enemies){
            if (hero.getInvincibilityPeriodLeft()>0) return;
            if (intersectsWithTolerance(enemy,hero,enemy.intersectTolerance)) {

                hero.damage(enemy.getDamage());
                hero.setInvincibilityPeriodLeft(Hero.INVINCIBILITY_DURATION);
                hero.applyKnockback(enemy);
            }
        }
    }



    public static void checkProjectileEnemyCollisions(Level level, Projectile projectile, ArrayList<Enemy> deadEnemies, Iterator<Projectile> projectileIterator) {
        for (Enemy enemy : level.enemies) {
            if (projectile.intersect(enemy)) {
                enemy.damageEnemy(projectile.damage);

                if (enemy.getHealth() <= 0) {
                    deadEnemies.add(enemy);
                }

                projectileIterator.remove();
                break;
            }
        }
    }

    public static void checkProjectileHeroCollisions(Hero hero, Projectile projectile, Iterator<Projectile> projectileIterator) {
        if (projectile.intersect(hero)) {
            if (hero.getInvincibilityPeriodLeft()==0) {
                hero.damage(projectile.damage);
                hero.setInvincibilityPeriodLeft(Hero.INVINCIBILITY_DURATION);
                hero.applyKnockback(projectile);
            }
            projectileIterator.remove();
        }
    }

    public static boolean handleProjectilePlatformCollisions(Level level, Projectile projectile, Iterator<Projectile> projectileIterator, boolean destroyed) {
        for (Platform platform : level.platforms) {
            if (projectile.intersect(platform)) {
                projectileIterator.remove();
                destroyed = true;
                break;
            }
        }
        return destroyed;
    }

    public static boolean intersectsWithTolerance(Entity a, Entity b, float intersectTolerance){
        return
                a.getPosX() + intersectTolerance < a.getPosX() + a.getWidth()
                        &&
                        a.getPosX() + a.getWidth() - intersectTolerance > b.getPosX()
                        &&
                        a.getPosY() + intersectTolerance < b.getPosY() + b.getHeight()
                        &&
                        a.getPosY() + a.getHeight() - intersectTolerance > b.getPosY();
    }

}
