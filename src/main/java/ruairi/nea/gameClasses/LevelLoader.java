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

    public ArrayList<Sprite> loadLevel(int level){
        ArrayList<Sprite> levelObjects = new ArrayList<>();

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
            line = reader.readLine();
            String[] elements = line.split(",");
            spawnPointX = Float.parseFloat(elements[0]);
            spawnPointY = Float.parseFloat(elements[1]);
            while ((line = reader.readLine())!=null){
                elements = line.split(",");
                float posX = Float.parseFloat(elements[0]);
                float posY = Float.parseFloat(elements[1]);
                String typeName = elements[2].strip().toUpperCase();
                PlatformType type = switch (typeName){
                    case "GRASS" -> PlatformType.GRASS;
                    case "WIDEGRASS" -> PlatformType.WIDEGRASS;
                    default -> throw new IllegalArgumentException(typeName+" is not a valid type of platform");
                };
                levelObjects.add(new Platform(posX, posY,type));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return levelObjects;
    }

    public float getSpawnPointX() {
        return spawnPointX;
    }

    public float getSpawnPointY() {
        return spawnPointY;
    }
}
