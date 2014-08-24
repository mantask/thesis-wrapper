package lt.kanaporis.thesis.changemodel;

import java.util.HashMap;
import java.util.Map;

public class TransformationProbabilities {

    private final ProbabilisticChangeModel changeModel;
    private final Map<String, Map<String, Double>> probs = new HashMap<>();

    public TransformationProbabilities(ProbabilisticChangeModel changeModel) {
        this.changeModel = changeModel;
    }

    public void put(String key1, String key2, double prob) {
        if (!probs.containsKey(key1)) {
            probs.put(key1, new HashMap<String, Double>());
        }
        probs.get(key1).put(key2, prob);
    }

    public double get(String key1, String key2) {
        if (key1.equals(key2)) {
            return changeModel.stopProb();
        }
        if (!probs.containsKey(key1) || !probs.get(key1).containsKey(key2)) {
            return 0.0;
        }
        return probs.get(key1).get(key2);
    }

    // --- Object ------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<String, Double>> tree1 : probs.entrySet()) {
            for (Map.Entry<String, Double> tree2 : tree1.getValue().entrySet()) {
                sb.append(tree1.getKey())
                        .append("->")
                        .append(tree2.getKey())
                        .append("=")
                        .append(tree2.getValue())
                        .append("\n");
            }
        }
        return sb.toString();
    }
}
