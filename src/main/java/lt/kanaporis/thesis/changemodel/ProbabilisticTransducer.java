package lt.kanaporis.thesis.changemodel;

import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        for (Tree tree : forest.trees()) {
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
     * Copy first trees, add a new root to middle trees, and copy last trees.
     *
     * @param forest
     * @return
     */
    private Forest insertRandomRoot(Forest forest) {
        int length = forest.trees().size();
        int fromIx = new Double(Math.random() * length - 1).intValue();
        int thruIx = fromIx + new Double(Math.random() * (length - fromIx - 1)).intValue();
        List<Tree> transformedTrees = new ArrayList<>();
        transformedTrees.addAll(forest.trees().subList(0, fromIx));
        transformedTrees.add(new Tree(
                Node.elem(changeModel.getRandomLabel()), // new random root
                forest.trees().subList(fromIx, thruIx).toArray(new Tree[] {}))); // sub-forest as children
        transformedTrees.addAll(forest.trees().subList(thruIx, length));
        return new Forest(transformedTrees);
    }

    /**
     * The process π_ds takes a tree and transforms it into a forest. It either deletes the root
     * of the tree or changes its label and then recursively transforms the subtreees of the tree.
     *
     * @param tree
     * @return
     */
    private Forest transformByDeleteAndSubstitute(Tree tree) {
        if (Math.random() < changeModel.getDelProb(tree.root().label())) {
            Forest f = new Forest();
            for (Tree subtree : tree.children()) {
                f = f.add(transformByDeleteAndSubstitute(subtree));
            }
            return f;
        } else {
            return new Forest(new Tree(
                    Node.elem(changeModel.getRandomLabel()), // random root
                    transform(new Forest(tree.children()))));
        }
    }

    // -------------------------------------------------------------

    public double prob(Tree origTree, Tree transTree) {
        return prob(new Forest(origTree), new Forest(transTree));
    }

    public double prob(Tree origTree, Forest transForest) {
        return prob(new Forest(origTree), transForest);
    }

    public double prob(Forest origForest, Tree transTree) {
        return prob(origForest, new Forest(transTree));
    }

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

        // TODO add more of termination conditions
        if (transForest.empty()) {
            return changeModel.getStopProb();
        }

        // TODO add Optimizations for similar trees (ref §4.1.1 Dalvi'09)
        // TODO record Prob2[origForest][transForest] when origForest.isTree() & transForest.isTree() for ProbabilisticWrapper
        // TODO somehow record Prob1[origForest][transForest], ie how do we tell by the forest the prefix?
        double probabilityWhenLastTreeRootWasInserted = changeModel.getInsProb(transForest.lastTree().root().label()) *
                prob(origForest, transForest.removeLastTreeNode());
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
        Tree lastOrigTree = origForest.lastTree();
        Tree lastTransTree = transForest.lastTree();
        return changeModel.getSubProb(lastOrigTree.root().label(), lastTransTree.root().label()) *
                prob(origForest.removeLastTree(), transForest.removeLastTree()) *
                prob(lastOrigTree.subforest(), lastTransTree.subforest()) +
                changeModel.getDelProb(lastOrigTree.root().label()) *
                probWhenLastTreeRootWasSub(origForest.removeLastTreeNode(), transForest);
    }

}
