package ruairi.nea.gameClasses;

import ruairi.nea.gameClasses.Combat.Projectile;
import ruairi.nea.gameClasses.Entities.*;

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

    private Hero hero;
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
                    case "SPAWNPOINT" -> loadHero(elements);
                    case "PLATFORM" -> loadPlatform(elements);
                    case "MOVINGPLATFORM" -> loadMovingPlatform(elements);
                    case "WALL" -> loadWall(elements);
                    case "GROUND" -> loadGround(elements);
                    case "ENEMY" -> loadEnemy(elements);
                    case "CHECKPOINT" -> loadCheckpoint(elements);
                    case "GOAL" -> loadGoal(elements);
                    default -> {System.out.println(elements[0] + " not a valid entity type"); return;}
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedReader getLevelReader(int level) throws FileNotFoundException {
        String levelPlatformsFile = switch (level) {
            case 1 -> "assets/level1Platforms.csv";
            case 2 -> "assets/level2Platforms.csv";
            case 3 -> "assets/level3Platforms.csv";
            case 4 -> "assets/level4Platforms.csv";
            case 5 -> "assets/level5Platforms.csv";

            default -> throw new IllegalStateException("Unexpected value: " + level);
        };

        return new BufferedReader(new FileReader(levelPlatformsFile));
    }

    private void loadGoal(String[] elements){
        float posX = Float.parseFloat(elements[1]);
        float posY = Float.parseFloat(elements[2]);
        Goal goal = new Goal(posX,posY);
        allEntities.add(goal);
        checkpoints.add(goal);
    }

    private void loadCheckpoint(String[] elements){
        float posX = Float.parseFloat(elements[1]);
        float posY = Float.parseFloat(elements[2]);
        Checkpoint checkpoint = new Checkpoint(posX,posY);
        allEntities.add(checkpoint);
        checkpoints.add(checkpoint);
    }

    private void loadEnemy(String[] elements){
        float posX = Float.parseFloat(elements[1]);
        float posY = Float.parseFloat(elements[2]);
        Enemy enemy;
        switch (elements[3]){
            case "FIREBALL" -> enemy = new Fireball(posX,posY,Float.parseFloat(elements[4]),Float.parseFloat(elements[5]));
            case "FIREMAGE" -> enemy = new FireMage(posX,posY,hero,enemyProjectiles,Float.parseFloat(elements[4]),Float.parseFloat(elements[5]));
            case "WILLOWISP" -> enemy = new WillOWisp(posX,posY,Float.parseFloat(elements[4]),Float.parseFloat(elements[5]));
            default -> {System.out.println(elements[3]+" not a valid type of enemy"); return;}
        }
        enemies.add(enemy);
        mobileEntities.add(enemy);
        allEntities.add(enemy);
    }

    private void loadHero(String[] elements){
        hero = new Hero(this).setSpawnPoint(Float.parseFloat(elements[1]), Float.parseFloat(elements[2]));
        mobileEntities.add(hero);
        allEntities.add(hero);
    }

    private void loadGround(String[] elements){
        float posX = Float.parseFloat(elements[1]);
        float posY = Float.parseFloat(elements[2]);
        float endX = Float.parseFloat(elements[3]);
        loadPlatformTile(posX,posY, Platform.PlatformType.leftPlatform);
        for (float i = posX+Platform.tileWidth*ZOOM; i < endX; i+=Platform.tileWidth*ZOOM) {
            loadPlatformTile(i,posY, Platform.PlatformType.midPlatform);
        }
        loadPlatformTile(endX,posY, Platform.PlatformType.rightPlatform);
    }

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

    private void loadMovingPlatform(String[] elements) {
        float posX = Float.parseFloat(elements[1]);
        float posY = Float.parseFloat(elements[2]);
        int tilesWide = Integer.parseInt(elements[3]);

        MovingPlatform.MoveDirection moveDirection;
        if (elements[4].equals("HORIZONTAL")) moveDirection = MovingPlatform.MoveDirection.HORIZONTAL;
        else moveDirection = MovingPlatform.MoveDirection.VERTICAL;

        float lesserBound = Float.parseFloat(elements[5]);
        float greaterBound = Float.parseFloat(elements[6]);
        createMovingPlatform(posX,posY,tilesWide,moveDirection,lesserBound,greaterBound);
    }

    private void loadWall(String[] elements) {
        float posX = Float.parseFloat(elements[1]);
        float posY = Float.parseFloat(elements[2]);
        int wallLength = Integer.parseInt(elements[3]);
        createWall(posX,posY,wallLength);
    }

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

    private void loadPlatform(String[] elements) {
        float posX = Float.parseFloat(elements[1]);
        float posY = Float.parseFloat(elements[2]);
        int platformLength = Integer.parseInt(elements[3]);
        createPlatform(posX,posY,platformLength);
    }

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
