package ruairi.nea.gameClasses.Entities.Enemies;

public abstract class PacingEnemy extends Enemy {

    protected float lesserBound;
    protected float greaterBound;
    protected float speed;
    public final PaceDirection paceDirection;

    public enum PaceDirection {
        VERTICAL,
        HORIZONTAL
    }

    public PacingEnemy(float posX, float posY, float width, float height,
                       float lesserBound, float greaterBound, float speed, float intersectTolerance, PaceDirection paceDirection) {
        super(posX, posY, width, height, intersectTolerance);

        this.lesserBound = lesserBound;
        this.greaterBound = greaterBound;

        if (paceDirection == PaceDirection.VERTICAL) this.velocityY = speed;
        else this.velocityX = speed;
        this.speed = speed;

        this.paceDirection = paceDirection;
    }

    @Override
    public void updateVelocity(double delta) {
        if (paceDirection == PaceDirection.VERTICAL){
            if (posY<=lesserBound) velocityY = speed;
            else if (posY>=greaterBound) velocityY = -1 * speed;
        }
        else if (paceDirection == PaceDirection.HORIZONTAL){
            if (posX <= lesserBound) velocityX = speed;
            else if (posX >= greaterBound) velocityX = -1 * speed;
        }
        super.updateVelocity(delta);
    }

}
