    package ruairi.nea.gameClasses;

    public abstract class Enemy extends Sprite {
        public Enemy(float posX, float posY, float velocityX, float velocityY, float width, float height) {
            super(posX, posY, width, height);
        }


        public void goToPoint(float moveSpeed, float pointX, float pointY){
            float deltaX = pointX - posX;
            float deltaY = pointY - posY;
            float deltaDist = (float) Math.sqrt(deltaX*deltaX + deltaY*deltaY);
            velocityX = moveSpeed*deltaX/deltaDist;
            velocityY = moveSpeed*deltaY/deltaDist;
        }
        public void goToPoint(float moveSpeed, float pointX){
            goToPoint(moveSpeed, pointX, posY);
        }

    }
