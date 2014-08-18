package lt.kanaporis.thesis.changemodel;

import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;
import org.apache.commons.lang3.Validate;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProbabilisticTransducerTest {

    private Tree origTree = new Tree(Node.elem("a"),
            new Tree(Node.elem("b")));
    private Tree transTree = new Tree(Node.elem("c"));
    private ProbabilisticChangeModel cm = getChangeModel();
    private ProbabilisticTransducer transducer = new ProbabilisticTransducer(cm);

    @Test
    public void testFullTreeTransform() throws Exception {
        assertEquals(cm.subProb("a", "c") * cm.delProb("b") * cm.stopProb() * cm.stopProb() +
                        cm.subProb("b", "c") * cm.delProb("a") * cm.stopProb() * cm.stopProb() +
                        cm.delProb("a") * cm.delProb("b") * cm.insProb("c") * cm.stopProb(),
                transducer.prob(origTree, transTree), 1E-6);
    }

    @Test
    public void testTransformToEmpty() throws Exception {
        assertEquals(cm.delProb("a") * cm.delProb("b") * cm.stopProb(),
                transducer.prob(origTree, Forest.EMPTY), 1E-6);
    }

    @Test
    public void testTransformFromEmpty() throws Exception {
        assertEquals(cm.insProb("a") * cm.insProb("b") * cm.stopProb(),
                transducer.prob(Forest.EMPTY, origTree), 1E-6);
    }

    @Test
    public void testTransformEmptyToEmpty() throws Exception {
        assertEquals(cm.stopProb(), transducer.prob(Forest.EMPTY, Forest.EMPTY), 1E-6);
    }

    public ProbabilisticChangeModel getChangeModel() {
        ProbabilisticChangeModel model = new ProbabilisticChangeModel();

        model.stopProb(0.15);

        model.insProb("a", 0.2);
        model.insProb("b", 0.3);
        model.insProb("c", 0.5);

        model.delProb("a", 0.12);
        model.delProb("b", 0.34);
        model.delProb("c", 0.45);

        model.subProb("a", "b", 0.17);
        model.subProb("a", "c", 0.13);
        model.subProb("a", "a", 0.05);

        model.subProb("b", "a", 0.2);
        model.subProb("b", "b", 0.05);
        model.subProb("b", "c", 0.1);

        model.subProb("c", "a", 0.15);
        model.subProb("c", "b", 0.05);
        model.subProb("c", "c", 0.10);

        Validate.isTrue(model.valid());

        return model;
    }

}
