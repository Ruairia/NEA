package ruairi.nea.gameClasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LevelLoader {

    GameScreen gameScreen;
    private float spawnPointX;
    private float spawnPointY;



    public LevelLoader(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    };

    public ArrayList<Entity> loadLevel(int level){
        ArrayList<Entity> levelObjects = new ArrayList<>();

        try{

            String levelPlatformsFile = switch (level) {
                case 1 -> "assets/level1Platforms.csv";
                case 2 -> "assets/level2Platforms.csv";
                case 3 -> "assets/level3Platforms.csv";
                case 4 -> "assets/level4Platforms.csv";
                case 5 -> "assets/level5Platforms.csv";

                default -> throw new IllegalStateException("Unexpected value: " + level);
            };

            BufferedReader reader = new BufferedReader(new FileReader(levelPlatformsFile));

            String line;
            String[] elements;

            while ((line = reader.readLine())!=null){
                elements = line.split(",");
                switch (elements[0]){
                    case "SPAWNPOINT" -> loadSpawnPoint(elements);
                    case "PLATFORM" -> loadPlatform(elements, levelObjects);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return levelObjects;
    }

    private void loadSpawnPoint(String[] elements){
        spawnPointX = Float.parseFloat(elements[1]);
        spawnPointY = Float.parseFloat(elements[2]);
    }

    private static void loadPlatform(String[] elements, ArrayList<Entity> levelObjects) {
        float posX = Float.parseFloat(elements[1]);
        float posY = Float.parseFloat(elements[2]);
        String typeName = elements[3].strip().toUpperCase();
        Platform.PlatformType type = switch (typeName){
            case "GRASS" -> Platform.PlatformType.GRASS;
            case "WIDEGRASS" -> Platform.PlatformType.WIDEGRASS;
            default -> throw new IllegalArgumentException(typeName+" is not a valid type of platform");
        };
        levelObjects.add(new Platform(posX, posY,type));
    }

    public float getSpawnPointX() {
        return spawnPointX;
    }

    public float getSpawnPointY() {
        return spawnPointY;
    }
}
