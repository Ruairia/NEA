package ruairi.nea.gameClasses.Level;

public class LevelGenerator {
    private final Level level;
    private final PrefabLibrary prefabLibrary;
    private final PerlinNoise noise;
    
    private static final float MIN_SECTION_WIDTH = 800f;
    private static final int MAX_RECURSION_DEPTH = 4;

    
    public LevelGenerator(Level level, long seed) {
        this.level = level;
        this.prefabLibrary = new PrefabLibrary(seed);
        this.noise = new PerlinNoise(seed);
    }
    
    public void generateLevel(float totalWidth) {
        generateSection(0, totalWidth, MAX_RECURSION_DEPTH, 0);
        placePrefab(prefabLibrary.get(10),totalWidth);
    }
    
    private void generateSection(float x, float width, int depth, float progressX) {
        // Base case: section too small or max depth
        if (depth <= 0 || width < MIN_SECTION_WIDTH * 2) {
            placePrefabInSection(x, width, progressX);
            return;
        }
        
        // Use noise to decide split position
        double splitNoise = noise.noise(progressX * 0.001, depth * 0.5);
        float splitRatio = 0.4f + (float)(splitNoise * 0.2); // 0.4 to 0.6
        float splitX = x + width * splitRatio;
        
        // Recursively generate left and right sections
        generateSection(x, splitX - x, depth - 1, progressX);
        generateSection(splitX, width - (splitX - x), depth - 1, progressX + (splitX - x));
    }
    
    private void placePrefabInSection(float x, float width, float progressX) {
        // Determine difficulty based on progress
        int difficulty = getDifficultyForProgress(progressX);
        PrefabMetadata prefab = prefabLibrary.selectByDifficultyAndWidth(difficulty,width);
        
        placePrefab(prefab, x);
    }
    
    private void placePrefab(PrefabMetadata prefab, float offsetX) {
        try {
            level.loadPrefab(
                new String[]{"PREFAB", String.valueOf(offsetX), "0", String.valueOf(prefab.getId())},
                0, 0
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private int getDifficultyForProgress(float progressX) {
        // Use noise for variation
        double difficultyNoise = noise.noise(progressX * 0.0005, 42);
        
        // Base difficulty increases with progress
        float normalisedProgress = progressX / 5000f;
        double difficultyValue = normalisedProgress * 0.7 + difficultyNoise * 0.3;
        
        if (difficultyValue < 0.05) return 1;
        if (difficultyValue < 0.15) return 2;
        if (difficultyValue < 0.3) return 3;
        if (difficultyValue < 0.5) return 4;
        return 5;
    }
}