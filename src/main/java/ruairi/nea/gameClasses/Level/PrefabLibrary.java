package ruairi.nea.gameClasses.Level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrefabLibrary {
    private final List<PrefabMetadata> prefabs;
    private final Random random;
    
    public PrefabLibrary(long seed) {
        this.prefabs = new ArrayList<>();
        this.random = new Random(seed);
        loadMetadata();
    }
    
    private void loadMetadata() {
        try {
            BufferedReader reader = new BufferedReader(
                new FileReader("assets/prefabMetaData.csv")
            );
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                prefabs.add(new PrefabMetadata(
                    Integer.parseInt(parts[0].trim()),
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
    
    public PrefabMetadata selectByDifficulty(int difficulty) {
        
        List<PrefabMetadata> filtered = new ArrayList<>();
        for (PrefabMetadata p : prefabs) {
            if (p.getDifficulty()==(difficulty)) {
                filtered.add(p);
            }
        }
        
        if (filtered.isEmpty()) return prefabs.get(0);
        return filtered.get(random.nextInt(filtered.size()));
    }
    
    public PrefabMetadata selectByType(String type) {
        List<PrefabMetadata> filtered = new ArrayList<>();
        for (PrefabMetadata p : prefabs) {
            if (p.getType().equals(type)) {
                filtered.add(p);
            }
        }
        
        if (filtered.isEmpty()) return prefabs.get(0);
        return filtered.get(random.nextInt(filtered.size()));
    }
}