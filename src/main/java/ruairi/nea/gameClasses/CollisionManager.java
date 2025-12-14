package ruairi.nea.gameClasses;

import ruairi.nea.gameClasses.Combat.Projectile;
import ruairi.nea.gameClasses.Entities.*;
import ruairi.nea.gameClasses.Entities.Enemies.Boss;
import ruairi.nea.gameClasses.Entities.Enemies.Enemy;
import ruairi.nea.gameClasses.Entities.Enemies.PacingEnemy;

import java.util.ArrayList;
import java.util.Iterator;

public class CollisionManager {
    public static void handleEntityPlatformCollisions(ArrayList<Entity> mobileEntities, ArrayList<Platform> platformsToBeChecked) {
        for (Entity entity : mobileEntities) {
            boolean hasCollided = false;
            for (Platform platform : platformsToBeChecked) {
                if (entity.intersect(platform)){
                    resolveEntityPlatformCollision(entity, platform);
                    hasCollided=true;
                }
            }
            if (!hasCollided) entity.setOnGround(false);
        }
    }

    public static void resolveEntityPlatformCollision(Entity entity, Platform platform) {
        if (entity instanceof Platform) return;
        float platformX = platform.getPosX();
        float platformY = platform.getPosY();
        float platformOldX = platform.getOldX();
        float platformOldY = platform.getOldY();
        float platformOldRight = platformOldX + platform.getWidth();
        float platformRight = platform.getPosX() + platform.getWidth();
        float platformOldTop = platformOldY + platform.getHeight();
        float platformTop = platform.getPosY() + platform.getHeight();

        float entityX = entity.getPosX();
        float entityY = entity.getPosY();
        float entityOldX = entity.getOldX();
        float entityOldY = entity.getOldY();
        float entityOldRight = entityOldX + entity.getWidth();
        float entityRight = entity.getPosX() + entity.getWidth();
        float entityOldTop = entityOldY + entity.getHeight();
        float entityTop = entity.getPosY() + entity.getHeight();

        if ((entityOldY >= platformOldTop) &&
                (entityY <= platformTop)) {
            //Came from above
            if(entity.getVelocityY()<=0){
                entity.setPosY(platform.getPosY() + platform.getHeight());

                if (entity instanceof PacingEnemy && ((PacingEnemy) entity).paceDirection== PacingEnemy.PaceDirection.VERTICAL)
                    entity.setVelocityY(-entity.getVelocityY());
                else entity.setVelocityY(platform.getVelocityY());

                entity.setOnGround(true);
                entity.setStoodOnPlatform(platform);
            }
        }
        else if ((entityOldTop <= platformOldY) &&
                (entityTop > platformY)) {
            //Came from Below
            entity.setPosY(platformY- entity.getHeight());
            if (entity instanceof PacingEnemy && ((PacingEnemy) entity).paceDirection== PacingEnemy.PaceDirection.VERTICAL)
                entity.setVelocityY(-entity.getVelocityY());
            else entity.setVelocityY(platform.getVelocityY());
        }

        else if ((entityOldRight <= platformOldX) &&
                (entityRight > platformX)) {
            // Came from the left
            entity.setPosX(platformX - entity.getWidth());
            if (entity instanceof PacingEnemy && ((PacingEnemy) entity).paceDirection== PacingEnemy.PaceDirection.HORIZONTAL) entity.setVelocityX(-entity.getVelocityX());
            else entity.setVelocityX(platform.getVelocityX());

        }

        else if ((entityOldX >= platformOldRight) &&
                (entityX < platformRight)) {
            // Came from the right
            entity.setPosX(platformRight);
            if (entity instanceof PacingEnemy && ((PacingEnemy) entity).paceDirection== PacingEnemy.PaceDirection.HORIZONTAL) entity.setVelocityX(-entity.getVelocityX());
            else entity.setVelocityX(platform.getVelocityX());
        }

        else entity.setPosY(platformTop);
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
            if (intersectsWithTolerance(enemy,projectile,projectile.intersectTolerance)) {
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
        if (intersectsWithTolerance(hero,projectile,projectile.intersectTolerance)) {
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
                a.getPosX() + intersectTolerance < b.getPosX() + b.getWidth()
                        &&
                        a.getPosX() + a.getWidth() - intersectTolerance > b.getPosX()
                        &&
                        a.getPosY() + intersectTolerance < b.getPosY() + b.getHeight()
                        &&
                        a.getPosY() + a.getHeight() - intersectTolerance > b.getPosY();
    }

}
