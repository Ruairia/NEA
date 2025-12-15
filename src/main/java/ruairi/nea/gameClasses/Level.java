package ruairi.nea.gameClasses;

import ruairi.nea.gameClasses.Combat.Projectile;
import ruairi.nea.gameClasses.Entities.*;
import ruairi.nea.gameClasses.Entities.Enemies.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static ruairi.nea.gameClasses.GameScreen.ZOOM;

public class Level {

    public ArrayList<Entity> allEntities;
    public ArrayList<Platform> platforms;
    public ArrayList<Enemy> enemies;
    public ArrayList<Entity> mobileEntities;
    public ArrayList<Projectile> playerProjectiles;
    public ArrayList<Projectile> enemyProjectiles;
    public ArrayList<Checkpoint> checkpoints = new ArrayList<>();
    public ArrayList<Coin> coins = new ArrayList<>();

    private Hero hero;
    public Boss boss;
    public Background background;

    public Level() {
        allEntities = new ArrayList<>();
        platforms = new ArrayList<>();
        enemies = new ArrayList<>();
        mobileEntities = new ArrayList<>();
        playerProjectiles = new ArrayList<>();
        enemyProjectiles = new ArrayList<>();

    }

    public void loadLevel(int level) {

        background = new Background(level);
        hero = new Hero(this);

        try {

            BufferedReader levelReader = getLevelReader(level);

            String line;
            String[] elements;

            while ((line = levelReader.readLine()) != null) {
                elements = line.split(",");
                for (int i = 0; i<elements.length; i++){
                    elements[i]=elements[i].strip();
                }
                switch (elements[0]) {
                    case "SPAWNPOINT" -> setHeroSpawnPoint(elements);
                    case "PLATFORM" -> loadPlatform(elements);
                    case "MOVINGPLATFORM" -> loadMovingPlatform(elements);
                    case "WALL" -> loadWall(elements);
                    case "GROUND" -> loadGround(elements);
                    case "ENEMY" -> loadEnemy(elements);
                    case "CHECKPOINT" -> loadCheckpoint(elements);
                    case "GOAL" -> loadGoal(elements);
                    case "COIN" -> loadCoin(elements);
                    case "PREFAB" -> loadPrefab(elements);
                    default -> {System.out.println(elements[0] + " not a valid entity type"); return;}
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPrefab(String[] constructionElements){loadPrefab(constructionElements,0,0);}

    public void loadPrefab(String[] constructionElements, float offsetX, float offsetY){
        offsetX += Float.parseFloat(constructionElements[1]);
        offsetY += Float.parseFloat(constructionElements[2]);

        int prefabID = Integer.parseInt(constructionElements[3]);

        try {

            BufferedReader levelReader = getLevelReader(prefabID);

            String line;
            String[] elements;

            while ((line = levelReader.readLine()) != null) {
                elements = line.split(",");
                for (int i = 0; i<elements.length; i++){
                    elements[i]=elements[i].strip();
                }
                switch (elements[0]) {
                    case "PLATFORM" -> loadPlatform(elements,offsetX,offsetY);
                    case "MOVINGPLATFORM" -> loadMovingPlatform(elements,offsetX,offsetY);
                    case "WALL" -> loadWall(elements,offsetX,offsetY);
                    case "GROUND" -> loadGround(elements,offsetX,offsetY);
                    case "ENEMY" -> loadEnemy(elements,offsetX,offsetY);
                    case "CHECKPOINT","SPAWNPOINT" -> loadCheckpoint(elements,offsetX,offsetY);
                    case "GOAL" -> loadGoal(elements,offsetX,offsetY);
                    case "COIN" -> loadCoin(elements,offsetX,offsetY);
                    case "PREFAB" -> loadPrefab(elements,offsetX,offsetY);
                    default -> {System.out.println(elements[0] + " not a valid entity type"); return;}
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedReader getLevelReader(int level) throws FileNotFoundException {
        String levelPlatformsFile = switch (level) {
            case 1 -> "assets/level1.csv";
            case 2 -> "assets/level2.csv";
            case 3 -> "assets/level3.csv";
            case 4 -> "assets/level4.csv";
            case 10 -> "assets/bossArena.csv";

            default -> throw new IllegalStateException("Unexpected value: " + level);
        };

        return new BufferedReader(new FileReader(levelPlatformsFile));
    }

    private void loadGoal(String[] elements,float offsetX,float offsetY){
        float posX = Float.parseFloat(elements[1])+offsetX;
        float posY = Float.parseFloat(elements[2])+offsetY;
        Goal goal = new Goal(posX,posY);
        allEntities.add(goal);
        checkpoints.add(goal);
    }
    private void loadGoal(String[] elements){loadGoal(elements,0,0);}

    private void loadCoin(String[] elements){loadCoin(elements,0,0);}
    private void loadCoin(String[] elements, float offsetX, float offsetY){
        float posX = Float.parseFloat(elements[1])+offsetX;
        float posY = Float.parseFloat(elements[2])+offsetY;
        Coin coin = new Coin(posX,posY,10);
        allEntities.add(coin);
        coins.add(coin);
    }

    private void loadCheckpoint(String[] elements,float offsetX,float offsetY){
        float posX = Float.parseFloat(elements[1])+offsetX;
        float posY = Float.parseFloat(elements[2])+offsetY;
        Checkpoint checkpoint = new Checkpoint(posX,posY);
        allEntities.add(checkpoint);
        checkpoints.add(checkpoint);
    }
    private void loadCheckpoint(String[] elements){loadCheckpoint(elements,0,0);}

    private void loadEnemy(String[] elements,float offsetX,float offsetY) {
        float posX = Float.parseFloat(elements[1])+offsetX;
        float posY = Float.parseFloat(elements[2])+offsetY;
        Enemy enemy;
        switch (elements[3]){
            case "FIREBALL" -> enemy = new Fireball(posX,posY,Float.parseFloat(elements[4])+offsetX,Float.parseFloat(elements[5])+offsetX);
            case "FIREMAGE" -> enemy = new FireMage(posX,posY,hero,enemyProjectiles,Float.parseFloat(elements[4])+offsetX,Float.parseFloat(elements[5])+offsetX);
            case "WILLOWISP" -> enemy = new WillOWisp(posX,posY,Float.parseFloat(elements[4])+offsetY,Float.parseFloat(elements[5])+offsetY);
            case "BOSS" -> {
                enemy = new Boss(posX,posY,this);
                boss = (Boss) enemy;
            }
            default -> {System.out.println(elements[3]+" not a valid type of enemy"); return;}
        }
        enemies.add(enemy);
        mobileEntities.add(enemy);
        allEntities.add(enemy);
    }
    private void loadEnemy(String[] elements) {loadEnemy(elements,0,0);}

    public void createExplosion(float posX, float posY, int size, int damage, Explosion.Origin origin){
        Explosion explosion = new Explosion(posX,posY,size,damage,origin);
        enemies.add(explosion);
        allEntities.add(explosion);
    }

    private void setHeroSpawnPoint(String[] elements){
        hero.setSpawnPoint(Float.parseFloat(elements[1]), Float.parseFloat(elements[2]));
        mobileEntities.add(hero);
        allEntities.add(hero);
    }

    private void loadGround(String[] elements,float offsetX,float offsetY) {
        float posX = Float.parseFloat(elements[1])+offsetX;
        float posY = Float.parseFloat(elements[2])+offsetY;
        float endX = Float.parseFloat(elements[3])+offsetX;
        loadPlatformTile(posX,posY, Platform.PlatformType.leftPlatform);
        for (float i = posX+Platform.tileWidth*ZOOM; i < endX; i+=Platform.tileWidth*ZOOM) {
            loadPlatformTile(i,posY, Platform.PlatformType.midPlatform);
        }
        loadPlatformTile(endX,posY, Platform.PlatformType.rightPlatform);
    }
    private void loadGround(String[] elements) {loadGround(elements,0,0);}

    private void loadMovingPlatformTile(float posX, float posY, MovingPlatform.MoveDirection moveDirection, float lesserBound, float greaterBound, Platform.PlatformType type){
        MovingPlatform movingPlatform = new MovingPlatform(posX,posY,moveDirection,lesserBound,greaterBound,type);
        platforms.add(movingPlatform);
        allEntities.add(movingPlatform);
    }

    private void createMovingPlatform(float posX, float posY, int tilesWide, MovingPlatform.MoveDirection moveDirection, float lesserBound, float greaterBound){
        if (tilesWide == 1) {
            loadMovingPlatformTile(posX, posY, moveDirection, lesserBound, greaterBound, Platform.PlatformType.singlePlatform);
            return;
        }

        for (int i = 0; i < tilesWide; i++) {
            float xOffset = Platform.tileWidth * ZOOM * i;
            float tileX = posX + xOffset;

            float tileLesserBound, tileGreaterBound;
            if (moveDirection == MovingPlatform.MoveDirection.VERTICAL) {
                tileLesserBound = lesserBound;
                tileGreaterBound = greaterBound;
            } else {
                tileLesserBound = lesserBound + xOffset;
                tileGreaterBound = greaterBound + xOffset;
            }

            Platform.PlatformType type;
            if (i == 0) {
                type = Platform.PlatformType.leftPlatform;
            } else if (i == tilesWide - 1) {
                type = Platform.PlatformType.rightPlatform;
            } else {
                type = Platform.PlatformType.midPlatform;
            }

            loadMovingPlatformTile(tileX, posY, moveDirection, tileLesserBound, tileGreaterBound, type);
        }
    }

    private void loadMovingPlatform(String[] elements,float offsetX,float offsetY) {
        float posX = Float.parseFloat(elements[1])+offsetX;
        float posY = Float.parseFloat(elements[2])+offsetY;
        int tilesWide = Integer.parseInt(elements[3]);

        float lesserBound = Float.parseFloat(elements[5]);
        float greaterBound = Float.parseFloat(elements[6]);

        MovingPlatform.MoveDirection moveDirection;
        if (elements[4].equals("HORIZONTAL")){
            moveDirection = MovingPlatform.MoveDirection.HORIZONTAL;
            lesserBound+=offsetX;
            greaterBound+=offsetX;
        }
        else{
            moveDirection = MovingPlatform.MoveDirection.VERTICAL;
            lesserBound+=offsetY;
            greaterBound+=offsetY;
        }


        createMovingPlatform(posX,posY,tilesWide,moveDirection,lesserBound,greaterBound);
    }
    private void loadMovingPlatform(String[] elements) {loadMovingPlatform(elements,0,0);}

    private void loadWall(String[] elements,float offsetX,float offsetY) {
        float posX = Float.parseFloat(elements[1])+offsetX;
        float posY = Float.parseFloat(elements[2])+offsetY;
        int wallLength = Integer.parseInt(elements[3]);
        createWall(posX,posY,wallLength);
    }
    private void loadWall(String[] elements) {loadWall(elements,0,0);}

    private void createWall(float posX, float posY, int tilesWide) {
        if (tilesWide==1) {
            loadWallTile(posX,posY,Wall.WallType.singleWall);
            return;
        }
        loadWallTile(posX,posY, Wall.WallType.bottomWall);
        for (int i = 1; i < tilesWide-1; i++) {
            loadWallTile(posX,posY+Platform.tileWidth*ZOOM*i, Wall.WallType.middleWall);
        }
        loadWallTile(posX,posY+Platform.tileWidth*ZOOM*(tilesWide-1), Wall.WallType.topWall);
    }

    private void loadWallTile(float posX, float posY, Wall.WallType type){
        Wall wall = new Wall(posX,posY,type);
        platforms.add(wall);
        allEntities.add(wall);
    }

    private void loadPlatform(String[] elements, float offsetX, float offsetY) {
        float posX = Float.parseFloat(elements[1])+offsetX;
        float posY = Float.parseFloat(elements[2])+offsetY;
        int platformLength = Integer.parseInt(elements[3]);
        createPlatform(posX,posY,platformLength);
    }
    private void loadPlatform(String[] elements) {loadPlatform(elements,0,0);}

    private void loadPlatformTile(float posX, float posY, Platform.PlatformType type){
        Platform platform = new Platform(posX,posY,type);
        platforms.add(platform);
        allEntities.add(platform);
    }

    private void createPlatform(float posX, float posY, int tilesWide){
        if (tilesWide==1) {
            loadPlatformTile(posX,posY, Platform.PlatformType.singlePlatform);
            return;
        }
        loadPlatformTile(posX,posY, Platform.PlatformType.leftPlatform);
        for (int i = 1; i < tilesWide-1; i++) {
            loadPlatformTile(posX+Platform.tileWidth*ZOOM*i,posY, Platform.PlatformType.midPlatform);
        }
        loadPlatformTile(posX+Platform.tileWidth*ZOOM*(tilesWide-1),posY, Platform.PlatformType.rightPlatform);
    }


    public Hero getHero() {
        return hero;
    }
}
