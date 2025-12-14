package ruairi.nea.gameClasses.Entities.Enemies;

import static ruairi.nea.gameClasses.Entities.Enemies.BossAI.*;
import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Boss extends Enemy{
    public static final float WIDTH = 16;
    public static final float HEIGHT = 16;

    private BossAI.BossState previousState = BossAI.BossState.IDLE;
    private BossAI.BossState currentState = BossAI.BossState.IDLE;

    public Boss(float posX, float posY){
        super(posX,posY,WIDTH*ZOOM,HEIGHT*ZOOM,10);
        loadAllWeights();
    }


}
