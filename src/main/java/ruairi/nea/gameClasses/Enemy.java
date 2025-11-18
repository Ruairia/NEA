    package ruairi.nea.gameClasses;

    public abstract class Enemy extends Entity {
        int damage;
        float intersectTolerance;



        public Enemy(float posX, float posY, float velocityX, float velocityY, float width, float height) {
            super(posX, posY, width, height);
            intersectTolerance = 0;
        }


        public  boolean intersectsHero(Entity hero){
            return
                    this.posX + intersectTolerance < hero.posX + hero.width
                            &&
                            this.posX + this.width - intersectTolerance > hero.posX
                            &&
                            this.posY + intersectTolerance < hero.posY + hero.height
                            &&
                            this.posY + this.height - intersectTolerance > hero.posY;
        }
    }