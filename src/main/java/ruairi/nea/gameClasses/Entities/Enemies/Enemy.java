    package ruairi.nea.gameClasses.Entities.Enemies;

    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.g2d.Batch;
    import ruairi.nea.gameClasses.Entities.Entity;
    import ruairi.nea.gameClasses.Hitbox;

    public abstract class Enemy extends Entity {
        protected boolean hasContactDamage = true;
        int contactDamage;
        protected boolean invulnerable = false;


        int health;

        protected Float appearDamagedTimer = null;
        protected float frozenTimer = 0;

        private float velocityXBeforeFrozen;
        private float velocityYBeforeFrozen;

        protected float knockbackTimer = 0;
        protected float knockbackVelocityX = 0;
        protected float knockbackVelocityY = 0;

        public Enemy(float posX, float posY, float width, float height, float intersectTolerance) {
            super(posX, posY, width, height);

            setHurtbox(new Hitbox(
                    posX+intersectTolerance,
                    posY+intersectTolerance,
                    width-2*intersectTolerance,
                    height-2*intersectTolerance,
                    this));
            getHurtbox().setLeftOffsetX(intersectTolerance);
            getHurtbox().setBottomOffsetY(intersectTolerance);
            getHurtbox().setRightOffsetX(intersectTolerance);
            getHurtbox().setTopOffsetY(intersectTolerance);

            health = 100;
        }

        public void kill(float stickAroundTime){
            setTimeUntilRemoval(stickAroundTime);
        }


        public int getContactDamage() {
            return contactDamage;
        }

        public void damageEnemy(int amount){
            health-=amount;
            appearDamaged(0.3f);
        }

        public void applyKnockback(float knockbackX, float knockbackY, float duration) {
            this.knockbackVelocityX = knockbackX;
            this.knockbackVelocityY = knockbackY;
            this.knockbackTimer = duration;
        }

        public boolean isInKnockback() {
            return knockbackTimer > 0;
        }

        public void freeze(float time){
            frozenTimer=time;
            velocityXBeforeFrozen=getVelocityX();
            velocityYBeforeFrozen=getVelocityY();
            setVelocityX(0);
            setVelocityY(0);
        }

        public void unfreeze(){
            setVelocityX(velocityXBeforeFrozen);
            setVelocityY(velocityYBeforeFrozen);
        }



        @Override
        protected void updateTimers(float delta) {
            super.updateTimers(delta);
            if (appearDamagedTimer!=null) {
                appearDamagedTimer-=delta;
                if (appearDamagedTimer<0) appearDamagedTimer=null;
            }
            if (frozenTimer>0) {
                frozenTimer-=delta;
                if (frozenTimer<=0) {
                    frozenTimer=0;
                    unfreeze();
                }
            }
            if (knockbackTimer > 0) {
                knockbackTimer -= delta;
                if (knockbackTimer <= 0) {
                    knockbackTimer = 0;
                    // Knockback ended - subclasses can resume normal behaviour
                }
            }
        }

        @Override
        protected void updateVelocity(double delta) {
            if (frozenTimer<=0) super.updateVelocity(delta);
        }

        @Override
        public void updatePosition(double delta) {
            // During knockback, use knockback velocity instead of normal velocity
            if (isInKnockback()) {
                posX = (float) (oldX + knockbackVelocityX * delta);
                posY = (float) (oldY + knockbackVelocityY * delta);

                // Apply decay to knockback velocity
                knockbackVelocityX *= 0.92f;
                knockbackVelocityY *= 0.92f;
            } else {
                // Normal position update
                super.updatePosition(delta);
            } }

        @Override
        public void draw(Batch batch) {
            if (appearDamagedTimer!=null) batch.setColor(1,0.8f,0.7f,0.95f);
            if (frozenTimer>0) batch.setColor(0.5f,0.8f,1f,0.95f);
            if (getTimeUntilRemoval()!=null) batch.setColor(0.1f,0.1f,0.1f,0.5f);
            super.draw(batch);
            batch.setColor(Color.WHITE);
        }

        @Override
        public void draw(Batch batch, Color colour) {
            if (appearDamagedTimer!=null) colour = new Color(1,0.8f,0.7f,0.95f);
            if (frozenTimer>0) colour = new Color(0.5f,0.8f,1f,0.95f);
            if (getTimeUntilRemoval()!=null) super.draw(batch, new Color(0.1f,0.1f,0.1f,0.5f));
            else super.draw(batch, colour);
        }

        public int getHealth() {
            return health;
        }

        public boolean hasContactDamage() {
            return hasContactDamage;
        }

        public void appearDamaged(float time){
            appearDamagedTimer=time;
        }
        public Float getAppearDamagedTimer() {
            return appearDamagedTimer;
        }
    }