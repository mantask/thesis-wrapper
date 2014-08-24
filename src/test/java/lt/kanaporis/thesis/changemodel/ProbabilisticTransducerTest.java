package lt.kanaporis.thesis.changemodel;

import lt.kanaporis.thesis.Fixture;
import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProbabilisticTransducerTest {

    // TODO extract into fixtures
    private Tree origTree = new Tree(Node.elem("a"),
            new Tree(Node.elem("b")));
    private Tree transTree = new Tree(Node.elem("c"));
    private ProbabilisticChangeModel cm = Fixture.abcChangeModel;
    private ProbabilisticTransducer transducer = new ProbabilisticTransducer(cm);

    @Test
    public void testFullTreeTransform() throws Exception {
        assertEquals(cm.subProb("a", "c") * cm.delProb("b") * cm.stopProb() * cm.stopProb() +
                        cm.subProb("b", "c") * cm.delProb("a") * cm.stopProb() * cm.stopProb() +
                        cm.delProb("a") * cm.delProb("b") * cm.insProb("c") * cm.stopProb(),
                transducer.prob(origTree, transTree), NumberUtils.THRESHOLD);
    }

    @Test
    public void testTransformToEmpty() throws Exception {
        assertEquals(cm.delProb("a") * cm.delProb("b") * cm.stopProb(),
                transducer.prob(origTree, Forest.EMPTY), NumberUtils.THRESHOLD);
    }

    @Test
    public void testTransformFromEmpty() throws Exception {
        assertEquals(cm.insProb("a") * cm.insProb("b") * cm.stopProb(),
                transducer.prob(Forest.EMPTY, origTree), NumberUtils.THRESHOLD);
    }

    @Test
    public void testTransformEmptyToEmpty() throws Exception {
        assertEquals(cm.stopProb(), transducer.prob(Forest.EMPTY, Forest.EMPTY), NumberUtils.THRESHOLD);
    }


}
