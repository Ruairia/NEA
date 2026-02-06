package ruairi.nea.gameClasses.Level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PrefabLibrary {
    private final HashMap<Integer, PrefabMetadata> prefabs;
    private final Random random;
    
    public PrefabLibrary(long seed) {
        this.prefabs = new HashMap<>();
        this.random = new Random(seed);
        loadMetadata();
    }
    
    private void loadMetadata() {
        try {
            BufferedReader reader = new BufferedReader(
                new FileReader("assets/Level/prefabMetaData.csv")
            );
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                prefabs.put(id,new PrefabMetadata(
                    id,
                    Integer.parseInt(parts[1].trim()),
                    parts[2].trim(),
                    parts[3].trim()
                ));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public PrefabMetadata selectByDifficultyAndWidth(int difficulty,float selectionWidth) {
        
        List<PrefabMetadata> filtered = new ArrayList<>();

        for (PrefabMetadata prefabMetadata : prefabs.values()) {
            if (selectionWidth >= prefabMetadata.getWidth()){
                filtered.add(prefabMetadata);
            }
        }

        if (filtered.size()<=4) {
            for (PrefabMetadata prefabMetadata : prefabs.values()) {
                if (Math.abs(prefabMetadata.getDifficulty()-(difficulty))<=1){
                    filtered.add(prefabMetadata);
                }
            }

        }
        
        if (filtered.isEmpty()) return prefabs.get(11);
        return filtered.get(random.nextInt(filtered.size()));
    }

    public PrefabMetadata get(int id){
        return prefabs.get(id);
    }

}