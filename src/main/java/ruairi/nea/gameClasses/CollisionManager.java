package ruairi.nea.gameClasses;

import ruairi.nea.gameClasses.Entities.Projectile;
import ruairi.nea.gameClasses.Entities.*;
import ruairi.nea.gameClasses.Entities.Enemies.*;

import java.util.ArrayList;

public class CollisionManager {
    public static void handleEntityPlatformCollisions(ArrayList<Entity> mobileEntities, ArrayList<Platform> platformsToBeChecked) {
        for (Entity entity : mobileEntities) {
            boolean hasCollided = false;
            for (Platform platform : platformsToBeChecked) {
                if (entity.getCollisionBox().intersects(platform.getCollisionBox())){
                    resolveEntityPlatformCollision(entity, platform);
                    hasCollided=true;
                }
            }
            if (entity.isOnGround()) entity.setVelocityY(0);
            if (!hasCollided) entity.setOnGround(false);
        }
    }

    public static void resolveEntityPlatformCollision(Entity entity, Platform platform) {
        if (entity instanceof Platform) return;

        Hitbox platformCollisionBox = platform.getCollisionBox();
        Hitbox entityCollisionBox = entity.getCollisionBox();

        float platformX = platformCollisionBox.getPosX();
        float platformY = platformCollisionBox.getPosY();
        float platformOldX = platformCollisionBox.getOldX();
        float platformOldY = platformCollisionBox.getOldY();
        float platformOldRight = platformOldX + platformCollisionBox.getWidth();
        float platformRight = platformCollisionBox.getPosX() + platformCollisionBox.getWidth();
        float platformOldTop = platformOldY + platformCollisionBox.getHeight();
        float platformTop = platformCollisionBox.getPosY() + platformCollisionBox.getHeight();

        float entityX = entityCollisionBox.getPosX();
        float entityY = entityCollisionBox.getPosY();
        float entityOldX = entityCollisionBox.getOldX();
        float entityOldY = entityCollisionBox.getOldY();
        float entityOldRight = entityOldX + entityCollisionBox.getWidth();
        float entityRight = entityCollisionBox.getPosX() + entityCollisionBox.getWidth();
        float entityOldTop = entityOldY + entityCollisionBox.getHeight();
        float entityTop = entityCollisionBox.getPosY() + entityCollisionBox.getHeight();



        if ((entityOldY >= platformOldTop) &&
                (entityY <= platformTop)) {
            if (platform instanceof Wall && ((Wall) platform).type!= Wall.WallType.topWall) return;
            //Came from above
            if(entity.getVelocityY()<=0){
                entity.setPosY(platformTop - entityCollisionBox.getBottomOffsetY());


                if (entity instanceof PacingEnemy && ((PacingEnemy) entity).paceDirection== PacingEnemy.PaceDirection.VERTICAL)
                    entity.setVelocityY(-entity.getVelocityY());


                entity.setPosX(entity.getPosX()+platformX-platformOldX);

                entity.setOnGround(true);
                entity.setStoodOnPlatform(platform);
            }
        }
        else if ((entityOldTop <= platformOldY) &&
                (entityTop > platformY)) {
            //Came from Below
            entity.setPosY(platformY - entityCollisionBox.getBottomOffsetY() - entityCollisionBox.getHeight());
            if (entity instanceof PacingEnemy && ((PacingEnemy) entity).paceDirection== PacingEnemy.PaceDirection.VERTICAL)
                entity.setVelocityY(-entity.getVelocityY());
        }

        else if ((entityOldRight <= platformOldX) &&
                (entityRight > platformX)) {
            // Came from the left
            entity.setPosX(platformX - entityCollisionBox.getLeftOffsetX() - entityCollisionBox.getWidth());
            if (entity instanceof PacingEnemy && ((PacingEnemy) entity).paceDirection== PacingEnemy.PaceDirection.HORIZONTAL) entity.setVelocityX(-entity.getVelocityX());
            entity.setOnGround(false);

        }

        else if ((entityOldX >= platformOldRight) &&
                (entityX < platformRight)) {
            // Came from the right
            entity.setPosX(platformRight - entityCollisionBox.getLeftOffsetX());
            if (entity instanceof PacingEnemy && ((PacingEnemy) entity).paceDirection== PacingEnemy.PaceDirection.HORIZONTAL) entity.setVelocityX(-entity.getVelocityX());
            entity.setOnGround(false);
        }

        else entity.setPosY(platformTop);
        entity.updateHitbox();

    }



    public static void handleEnemyHeroCollisions(ArrayList<Enemy> enemies, Hero hero){
        for (Enemy enemy : enemies){
            if (enemy.getTimeUntilRemoval()!=null) continue;
            if (!enemy.hasContactDamage()) return;
            if (hero.getInvincibilityPeriodLeft()>0) return;

            if (enemy.getHurtbox().intersects(hero.getHitbox())) {
                hero.damage(enemy.getContactDamage());
                hero.setInvincibilityPeriodLeft(Hero.INVINCIBILITY_DURATION);
                hero.applyKnockback(enemy);
                if (enemy instanceof Boss) {
                    BossAI.rewardMoveTransition(((Boss) enemy).getPreviousState(),((Boss) enemy).getCurrentState(), 0.1f);
                    BossAI.rewardMoveEverywhere(((Boss) enemy).getCurrentState(), 0.1f);
                }
                else if (enemy instanceof Explosion && ((Explosion)enemy).getOrigin()== Explosion.Origin.BOSS){
                    BossAI.rewardMoveEverywhere(BossAI.BossState.SHOOT_EXPLOSIVE,0.2f);
                    enemy.kill(1.6f-((Explosion) enemy).lifetime);
                }
            }
        }
    }



    public static void checkProjectileEnemyCollisions(Level level, Projectile projectile) {
        for (Enemy enemy : level.enemies) {
            if (enemy instanceof Explosion) continue;
            if (projectile.getTimeUntilRemoval()!=null) continue;
            if (enemy.getHitbox().intersects(projectile.getHurtbox())) {

                enemy.damageEnemy(projectile.damage);

                if (enemy instanceof Boss) {
                    BossAI.punishMoveTransition(((Boss) enemy).getPreviousState(),((Boss) enemy).getCurrentState(), 0.02f);
                    BossAI.punishMoveEverywhere(((Boss) enemy).getCurrentState(), 0.02f);
                }

                if (enemy.getHealth() <= 0 && enemy.getTimeUntilRemoval()==null) {
                    enemy.kill(0.2f);
                }

                if (projectile.getTimeUntilRemoval()==null) projectile.kill(0.1f);

                break;
            }
        }
    }

    public static void checkProjectileHeroCollisions(Hero hero, Projectile projectile) {
        if ((hero.getHitbox().intersects(projectile.getHurtbox()))&&projectile.getTimeUntilRemoval()==null) {
            if (hero.getInvincibilityPeriodLeft()==0) {
                hero.damage(projectile.damage);
                hero.setInvincibilityPeriodLeft(Hero.INVINCIBILITY_DURATION);
                hero.applyKnockback(projectile);
                if (projectile.type == Projectile.projectileType.BOSS) BossAI.rewardMoveEverywhere(BossAI.BossState.SHOOT, 0.1f);
                if (projectile.type == Projectile.projectileType.BOSS_EXPLOSIVE) BossAI.rewardMoveEverywhere(BossAI.BossState.SHOOT_EXPLOSIVE,0.1f);
            }
            if (projectile.getTimeUntilRemoval()==null) projectile.kill(0.1f);
        }
    }

    public static boolean handleProjectilePlatformCollisionsAndCheckIfDestroyed(Level level, Projectile projectile) {
        for (Platform platform : level.platforms) {
            if (projectile.getCollisionBox().intersects(platform.getCollisionBox())) {
                if (projectile.getTimeUntilRemoval()==null) projectile.kill(0.1f);
                return true;
            }
        }
        return false;
    }


}
