package ruairi.nea.gameClasses.Level;

import java.util.Random;

public class PerlinNoise {
    private final int[] permutation;
    
    public PerlinNoise(long seed) {
        Random random = new Random(seed);
        permutation = new int[512];
        int[] half = new int[256];
        
        for (int i = 0; i < 256; i++) {
            half[i] = i;
        }
        
        // Shuffle from back to front using Fisher-Yates algorithm
        for (int i = 255; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = half[i];
            half[i] = half[j];
            half[j] = temp;
        }
        
        // Duplicate first half to get full permutation array to avoid overflow checks
        for (int i = 0; i < 256; i++) {
            permutation[i] = permutation[i + 256] = half[i];
        }
    }
    
    public double noise(double x, double y) {
        int gridX = (int) Math.floor(x) & 255;
        int gridY = (int) Math.floor(y) & 255;
        
        double innerX = x-Math.floor(x);
        double innerY = y-Math.floor(y);
        
        double smoothX = smoothen(innerX);
        double smoothY = smoothen(innerY);
        
        int cornerBottomLeft = permutation[gridX] + gridY;
        int cornerBottomRight = permutation[gridX + 1] + gridY;

        // Bilinear interpolation between the four corners
        double bottomInterpolation = lerp(smoothX,
                gradient(permutation[cornerBottomLeft], innerX, innerY),
                gradient(permutation[cornerBottomRight], innerX - 1, innerY)
        );

        double topInterpolation = lerp(smoothX,
                gradient(permutation[cornerBottomLeft + 1], innerX, innerY - 1),
                gradient(permutation[cornerBottomRight + 1], innerX - 1, innerY - 1)
        );

        return lerp(smoothY, bottomInterpolation, topInterpolation);
    }
    
    private double smoothen(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10); //Uses polynomial 6t^5-15t^4+10t^3
    }
    
    private double lerp(double weight, double valueA, double valueB) {
        return valueA + weight*(valueB - valueA);
    }
    
    private double gradient(int hash, double x, double y) {
        int gradientIndex = hash & 15;  // Use bottom 4 bits (0-15)

        // Select gradient components based on hash
        double horizontalComponent = gradientIndex < 8 ? x : y;
        double verticalComponent = gradientIndex < 4 ? y :
                (gradientIndex == 12 || gradientIndex == 14 ? x : 0);

        // Apply direction (positive or negative) based on hash bits
        double signedHorizontal = (gradientIndex & 1) == 0 ?
                horizontalComponent : -horizontalComponent;
        double signedVertical = (gradientIndex & 2) == 0 ?
                verticalComponent : -verticalComponent;

        return signedHorizontal + signedVertical;
    }
}