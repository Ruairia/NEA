package ruairi.nea.gameClasses.Staffs;

import com.badlogic.gdx.graphics.Color;
import ruairi.nea.gameClasses.Entities.Hero;

public class FireStaff extends Staff {
    public FireStaff(Hero hero) {
        super(Color.ORANGE,hero);
    }

    @Override
    public void attack() {
        System.out.println("hi");
    }
}
