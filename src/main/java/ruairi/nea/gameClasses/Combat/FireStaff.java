package ruairi.nea.gameClasses.Combat;

import com.badlogic.gdx.graphics.Color;
import ruairi.nea.gameClasses.Entities.Entity;
import ruairi.nea.gameClasses.Entities.Hero;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class FireStaff extends Staff {
    public FireStaff(Hero hero) {
        super(Color.ORANGE,hero);
    }


    @Override
    public void attack() {
        if (cooldown>0) return;
        float posX = hero.getPosX() + hero.getWidth() * (hero.getCurrentDirection()== Entity.Direction.RIGHT? 1 : 0);
        float velocityX = 200 * ZOOM * (hero.getCurrentDirection()== Entity.Direction.RIGHT? 1 : -1);
        new Projectile(posX,hero.getPosY() + hero.getHeight()*0.5f,velocityX,0,50);
        cooldown = 0.5f;
    }


}
