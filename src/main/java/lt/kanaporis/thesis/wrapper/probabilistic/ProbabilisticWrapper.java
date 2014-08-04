package lt.kanaporis.thesis.wrapper.probabilistic;

import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.wrapper.BaseWrapper;
import lt.kanaporis.thesis.wrapper.WrapperResult;

/**
 * Created by mantas on 8/1/14.
 */
public class ProbabilisticWrapper extends BaseWrapper {

    // TODO probabilities = new TreeMap<Node, Double>();
    private ProbabilisticTransducer probabilisticTransducer;

    @Override
    protected WrapperResult wrap(final Forest newTree) {

        Node bestGuessNode = null;
        double bestGuessProbability = 0.0;
        // TODO Set<Node, Double> candidates = new TreeSet<>();

        // Run TP(w, w') and record p_1v, p_2v for all v
        probabilisticTransducer.prob(oldTree, newTree);
        // TODO record prefixTransformationProbabilities.set(v, 0.123)

        // P_2 = w - (tree under u) – (getPrefix of u in w)
        Forest distinguishedNodeTail = oldTree.getTail(distinguishedNode);

        // Run TP(P_2, w') and record probabilities of P_2 → complete subtrees of w'
        probabilisticTransducer.prob(distinguishedNodeTail, newTree);
        // TODO record subtreeTransformationProbabilities.set(v, 0.123)

        // foreach v in w' do
        for (Node candidateNode : new PostOrderNavigator(newTree)) {

            // P'_2 = w' - (tree under v) - (getPrefix of v in w')
            Forest candidateNodeTail = newTree.getTail(candidateNode);

            // foreach z in P_2 in PostOrder do
            for (Node tailNode : new PostOrderNavigator(candidateNodeTail)) {

                // P''_2 = getPrefix of z in P_2
                Forest tailNodePrefix = candidateNodeTail.getPrefix(tailNode);

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

        return new WrapperResult(bestGuessNode.toString(), bestGuessProbability);
    }
}
