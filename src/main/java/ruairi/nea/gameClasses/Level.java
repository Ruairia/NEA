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
                switch (elements[0]) {
                    case "SPAWNPOINT" -> loadHero(elements);
                    case "PLATFORM" -> loadPlatformTile(elements);
                    case "ENEMY" -> loadEnemy(elements);
                    case "CHECKPOINT" -> loadCheckpoint(elements);
                    default -> throw new IllegalStateException("Unexpected value: " + elements[0]);
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
            default -> throw new IllegalArgumentException(elements[3]);
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

    private void loadPlatformTile(String[] elements) {
        float posX = Float.parseFloat(elements[1]);
        float posY = Float.parseFloat(elements[2]);
        String typeName = elements[3].strip().toUpperCase();
        switch (typeName){
            case "SINGLE" ->{
                createPlatform(posX,posY,1);
            }
            case "NARROW" -> {
                createPlatform(posX,posY,2);
            }
            case "WIDE" -> {
                createPlatform(posX,posY,4);
            }
            case "ULTRAWIDE" -> {
                createPlatform(posX,posY,6);
            }
            default -> throw new IllegalArgumentException(typeName+" is not a valid type of platform");
        }
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
