    package ruairi.nea.gameClasses.Entities.Enemies;

    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.g2d.Batch;
    import ruairi.nea.gameClasses.Entities.Entity;

    public abstract class Enemy extends Entity {
        protected boolean hasContactDamage = true;
        int contactDamage;
        public float intersectTolerance;



        int health;



        public Enemy(float posX, float posY, float width, float height, float intersectTolerance) {
            super(posX, posY, width, height);
            this.intersectTolerance = intersectTolerance;
            health = 100;
        }




        public int getContactDamage() {
            return contactDamage;
        }

        public void damageEnemy(int amount){
            health-=amount;
        }
        public int getHealth() {
            return health;
        }

        @Override
        public void draw(Batch batch) {
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
    }