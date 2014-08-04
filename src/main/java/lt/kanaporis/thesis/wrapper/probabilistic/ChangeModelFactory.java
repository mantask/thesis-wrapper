package lt.kanaporis.thesis.wrapper.probabilistic;

import lt.kanaporis.thesis.NumberUtils;
import org.apache.commons.lang3.Validate;

/**
 * Created by mantas on 8/2/14.
 */
public class ChangeModelFactory {

    /**
     * Reads change model from file
     * @param filename
     * @return
     */
    public static ChangeModel buildFromCsv(String filename) {
        ChangeModel changeModel = new ChangeModel();
        // TODO
        Validate.isTrue(verify(changeModel, "Change model invariants do not hold!"));
        return changeModel;
    }

    /**
     * Verifies invariants of change model.
     * @param changeModel
     * @return
     */
    public static boolean verify(ChangeModel changeModel) {
        return verifyStop(changeModel) &&
                verifyDel(changeModel) &&
                verifyIns(changeModel) &&
                verifySub(changeModel);
    }

    private static boolean verifySub(ChangeModel cm) {
        double sum = 0.0;
        for (String label1 : cm.getLabels()) {
            for (String label2 : cm.getLabels()) {
                // TODO even if label1=label2 ?
                double probSub = cm.getSubProb(label1, label2);
                if (le(probSub, 0)) {
                    return false;
                }
                sum += probSub;
            }
        }
        return eq(sum, 1.0);
    }

    private static boolean verifyIns(ChangeModel cm) {
        double sum = 0.0;
        for (String label : cm.getLabels()) {
            double probIns = cm.getInsProb(label);
            if (le(probIns, 0)) {
                return false;
            }
            sum += probIns;
        }
        return eq(sum, 1.0);
    }

    private static boolean verifyDel(ChangeModel cm) {
        for (String label : cm.getLabels()) {
            double delProb = cm.getDelProb(label);
            if (lt(delProb, 0) || lt(1, delProb)) {
                return false;
            }
        }
        return true;
    }

    private static boolean verifyStop(ChangeModel cm) {
        double stopProb = cm.getStopProb();
        return lt(0, stopProb) && lt(stopProb, 1);
    }

    private static boolean eq(double a, double b) {
        return Math.abs(a - b) < 1E-6;
    }

    private static boolean lt(double a, double b) {
        return gt(b, a);
    }

    private static boolean gt(double a, double b) {
        return a - b > 1E-6;
    }

    private static boolean le(double a, double b) {
        return lt(a, b) || eq(a, b);
    }

}
