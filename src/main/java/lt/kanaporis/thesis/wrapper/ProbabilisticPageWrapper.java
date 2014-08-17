package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.changemodel.ProbabilisticTransducer;
import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.PostOrderNavigator;
import lt.kanaporis.thesis.tree.Tree;

import java.util.Map;

public class ProbabilisticPageWrapper {

    private final Tree oldTree;
    private final Node distinguishedNode;

    // TODO init these by transducer
    private Map<Node, Double> prefixTransformationProbs;
    private Map<Node, Double> subtreeTransformationProbs;


    // TODO probabilities = new TreeMap<Node, Double>();
    private ProbabilisticTransducer probabilisticTransducer;

    public ProbabilisticPageWrapper(Tree oldTree, Node distinguishedNode) {
        this.oldTree = oldTree;
        this.distinguishedNode = distinguishedNode;
    }

    public Tree wrap(final Tree newTree) {

        Tree bestGuessNode = null;
        double bestGuessProbability = 0.0;
        // TODO Set<Node, Double> candidates = new TreeSet<>();

        // Run TP(w, w') and record p_1v, p_2v for all v
        probabilisticTransducer.prob(new Forest(oldTree), new Forest(newTree));
        // TODO record prefixTransformationProbabilities.set(v, 0.123)

        // P_2 = w - (tree under u) – (getPrefix of u in w)
        Tree distinguishedNodeTail = oldTree.tail(distinguishedNode);

        // Run TP(P_2, w') and record probabilities of P_2 → complete subtrees of w'
        probabilisticTransducer.prob(distinguishedNodeTail, newTree);
        // TODO record subtreeTransformationProbabilities.set(v, 0.123)

        // foreach v in w' do
        for (Tree candidateNode : new PostOrderNavigator(newTree)) {

            // P'_2 = w' - (tree under v) - (getPrefix of v in w')
            Tree candidateNodeTail = newTree.tail(candidateNode.root());

            // foreach z in P_2 in PostOrder do
            for (Tree tailTree : new PostOrderNavigator(candidateNodeTail)) {

                // P''_2 = getPrefix of z in P_2
                Forest tailNodePrefix = candidateNodeTail.prefix(tailTree.root());

                // TP(P''_2, P'_2)
                probabilisticTransducer.prob(tailNodePrefix, candidateNodeTail);
                // TODO smth?

            }

            // p3v = TP(P_2, P'_2)
            double tailTransformationProb = probabilisticTransducer.prob(distinguishedNodeTail, candidateNodeTail);

            // Pr(v) = p_1v × p_2v × p_3v
            double candidateProbability = prefixTransformationProbs.get(candidateNode) *
                    subtreeTransformationProbs.get(candidateNode) *
                    tailTransformationProb;

            // record the best guess so far
            if (candidateProbability > bestGuessProbability) {
                bestGuessProbability = candidateProbability;
                bestGuessNode = candidateNode;
                // TODO candidates.put(candidateNode, candindateProbability)
            }
        }

        // TODO use content models to extract the best candidate from all probabilities
        // foreach candidate with highest prob: do content model test

        return bestGuessNode;
    }
}
