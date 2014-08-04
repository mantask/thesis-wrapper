package lt.kanaporis.thesis.wrapper.probabilistic;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Probabilistic distribution over the next possible state of a web page.
 * θ = { p_stop, { p_del(l) }, { p_ins(l) }, { p_sub(l_1, l_2) } }
 *
 * Created by mantas on 8/1/14.
 */
public class ChangeModel {

    private Double stopProb;
    private Map<String, Double> insProbs = new HashMap<>();
    private Map<String, Double> delProbs = new HashMap<>();
    private Map<String, Double> subProbs = new HashMap<>();
    private Set<String> labels = new HashSet<>();

    // --- Getters ------------------------------------------

    public double getSubProb(String label1, String label2) {
        Double probability = subProbs.get(subKey(label1, label2));
        return probability != null ? probability : 0.0;
    }

    public double getDelProb(String label) {
        Double probability = delProbs.get(label);
        return probability != null ? probability : 0.0;
    }

    public double getInsProb(String label) {
        Double probability = insProbs.get(label);
        return probability != null ? probability : 0.0;
    }

    public double getStopProb() {
        return stopProb != null ? stopProb : 0.0;
    }

    public String getRandomLabel() {
        // TODO must take prob distribution into account, e.g. p_ins(l)
        int labelCount = labels.size();
        int pos = new Double(Math.random() * labelCount).intValue();
        Object[] labelArray = labels.toArray();
        for (int i = pos; ; i = (i + 1) % labels.size()) {
            if (labelArray[i] != null) {
                return (String) labelArray[i];
            }
        }
    }

    public Set<String> getLabels() {
        return labels;
    }

    // --- Setters ------------------------------------------

    public void setSubProb(String label1, String label2, double probability)  {
        Validate.isTrue(0.0 <= probability && probability <= 1.0);
        subProbs.put(subKey(label1, label2), probability);
        labels.add(label1);
        labels.add(label2);
    }

    public void setDelProb(String label, double probability)  {
        Validate.isTrue(0.0 <= probability && probability <= 1.0);
        delProbs.put(label, probability);
        labels.add(label);
    }

    public void setInsProb(String label, double probability)  {
        Validate.isTrue(0.0 <= probability && probability <= 1.0);
        insProbs.put(label, probability);
        labels.add(label);
    }

    public void setStopProb(double probability)  {
        Validate.isTrue(0.0 < probability && probability < 1.0);
        stopProb = probability;
    }

    // --------------------------------------------------------

    private String subKey(String label1, String label2) {
        return label1 + ":" + label2;
    }

}
