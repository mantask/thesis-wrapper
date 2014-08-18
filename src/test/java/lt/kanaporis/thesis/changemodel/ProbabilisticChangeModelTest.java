package lt.kanaporis.thesis.changemodel;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class ProbabilisticChangeModelTest {

    @Test
    public void testEmptyProbs() throws Exception {
        ProbabilisticChangeModel model = new ProbabilisticChangeModel();
        assertEquals(0, model.subProb("a", "h1"), 1E-6);
        assertEquals(0, model.delProb("a"), 1E-6);
        assertEquals(0, model.insProb("a"), 1E-6);
        assertEquals(0, model.stopProb(), 1E-6);
        assertNull(model.randomLabel());
    }

    @Test
    public void testGetLabels() throws Exception {
        ProbabilisticChangeModel model = new ProbabilisticChangeModel();
        model.delProb("h1", 0.5);
        model.delProb("a", 0.5);
        model.insProb("strong", 1.0);
        model.stopProb(0.75);
        model.subProb("span", "h1", 0.65);

        Set<String> labels = model.labels();
        assertEquals(4, labels.size());
        assertTrue(labels.contains("a"));
        assertTrue(labels.contains("h1"));
        assertTrue(labels.contains("span"));
        assertTrue(labels.contains("strong"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubProbTooLarge() throws Exception {
        new ProbabilisticChangeModel().subProb("a", "span", 1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubProbTooSmall() throws Exception {
        new ProbabilisticChangeModel().subProb("a", "span", -1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelProbTooLarge() throws Exception {
        new ProbabilisticChangeModel().delProb("a", 1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelProbTooSmall() throws Exception {
        new ProbabilisticChangeModel().delProb("a", -1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsProbTooLarge() throws Exception {
        new ProbabilisticChangeModel().insProb("a", 1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tesInsProbTooSmall() throws Exception {
        new ProbabilisticChangeModel().insProb("a", -1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStopProbTooLarge() throws Exception {
        new ProbabilisticChangeModel().stopProb(1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStopProbTooSmall() throws Exception {
        new ProbabilisticChangeModel().stopProb(-1.1);
    }

    @Test
    public void testValidateSuccess() throws Exception {
        ProbabilisticChangeModel model = new ProbabilisticChangeModel();

        model.stopProb(0.25);

        model.insProb("a", 0.33);
        model.insProb("p", 0.33);
        model.insProb("h1", 0.34);

        assertFalse(model.valid());

        model.delProb("a", 0.33);
        model.delProb("p", 0.33);
        model.delProb("h1", 0.33);

        assertFalse(model.valid());

        model.subProb("a", "p", 0.2);
        model.subProb("a", "h1", 0.1);
        model.subProb("a", "a", 0.1);

        model.subProb("p", "h1", 0.2);
        model.subProb("p", "a", 0.1);
        model.subProb("p", "p", 0.1);

        model.subProb("h1", "p", 0.05);
        model.subProb("h1", "h1", 0.05);

        assertFalse(model.valid());

        model.subProb("h1", "a", 0.1);

        assertTrue(model.valid());
    }

}
