package ruairi.nea.gameClasses.Combat;

import com.badlogic.gdx.graphics.Color;
import ruairi.nea.gameClasses.Entities.Entity;
import ruairi.nea.gameClasses.Entities.Hero;
import ruairi.nea.gameClasses.Entities.Projectile;
import ruairi.nea.gameClasses.Level.Level;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class FireStaff extends Staff {

    public static final int MANA_COST = 40;

    Level level;

    public FireStaff(Hero hero, Level level) {
        super(hero,true);
        this.projectiles = level.projectiles;
        this.manaCost=MANA_COST;
        colour=Color.ORANGE;
        this.level = level;
    }

    ArrayList<Projectile> projectiles;

    @Override
    public void attack() {
        if (cooldown>0) return;

        currentAnimation=animations.get(Hero.HeroState.ATTACKING);

        float posX = hero.getPosX() + hero.getWidth() * (hero.getCurrentDirection()== Entity.Direction.RIGHT? 1 : 0);
        float velocityX = 200 * ZOOM * (hero.getCurrentDirection()== Entity.Direction.RIGHT? 1 : -1);
        projectiles.add(new Projectile(
                posX,hero.getPosY() + hero.getHeight()*0.5f,
                velocityX,0,
                50, level, Projectile.projectileType.FIRE_STAFF, Projectile.Origin.PLAYER));

        cooldown = 0.4f;
    }

    @Override
    public void attackDownwards() {
        if (cooldown>0) return;

        currentAnimation=animations.get(Hero.HeroState.ATTACKING_DOWNWARDS);

        float posX = hero.getPosX() + hero.getWidth() * (hero.getCurrentDirection()== Entity.Direction.RIGHT? 1 : 0);
        float velocityX = 100 * ZOOM * hero.getPlayerDirection();
        float velocityY = -100 * ZOOM;
        projectiles.add(new Projectile(
                posX,hero.getPosY(),
                velocityX,velocityY,
                50, level, Projectile.projectileType.FIRE_STAFF, Projectile.Origin.PLAYER));

        cooldown = 0.4f;
    }


}
