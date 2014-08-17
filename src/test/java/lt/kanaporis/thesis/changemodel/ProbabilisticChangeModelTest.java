package lt.kanaporis.thesis.changemodel;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class ProbabilisticChangeModelTest {

    @Test
    public void testEmptyProbs() throws Exception {
        ProbabilisticChangeModel model = new ProbabilisticChangeModel();
        assertEquals(0, model.getSubProb("a", "h1"), 1E-6);
        assertEquals(0, model.getDelProb("a"), 1E-6);
        assertEquals(0, model.getInsProb("a"), 1E-6);
        assertEquals(0, model.getStopProb(), 1E-6);
        assertNull(model.getRandomLabel());
    }

    @Test
    public void testGetLabels() throws Exception {
        ProbabilisticChangeModel model = new ProbabilisticChangeModel();
        model.setDelProb("h1", 0.5);
        model.setDelProb("a", 0.5);
        model.setInsProb("strong", 1.0);
        model.setStopProb(0.75);
        model.setSubProb("span", "h1", 0.65);

        Set<String> labels = model.getLabels();
        assertEquals(4, labels.size());
        assertTrue(labels.contains("a"));
        assertTrue(labels.contains("h1"));
        assertTrue(labels.contains("span"));
        assertTrue(labels.contains("strong"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubProbTooLarge() throws Exception {
        new ProbabilisticChangeModel().setSubProb("a", "span", 1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubProbTooSmall() throws Exception {
        new ProbabilisticChangeModel().setSubProb("a", "span", -1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelProbTooLarge() throws Exception {
        new ProbabilisticChangeModel().setDelProb("a", 1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelProbTooSmall() throws Exception {
        new ProbabilisticChangeModel().setDelProb("a", -1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsProbTooLarge() throws Exception {
        new ProbabilisticChangeModel().setInsProb("a", 1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tesInsProbTooSmall() throws Exception {
        new ProbabilisticChangeModel().setInsProb("a", -1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStopProbTooLarge() throws Exception {
        new ProbabilisticChangeModel().setStopProb(1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStopProbTooSmall() throws Exception {
        new ProbabilisticChangeModel().setStopProb(-1.1);
    }

    @Test
    public void testValidateSuccess() throws Exception {
        ProbabilisticChangeModel model = new ProbabilisticChangeModel();

        model.setStopProb(0.25);

        model.setInsProb("a", 0.33);
        model.setInsProb("p", 0.33);
        model.setInsProb("h1", 0.34);

        assertFalse(model.valid());

        model.setDelProb("a", 0.33);
        model.setDelProb("p", 0.33);
        model.setDelProb("h1", 0.33);

        assertFalse(model.valid());

        model.setSubProb("a", "p", 0.2);
        model.setSubProb("a", "h1", 0.2);
        model.setSubProb("p", "h1", 0.2);
        model.setSubProb("p", "a", 0.2);
        model.setSubProb("h1", "p", 0.1);

        assertFalse(model.valid());

        model.setSubProb("h1", "a", 0.1);

        assertTrue(model.valid());
    }

}
