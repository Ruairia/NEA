package ruairi.nea.gameClasses.Level;

public class PrefabMetadata {
    private final int id;
    private final int width;
    private final int difficulty;
    private final String type;
    
    public PrefabMetadata(int id, int width, String type, String difficulty) {
        this.id = id;
        this.width = width;
        this.difficulty = Integer.parseInt(difficulty);
        this.type = type;
    }
    
    public int getId() { return id; }
    public int getWidth() { return width; }
    public int getDifficulty() { return difficulty; }
    public String getType() { return type; }
}