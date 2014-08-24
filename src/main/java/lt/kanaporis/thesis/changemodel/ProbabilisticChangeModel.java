package lt.kanaporis.thesis.changemodel;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static lt.kanaporis.thesis.changemodel.NumberUtils.*;

/**
 * Probabilistic distribution over the next possible state of a web page.
 * θ = { p_stop, { p_del(l) }, { p_ins(l) }, { p_sub(l_1, l_2) } }
 *
 * Created by mantas on 8/1/14.
 */
public class ProbabilisticChangeModel {

    private Double stopProb;
    private Map<String, Double> insProbs = new HashMap<>();
    private Map<String, Double> delProbs = new HashMap<>();
    private Map<String, Double> subProbs = new HashMap<>();
    private Set<String> labels = new HashSet<>();

    // --- Getters ------------------------------------------

    public double subProb(String label1, String label2) {
        if (label1.equals(label2)) {
            return stopProb();
        }
        Double probability = subProbs.get(subKey(label1, label2));
        return probability != null ? probability : 0.0;
    }

    public double delProb(String label) {
        Double probability = delProbs.get(label);
        return probability != null ? probability : 0.0;
    }

    public double insProb(String label) {
        Double probability = insProbs.get(label);
        return probability != null ? probability : 0.0;
    }

    public double stopProb() {
        return stopProb != null ? stopProb : 0.0;
    }

    public String randomLabel() {
        // TODO must take prob distribution into account, e.g. p_ins(l)
        int labelCount = labels.size();
        if (labelCount == 0) {
            return null;
        }
        int pos = new Double(Math.random() * labelCount).intValue();
        Object[] labelArray = labels.toArray();
        for (int i = pos; ; i = (i + 1) % labels.size()) {
            if (labelArray[i] != null) {
                return (String) labelArray[i];
            }
        }
    }

    public Set<String> labels() {
        return labels;
    }

    // --- Setters ------------------------------------------

    public void subProb(String label1, String label2, double probability)  {
        Validate.isTrue(0.0 <= probability && probability <= 1.0);
        subProbs.put(subKey(label1, label2), probability);
        labels.add(label1);
        labels.add(label2);
    }

    public void delProb(String label, double probability)  {
        Validate.isTrue(0.0 <= probability && probability <= 1.0);
        delProbs.put(label, probability);
        labels.add(label);
    }

    public void insProb(String label, double probability)  {
        Validate.isTrue(0.0 <= probability && probability <= 1.0);
        insProbs.put(label, probability);
        labels.add(label);
    }

    public void stopProb(double probability)  {
        Validate.isTrue(0.0 < probability && probability < 1.0);
        stopProb = probability;
    }
    
    // --- Verifying ------------------------------------------

    /**
     * Verifies invariants of change model.
     */
    public boolean valid() {
        return validStop() &&
                validDel() &&
                validIns() &&
                validSub();
    }

    private boolean validSub() {
        double sum = 0.0;
        for (String label1 : labels()) {
            for (String label2 : labels()) {
                // we allow label1=label2 probabilities in model
                double probSub = subProb(label1, label2);
                if (le(probSub, 0)) {
                    return false;
                }
                sum += probSub;
            }
        }
        return eq(sum, 1.0);
    }

    private boolean validIns() {
        double sum = 0.0;
        for (String label : labels()) {
            double probIns = insProb(label);
            if (le(probIns, 0)) {
                return false;
            }
            sum += probIns;
        }
        return eq(sum, 1.0);
    }

    private boolean validDel() {
        for (String label : labels()) {
            double delProb = delProb(label);
            if (lt(delProb, 0) || lt(1, delProb)) {
                return false;
            }
        }
        return true;
    }

    private boolean validStop() {
        double stopProb = stopProb();
        return lt(0, stopProb) && lt(stopProb, 1);
    }

    // --------------------------------------------------------

    private String subKey(String label1, String label2) {
        return label1 + ":" + label2;
    }

}
