package lt.kanaporis.thesis.region;

/**
 * diff(length, index (compared to previous))
 * e.g. diff[2][3] - gen.node length = 2, diff between (1,2) and (3,4) ← or smth else?
 */
public class DiffsToNextGeneralizedNode {

    private final double[][] editDistances;

    public DiffsToNextGeneralizedNode(int nodeCount, int maxLength) {
        editDistances = new double[nodeCount - 1][maxLength];
    }

    public double from(int pos, int len) {
        return editDistances[pos][len - 1];
    }

    public void from(int pos, int len, double dist) {
        editDistances[pos][len - 1] = dist;
    }

}
