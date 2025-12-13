package ruairi.nea.gameClasses.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;

import static ruairi.nea.gameClasses.Entities.Entity.Direction.RIGHT;
import static ruairi.nea.gameClasses.Entities.MovingPlatform.MoveDirection.HORIZONTAL;

public class MovingPlatform extends Platform{
    public static final float SPEED = 100;

    private float lesserBound;
    private float greaterBound;

    public enum MoveDirection {
        VERTICAL, HORIZONTAL
    }

    public final MoveDirection moveDirection;

    public MovingPlatform(float posX, float posY, MoveDirection moveDirection,float lesserBound, float greaterBound, PlatformType type){
        super(posX,posY,type);
        this.lesserBound = lesserBound;
        this.greaterBound = greaterBound;
        this.moveDirection = moveDirection;
        if (moveDirection==HORIZONTAL){
            velocityX = SPEED;
        }
        else velocityY = SPEED;
    }

    @Override
    public void updateVelocity(double delta) {
        if (moveDirection == MoveDirection.VERTICAL){
            if (posY<=lesserBound) velocityY = SPEED;
            else if (posY>=greaterBound) velocityY = -1 * SPEED;
        }
        else if (moveDirection == HORIZONTAL){
            if (posX <= lesserBound) velocityX = SPEED;
            else if (posX >= greaterBound) velocityX = -1 * SPEED;
        }
        super.updateVelocity(delta);
    }

    @Override
    public void draw(Batch batch) {
        Direction direction = getCurrentDirection();
        setCurrentDirection(RIGHT);
        super.draw(batch);
        setCurrentDirection(direction);
    }
}