package ruairi.nea.gameClasses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LevelLoader {

    GameScreen gameScreen;

    public LevelLoader(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    };

    public ArrayList<Sprite> loadLevel(int level){
        ArrayList<Sprite> levelObjects = new ArrayList<>();

        try{

            String levelFile = switch (level) {
                case 1 -> "assets/level1.csv";
                case 2 -> "assets/level2.csv";
                case 3 -> "assets/level3.csv";
                case 4 -> "assets/level4.csv";
                case 5 -> "assets/level5.csv";

                default -> throw new IllegalStateException("Unexpected value: " + level);
            };

            BufferedReader reader = new BufferedReader(new FileReader(levelFile));

            reader.readLine();
            String line;

            while ((line = reader.readLine())!=null){
                String[] elements = line.split(",");
                float posX = Float.parseFloat(elements[0]);
                float posY = Float.parseFloat(elements[1]);
                String typeName = elements[2].strip().toUpperCase();
                PlatformType type = switch (typeName){
                    case "GRASS" -> PlatformType.GRASS;
                    case "WIDEGRASS" -> PlatformType.WIDEGRASS;
                    default -> throw new IllegalArgumentException(typeName+" is not a valid type of platform");
                };
                gameScreen.createPlatform(posX, posY,type);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return levelObjects;
    }
}
