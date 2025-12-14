    package ruairi.nea.gameClasses.Entities.Enemies;

    import ruairi.nea.gameClasses.Entities.Entity;

    public abstract class Enemy extends Entity {
        int contactDamage;
        public final float intersectTolerance;



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
    }