package LocalAreas;

import java.util.List;

/**
 *
 * @author Rasto
 */
public class Area {
    public int index;
    public List<Integer> vertices;
    public List<Float> csvValues;
    public List<Float> color;
    public float geoMean, ariMean, percentileSevFiv, min, max, rootMean, variance;
    
    @Override
    public String toString() {
        String str = "\nArea-> index: " + index + "\nvertices: " + vertices.toString()
                + "\ncsvValues: " + csvValues.toString() + "\nGeometric Mean: " + geoMean + "\nArithmetic Mean: " + ariMean + 
                "\nMaximal value: " + max + "\nMinimal value: " + min + "\n75 percentil: " + percentileSevFiv + "\nRoot Mean Square: " 
                + rootMean + "\nVariance: " + variance + "\n";

        return str;
    }
}
