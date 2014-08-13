package lt.kanaporis.thesis.changemodel;

import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Node;

import java.util.Iterator;

/**
 * Created by mantas on 8/1/14.
 */
public class ProbabilisticTransducer {

    private final ProbabilisticChangeModel changeModel;

    public ProbabilisticTransducer(ProbabilisticChangeModel changeModel) {
        this.changeModel = changeModel;
    }

    /**
     * A random process that makes one pass over the whole forest and at each position,
     * it randomly decides to either delete a node, change its label, or insert new nodes.
     *
     * @param forest
     * @return
     */
    public Forest transform(Forest forest) {
        Forest transformedForest = new Forest();
        for (Node tree : forest.getTrees()) {
            transformedForest = transformedForest.add(transformByDeleteAndSubstitute(tree));
        }
        return transformByInsert(transformedForest);
    }

    /**
     * The process π_ins takes a forest and performs a set of random inserts at the root level.
     *
     * @param f
     * @return
     */
    private Forest transformByInsert(Forest f) {
        if (Math.random() < changeModel.getStopProb()) {
            return f;
        } else {
            return transformByInsert(insertRandomRoot(f));
        }
    }

    /**
     * An insert operation that adds a node at the top of the forest chosen randomly from all such operations.
     *
     * @param forest
     * @return
     */
    private Forest insertRandomRoot(Forest forest) {
        int fTreeCount = forest.getTrees().size();
        int subSequenceStart = new Double(Math.random() * fTreeCount - 1).intValue();
        int subSequenceLength = new Double(Math.random() * (fTreeCount - subSequenceStart - 1)).intValue();
        Iterator<Node> fTreeIterator = forest.getTrees().iterator();

        Forest transformedForest = new Forest();

        // copy first getTrees
        for (int i = 0; i < subSequenceStart; i++) {
            transformedForest = transformedForest.add(fTreeIterator.next());
        }

        // add parent to the middle getTrees
        // TODO it's critical to respect the probabilistic distribution of insert operation
        Node randomRoot = Node.elem(changeModel.getRandomLabel());
        for (int i = 0; i < subSequenceLength; i++) {
            randomRoot.addChild(fTreeIterator.next());
        }
        transformedForest = transformedForest.add(randomRoot);

        // copy last getTrees
        for (int i = subSequenceStart + subSequenceLength + 1; i < fTreeCount; i++) {
            transformedForest = transformedForest.add(fTreeIterator.next());
        }

        return transformedForest;
    }

    /**
     * The process π_ds takes a tree and transforms it into a forest. It either deletes the root
     * of the tree or changes its label and then recursively transforms the subtreees of the tree.
     *
     * @param tree
     * @return
     */
    private Forest transformByDeleteAndSubstitute(Node tree) {
        if (Math.random() < changeModel.getDelProb(tree.getLabel())) {
            Forest f = new Forest();
            for (Node child : tree.getChildren()) {
                f = f.add(transformByDeleteAndSubstitute(child));
            }
            return f;
        } else {
            // TODO it's critical to respect the probabilistic distribution of substitute operation
            Node newRandomRoot = Node.elem(changeModel.getRandomLabel());
            newRandomRoot.addChild(transform(new Forest(tree.getChildren())));
            return new Forest(newRandomRoot);
        }
    }

    // -------------------------------------------------------------

    /**
     * Calculates the prob of transforming a tree f1 into a tree f2, given insertion,
     * deletion and substitution operations and their probabilities, i.e. change model.
     *
     * Implements function DP_1(F_s, F_t), which calculates prob that P_θ(T|S), i.e.
     * that π(F_s) = F_t.
     *
     * DP_1(F_s, F_t) = DP_2(F_s, F_t) + p × DP_1(F_s, F_t - v)
     *
     * @param origForest Original tree.
     * @param transForest Transformed tree.
     * @return Probability of transforming original tree into transformed one. Result in the range of [0..1].
     */
    public double prob(Forest origForest, Forest transForest) {

        // TODO add more of termination conditoins
        if (transForest.isEmpty()) {
            return changeModel.getStopProb();
        }

        // TODO add Optimizations for similar trees (ref §4.1.1 Dalvi'09)

        Node lastRootOfTransForest = transForest.getLastTree();
        // TODO record Prob2[origForest][transForest] when origForest.isTree() & transForest.isTree() for ProbabilisticWrapper
        // TODO somehow record Prob1[origForest][transForest], ie how do we tell by the forest the prefix?
        double probabilityWhenLastTreeRootWasInserted = changeModel.getInsProb(lastRootOfTransForest.getLabel()) *
                prob(origForest, transForest.removeNode(lastRootOfTransForest));
        return probWhenLastTreeRootWasSub(origForest, transForest) +
                probabilityWhenLastTreeRootWasInserted;
    }

    /**
     * Calculates the prob of transforming a tree f1 into a tree f2, given insertion,
     * deletion and substitution operations and their probabilities, i.e. change model.
     *
     * Implements function DP_2(F_s, F_t), which calculates prob that P_θ(T|S) when
     * the last tree root v of forest T was substituted, i.e. that π(F_s) = F_t and v was generated
     * by substitution under π.
     *
     * DP_2(F_s, F_t) = p_1 × DP_1(F_2 - [u], F_t - [v]) × DP_1(⌊u⌋, ⌊v⌋) + p_2 × DP_2(F_s - u, F_t)
     *
     * @param f1 Original tree.
     * @param f2 Transformed tree.
     * @return Probability of transforming original tree into transformed one. Result in the range of [0..1].
     */
    public double probWhenLastTreeRootWasSub(Forest origForest, Forest transForest) {
        Node lastRootOfOrigForest = origForest.getLastTree();
        Node lastRootOfTransForest = transForest.getLastTree();
        return changeModel.getSubProb(lastRootOfOrigForest.getLabel(), lastRootOfTransForest.getLabel()) *
                prob(origForest.removeTree(lastRootOfOrigForest), transForest.removeTree(lastRootOfTransForest)) *
                prob(lastRootOfOrigForest.getSubForest(), lastRootOfTransForest.getSubForest()) +
                changeModel.getDelProb(lastRootOfOrigForest.getLabel()) *
                probWhenLastTreeRootWasSub(origForest.removeNode(lastRootOfOrigForest), transForest);
    }

}
