package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.changemodel.ProbabilisticChangeModel;
import lt.kanaporis.thesis.changemodel.ProbabilisticTransducer;
import lt.kanaporis.thesis.changemodel.TransformationProbabilities;
import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.PostOrderNavigator;
import lt.kanaporis.thesis.tree.Tree;

/**
 * Probabilistic page-level wrapper based on [Parameswaran'11].
 */
public class ProbabilisticPageLevelWrapper {

    private final ProbabilisticTransducer probabilisticTransducer;
    private final Tree oldTree;
    private final Tree distinguishedNode;
    private final Forest distinguishedNodePrefix;
    private final Tree distinguishedNodeTail;

    // TODO might want to ignore text nodes
    public ProbabilisticPageLevelWrapper(Tree oldTree, Tree distinguishedNode, ProbabilisticChangeModel changeModel) {
        this.probabilisticTransducer = new ProbabilisticTransducer(changeModel);
        this.oldTree = oldTree;
        this.distinguishedNode = distinguishedNode;
        this.distinguishedNodePrefix = oldTree.prefix(distinguishedNode.root());
        this.distinguishedNodeTail = oldTree.tail(distinguishedNode.root());
    }

    public Tree wrap(final Tree newTree) {

        // Initialize
        Tree bestGuessNode = null;
        double bestGuessProbability = 0.0;
        TransformationProbabilities transProbs = new TransformationProbabilities(probabilisticTransducer.getChangeModel());

        // pre-compute tree and subtree transformation probabilities
        probabilisticTransducer.prob(oldTree, newTree, transProbs);
        probabilisticTransducer.prob(distinguishedNodeTail, newTree, transProbs);

        // iterate through candidate nodes
        for (Tree candidateNode : PostOrderNavigator.sort(newTree)) {
            Forest candidateNodePrefix = newTree.prefix(candidateNode.root());
            Tree candidateNodeTail = newTree.tail(candidateNode.root());

            // pre-compute tail transformation probabilities
            for (Tree candidateTailTree : PostOrderNavigator.sort(candidateNodeTail)) {
                Forest tailNodePrefix = candidateNodeTail.prefix(candidateTailTree.root());
                probabilisticTransducer.prob(tailNodePrefix, candidateNodeTail, transProbs);
            }

            // Prob_tree = Prob_prefix × Prob_subtree × Prob_tail
            double candidateProbability = transProbs.get(distinguishedNodePrefix.toString(), candidateNodePrefix.toString()) *
                    transProbs.get(distinguishedNode.toString(), candidateNode.toString()) *
                    probabilisticTransducer.prob(distinguishedNodeTail, candidateNodeTail);

            // record the best guess so far
            if (candidateProbability > bestGuessProbability) {
                bestGuessProbability = candidateProbability;
                bestGuessNode = candidateNode;
                // TODO candidates.put(new Guess(candidateNode, candidateProbability))
            }
        }

        // TODO use content models to extract the best candidate from all probabilities
        // foreach candidate with highest prob: do content model test

        return bestGuessNode;
    }
}
