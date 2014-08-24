package lt.kanaporis.thesis.changemodel;

import lt.kanaporis.thesis.Fixture;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransformationProbabilitiesTest {

    @Test
    public void testPut() throws Exception {
        TransformationProbabilities probs = new TransformationProbabilities(Fixture.abcChangeModel);
        probs.put("a", "b", 0.05);
        probs.put("a", "b", 0.1);
        assertEquals(0.1, probs.get("a", "b"), 1E-6);
        assertEquals(0.15, probs.get("a", "a"), 1E-6); // P_stop
        assertEquals(0.0, probs.get("b", "a"), 1E-6);
        assertEquals(0.0, probs.get("c", "a"), 1E-6);
        assertEquals(0.0, probs.get("c", "e"), 1E-6);
    }
}
