package ruairi.nea.gameClasses.Combat;

import com.badlogic.gdx.graphics.Color;
import ruairi.nea.gameClasses.Entities.Entity;
import ruairi.nea.gameClasses.Entities.Hero;

import java.util.ArrayList;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class FireStaff extends Staff {



    public FireStaff(Hero hero,ArrayList<Projectile> playerProjectiles) {
        super(Color.ORANGE,hero, false);
        this.playerProjectiles = playerProjectiles;
        this.requiresMana=true;
        this.manaCost=70;
    }

    ArrayList<Projectile> playerProjectiles;

    @Override
    public void attack() {
        if (cooldown>0) return;
        float posX = hero.getPosX() + hero.getWidth() * (hero.getCurrentDirection()== Entity.Direction.RIGHT? 1 : 0);
        float velocityX = 200 * ZOOM * (hero.getCurrentDirection()== Entity.Direction.RIGHT? 1 : -1);
        playerProjectiles.add(new Projectile(posX,hero.getPosY() + hero.getHeight()*0.5f,velocityX,0,50, Projectile.projectileType.FIRE_STAFF));
        cooldown = 0.4f;
    }


}
