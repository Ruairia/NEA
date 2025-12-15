package ruairi.nea.gameClasses;

import ruairi.nea.gameClasses.Combat.Projectile;
import ruairi.nea.gameClasses.Entities.*;
import ruairi.nea.gameClasses.Entities.Enemies.*;

import java.util.ArrayList;

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



    public static void handleEnemyHeroCollisions(ArrayList<Enemy> enemies, Hero hero){
        for (Enemy enemy : enemies){
            if (enemy.getTimeUntilRemoval()!=null) continue;

            if (hero.getInvincibilityPeriodLeft()>0) return;
            if (intersectsWithTolerance(enemy,hero,enemy.intersectTolerance)) {

                hero.damage(enemy.getContactDamage());
                hero.setInvincibilityPeriodLeft(Hero.INVINCIBILITY_DURATION);
                hero.applyKnockback(enemy);
                if (enemy instanceof Boss) {
                    BossAI.rewardMoveTransition(((Boss) enemy).getPreviousState(),((Boss) enemy).getCurrentState(), 0.1f);
                    BossAI.rewardMoveEverywhere(((Boss) enemy).getCurrentState(), 0.1f);
                }
                else if (enemy instanceof Explosion && ((Explosion)enemy).getOrigin()== Explosion.Origin.BOSS) BossAI.rewardMoveEverywhere(BossAI.BossState.SHOOT_EXPLOSIVE,0.2f);
            }
        }
    }



    public static void checkProjectileEnemyCollisions(Level level, Projectile projectile) {
        for (Enemy enemy : level.enemies) {
            if (enemy instanceof Explosion) continue;
            if (projectile.getTimeUntilRemoval()!=null) continue;
            if (intersectsWithTolerance(enemy,projectile,projectile.intersectTolerance)) {

                enemy.damageEnemy(projectile.damage);

                if (enemy instanceof Boss) {
                    BossAI.punishMoveTransition(((Boss) enemy).getPreviousState(),((Boss) enemy).getCurrentState(), 0.1f);
                    BossAI.punishMoveEverywhere(((Boss) enemy).getCurrentState(), 0.1f);
                }

                if (enemy.getHealth() <= 0 && enemy.getTimeUntilRemoval()==null) {
                    enemy.setTimeUntilRemoval(0.2f);
                }

                if (projectile.getTimeUntilRemoval()==null) projectile.setTimeUntilRemoval(0.1f);

                break;
            }
        }
    }

    public static void checkProjectileHeroCollisions(Hero hero, Projectile projectile) {
        if (intersectsWithTolerance(hero,projectile,projectile.intersectTolerance)&&projectile.getTimeUntilRemoval()==null) {
            if (hero.getInvincibilityPeriodLeft()==0) {
                hero.damage(projectile.damage);
                hero.setInvincibilityPeriodLeft(Hero.INVINCIBILITY_DURATION);
                hero.applyKnockback(projectile);
                if (projectile.type == Projectile.projectileType.BOSS) BossAI.rewardMoveEverywhere(BossAI.BossState.SHOOT, 0.1f);
                if (projectile.type == Projectile.projectileType.BOSS_EXPLOSIVE) BossAI.rewardMoveEverywhere(BossAI.BossState.SHOOT_EXPLOSIVE,0.1f);
            }
            if (projectile.getTimeUntilRemoval()==null) projectile.setTimeUntilRemoval(0.1f);
        }
    }

    public static boolean handleProjectilePlatformCollisions(Level level, Projectile projectile) {
        for (Platform platform : level.platforms) {
            if (projectile.intersect(platform)) {
                if (projectile.getTimeUntilRemoval()==null) projectile.setTimeUntilRemoval(0.1f);
                return true;
            }
        }
        return false;
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
