package ruairi.nea.gameClasses;

import ruairi.nea.gameClasses.Entities.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Level {

    public ArrayList<Entity> allEntities;
    public ArrayList<Platform> platforms;
    public ArrayList<Enemy> damagingEntities;
    public ArrayList<Entity> mobileEntities;

    private Hero hero;
    public Background background;

    public Level() {
        allEntities = new ArrayList<>();
        platforms = new ArrayList<>();
        damagingEntities = new ArrayList<>();
        mobileEntities = new ArrayList<>();

    };

    public void loadLevel(int level) {

        background = new Background(level);

        try {

            BufferedReader levelReader = getLevelReader(level);

            String line;
            String[] elements;

            while ((line = levelReader.readLine()) != null) {
                elements = line.split(",");
                allEntities.add(switch (elements[0]) {
                    case "SPAWNPOINT" -> loadHero(elements);
                    case "PLATFORM" -> loadPlatform(elements);
                    case "ENEMY" -> loadEnemy(elements);
                    default -> throw new IllegalStateException("Unexpected value: " + elements[0]);
                });
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

    private Enemy loadEnemy(String[] elements){
        float posX = Float.parseFloat(elements[1]);
        float posY = Float.parseFloat(elements[2]);
        Enemy enemy;
        switch (elements[3]){
            case "FIREBALL" -> enemy = new Fireball(posX,posY,Float.parseFloat(elements[4]),Float.parseFloat(elements[5]));
            default -> throw new IllegalArgumentException(elements[3]);
        }
        damagingEntities.add(enemy);
        return enemy;
    }

    private Hero loadHero(String[] elements){
        hero = new Hero().setSpawnPoint(Float.parseFloat(elements[1]), Float.parseFloat(elements[2]));
        mobileEntities.add(hero);
        return hero;
    }

    private Platform loadPlatform(String[] elements) {
        float posX = Float.parseFloat(elements[1]);
        float posY = Float.parseFloat(elements[2]);
        String typeName = elements[3].strip().toUpperCase();
        Platform.PlatformType type = switch (typeName){
            case "GRASS" -> Platform.PlatformType.GRASS;
            case "WIDEGRASS" -> Platform.PlatformType.WIDEGRASS;
            default -> throw new IllegalArgumentException(typeName+" is not a valid type of platform");
        };
        Platform platform = new Platform(posX, posY,type);
        platforms.add(platform);
        return platform;
    }


    public Hero getHero() {
        return hero;
    }
}
