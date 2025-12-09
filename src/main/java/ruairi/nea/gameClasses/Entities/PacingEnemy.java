package ruairi.nea.gameClasses.Entities;

public abstract class PacingEnemy extends Enemy {

    protected float leftBound;
    protected float rightBound;
    protected float speed;

    public PacingEnemy(float posX, float posY, float width, float height,
                       float leftBound, float rightBound, float speed, float intersectTolerance) {
        super(posX, posY, width, height, intersectTolerance);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.velocityX = speed;
        this.speed = speed;
    }

    @Override
    public void updateVelocity(double delta) {
        if (posX <= leftBound) {
            velocityX = speed;
        } else if (posX >= rightBound) {
            velocityX = -speed;
        }
        super.updateVelocity(delta);
    }
}
