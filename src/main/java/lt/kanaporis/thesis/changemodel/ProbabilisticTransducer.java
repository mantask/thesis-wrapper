package lt.kanaporis.thesis.changemodel;

import lt.kanaporis.thesis.Config;
import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.PostOrderNavigator;
import lt.kanaporis.thesis.tree.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProbabilisticTransducer {

    private final ProbabilisticChangeModel changeModel;

    public ProbabilisticTransducer(ProbabilisticChangeModel changeModel) {
        this.changeModel = changeModel;
    }

    /**
     * A random process that makes one pass over the whole forest and at each position,
     * it randomly decides to either delete a node, change its label, or insert new nodes.
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
     */
    private Forest transformByInsert(Forest f) {
        if (Math.random() < changeModel.stopProb()) {
            return f;
        } else {
            return transformByInsert(insertRandomRoot(f));
        }
    }

    /**
     * An insert operation that adds a node at the top of the forest chosen randomly from all such operations.
     * Copy first trees, add a new root to middle trees, and copy last trees.
     */
    private Forest insertRandomRoot(Forest forest) {
        int length = forest.trees().size();
        int fromIx = new Double(Math.random() * length - 1).intValue();
        int thruIx = fromIx + new Double(Math.random() * (length - fromIx - 1)).intValue();
        List<Tree> transformedTrees = new ArrayList<>();
        transformedTrees.addAll(forest.trees().subList(0, fromIx));
        transformedTrees.add(new Tree(
                Node.elem(changeModel.randomLabel()), // new random root
                forest.trees().subList(fromIx, thruIx).toArray(new Tree[] {}))); // sub-forest as children
        transformedTrees.addAll(forest.trees().subList(thruIx, length));
        return new Forest(transformedTrees);
    }

    /**
     * The process π_ds takes a tree and transforms it into a forest. It either deletes the root
     * of the tree or changes its label and then recursively transforms the subtreees of the tree.
     */
    private Forest transformByDeleteAndSubstitute(Tree tree) {
        if (Math.random() < changeModel.delProb(tree.root().label())) {
            Forest f = new Forest();
            for (Tree subtree : tree.children()) {
                f = f.add(transformByDeleteAndSubstitute(subtree));
            }
            return f;
        } else {
            return new Forest(new Tree(
                    Node.elem(changeModel.randomLabel()), // random root
                    transform(new Forest(tree.children()))));
        }
    }

    // -------------------------------------------------------------

    /**
     * Convenience method
     */
    public double prob(Tree origTree, Tree transTree, TransformationProbabilities probs) {
        return prob(new Forest(origTree), new Forest(transTree), probs);
    }

    public double prob(Tree origTree, Tree transTree) {
        return prob(origTree, transTree, null);
    }

    /**
     * Convenience method
     */
    public double prob(Tree origTree, Forest transForest, TransformationProbabilities probs) {
        return prob(new Forest(origTree), transForest, probs);
    }

    public double prob(Tree origTree, Forest transForest) {
        return prob(origTree, transForest, null);
    }

    /**
     * Convenience method
     */
    public double prob(Forest origForest, Tree transTree, TransformationProbabilities probs) {
        return prob(origForest, new Forest(transTree), probs);
    }

    public double prob(Forest origForest, Tree transTree) {
        return prob(origForest, transTree, null);
    }

    /**
     * Calculates the prob of transforming a forest f1 into a forest f2, given insertion,
     * deletion and substitution operations and their probabilities, i.e. change model.
     *
     * Implements function DP_1(F_s, F_t), which calculates prob that P_θ(T|S), i.e.
     * that π(F_s) = F_t.
     *
     *   DP_1(F_s, F_t) = DP_2(F_s, F_t) +
     *                    P_ins(v) × DP_1(F_s, F_t - v)
     *
     * @param origForest Original tree.
     * @param transForest Transformed tree.
     * @return Probability of transforming original tree into transformed one. Result in the range of [0..1].
     */
    public double prob(Forest origForest, Forest transForest, TransformationProbabilities probs) {

        if (transForest.empty()) {
            return probWhenAllNodesWereDel(origForest);
        }

        if (substantiallyDifferent(origForest, transForest)) {
            return 0.0;
        }

        // TODO add Special Forest Optimization by Zhang and Shasha [edit dist trees]
        // TODO record Prob2[origForest][transForest] when origForest.isTree() & transForest.isTree() for ProbabilisticWrapper
        // TODO somehow record Prob1[origForest][transForest], ie how do we tell by the forest the prefix?
        double prob = probWhenLastTreeRootWasSub(origForest, transForest) +
                probWhenLastTreeRootWasIns(origForest, transForest);

        // record probabilities
        if (probs != null) {
            probs.put(origForest.toString(), transForest.toString(), prob);
        }

        return prob;
    }

    /**
     * Checks if optimization for similar trees can be applied.
     */
    private boolean substantiallyDifferent(Forest origForest, Forest transForest) {
        return Config.ENABLE_OPTIMIZATION_FOR_SIMILAR_TREES &&
                origForest.trees().size() == 1 &&
                transForest.trees().size() == 1 &&
                origForest.tree(0).substantiallyDifferentFrom(transForest.tree(0));
    }

    public double prob(Forest origForest, Forest transForest) {
        return prob(origForest, transForest, null);
    }

    /**
     * All nodes in the forest were deleted and then stopped.
     */
    private double probWhenAllNodesWereDel(Forest forest) {
        double prob = changeModel.stopProb();
        for (Tree tree : forest.trees()) {
            for (Tree node : PostOrderNavigator.sort(tree)) {
                prob *= changeModel.delProb(node.root().label());
            }
        }
        return prob;
    }

    /**
     * v in F_t was inserted and other trees were transformed:
     *   P_ins(v) × DP_1(F_s, F_t - v)
     */
    private double probWhenLastTreeRootWasIns(Forest origForest, Forest transForest) {
        return changeModel.insProb(transForest.rightmostTree().root().label()) *
                prob(origForest, transForest.removeRightmostRoot());
    }

    /**
     * Calculates the prob of transforming a tree f1 into a tree f2, given insertion,
     * deletion and substitution operations and their probabilities, i.e. change model.
     *
     * Implements function DP_2(F_s, F_t), which calculates prob that P_θ(T|S) when
     * the last tree root v of forest T was substituted, i.e. that π(F_s) = F_t and v was generated
     * by substitution under π.
     *
     *   DP_2(F_s, F_t) = P_sub(u, v) × DP_1(F_2 - [u], F_t - [v]) × DP_1(⌊u⌋, ⌊v⌋) +
     *                    P_del(u) × DP_2(F_s - u, F_t)
     *
     * @param f1 Original tree.
     * @param f2 Transformed tree.
     * @return Probability of transforming original tree into transformed one. Result in the range of [0..1].
     */
    public double probWhenLastTreeRootWasSub(Forest origForest, Forest transForest) {

        // there is no node in an empty tree, that can be substituted
        if (origForest.empty()) {
            return 0;
        }

        return probWhenRightmostRootsWereSub(origForest, transForest) +
                probWhenRightmostRootWasSubByNonRoot(origForest, transForest);
    }

    /**
     * The root u in F_s was deleted and v in F_t was substituted:
     *   P_del(u) × DP_2(F_s - u, F_t)
     */
    private double probWhenRightmostRootWasSubByNonRoot(Forest origForest, Forest transForest) {
        return changeModel.delProb(origForest.rightmostTree().root().label()) *
                probWhenLastTreeRootWasSub(origForest.removeRightmostRoot(), transForest);
    }

    /**
     * (1) u in F_s was substituted with v in F_t, (2) subtrees under u and v were transformed,
     * and (3) other trees in F_s and F_t were also transformed:
     *   P_sub(u, v) × DP_1(F_2 - [u], F_t - [v]) × DP_1(⌊u⌋, ⌊v⌋)
     */
    private double probWhenRightmostRootsWereSub(Forest origForest, Forest transForest) {
        return changeModel.subProb(
                    origForest.rightmostTree().root().label(),
                    transForest.rightmostTree().root().label()) *
                prob(origForest.removeRightmostTree(), transForest.removeRightmostTree()) *
                prob(origForest.rightmostTree().subforest(), transForest.rightmostTree().subforest());
    }

    // ------------------------------------------------------------------------

    public ProbabilisticChangeModel getChangeModel() {
        return changeModel;
    }
}
