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

        public Enemy(float posX, float posY, float width, float height, float intersectTolerance) {
            super(posX, posY, width, height);

            setHurtbox(new Hitbox(
                    posX+intersectTolerance,
                    posY+intersectTolerance,
                    width-2*intersectTolerance,
                    height-2*intersectTolerance,
                    this));
            getHurtbox().setLeftOffsetX(intersectTolerance);
            getHurtbox().setLeftOffsetY(intersectTolerance);
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
        public int getHealth() {
            return health;
        }





        @Override
        protected void updateTimers(float delta) {
            super.updateTimers(delta);
            if (appearDamagedTimer!=null) {
                appearDamagedTimer-=delta;
                if (appearDamagedTimer<0) appearDamagedTimer=null;
            }
        }

        @Override
        public void draw(Batch batch) {
            if (appearDamagedTimer!=null) batch.setColor(1,0.8f,0.7f,0.95f);
            if (getTimeUntilRemoval()!=null) batch.setColor(0.1f,0.1f,0.1f,0.5f);
            super.draw(batch);
            batch.setColor(Color.WHITE);
        }

        @Override
        public void draw(Batch batch, Color color) {
            if (getTimeUntilRemoval()!=null) super.draw(batch, new Color(0.1f,0.1f,0.1f,0.5f));
            else super.draw(batch, color);
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