package ruairi.nea.gameClasses.Combat;

import com.badlogic.gdx.graphics.Color;
import ruairi.nea.gameClasses.Entities.Entity;
import ruairi.nea.gameClasses.Entities.Hero;
import ruairi.nea.gameClasses.Entities.Projectile;
import ruairi.nea.gameClasses.Level;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class IceStaff extends Staff {

    public static final int MANA_COST = 90;

    Level level;

    public IceStaff(Hero hero, Level level) {
        super(hero,true);
        this.projectiles = level.projectiles;
        this.manaCost=MANA_COST;
        colour=Color.CYAN;
        this.level = level;
    }

    ArrayList<Projectile> projectiles;

    @Override
    public void attack() {
        if (cooldown>0) return;
        float posX = hero.getPosX() + hero.getWidth() * (hero.getCurrentDirection()== Entity.Direction.RIGHT? 1 : 0);
        float velocityX = 200 * ZOOM * (hero.getCurrentDirection()== Entity.Direction.RIGHT? 1 : -1);
        projectiles.add(new Projectile(
                posX,hero.getPosY() + hero.getHeight()*0.5f,
                velocityX,0,
                25, level, Projectile.projectileType.ICE_STAFF, Projectile.Origin.PLAYER));

        cooldown = 0.4f;
    }

    @Override
    public void attackDownwards() {
        if (cooldown>0) return;
        float posX = hero.getPosX() + hero.getWidth() * (hero.getCurrentDirection()== Entity.Direction.RIGHT? 1 : 0);
        float velocityX = 100 * ZOOM * hero.getPlayerDirection();
        float velocityY = -100 * ZOOM;
        projectiles.add(new Projectile(
                posX,hero.getPosY(),
                velocityX,velocityY,
                50, level, Projectile.projectileType.ICE_STAFF, Projectile.Origin.PLAYER));

        cooldown = 0.4f;
    }


}
