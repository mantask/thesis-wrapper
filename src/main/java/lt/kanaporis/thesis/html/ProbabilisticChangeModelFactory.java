package lt.kanaporis.thesis.html;

import lt.kanaporis.thesis.changemodel.ProbabilisticChangeModel;
import org.apache.commons.lang3.Validate;

/**
 * Reads probabilistic change model from a file
 */
public class ProbabilisticChangeModelFactory {

    /**
     * Reads change model from file
     * @param filename
     * @return
     */
    public static ProbabilisticChangeModel buildFromCsv(String filename) {
        ProbabilisticChangeModel changeModel = new ProbabilisticChangeModel();
        // TODO
        Validate.isTrue(changeModel.valid(), "Change model invariants do not hold!");
        return changeModel;
    }

}
