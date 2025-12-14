    package ruairi.nea.gameClasses.Entities.Enemies;

    import ruairi.nea.gameClasses.Entities.Entity;

    public abstract class Enemy extends Entity {
        int damage;
        public final float intersectTolerance;



        int health;



        public Enemy(float posX, float posY, float width, float height, float intersectTolerance) {
            super(posX, posY, width, height);
            this.intersectTolerance = intersectTolerance;
            health = 100;
        }




        public int getDamage() {
            return damage;
        }

        public void damageEnemy(int amount){
            health-=amount;
        }
        public int getHealth() {
            return health;
        }
    }