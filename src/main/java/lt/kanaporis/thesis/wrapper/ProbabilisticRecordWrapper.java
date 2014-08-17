package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.tree.DataRegionLocator;
import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class ProbabilisticRecordWrapper {

    private final ProbabilisticPageWrapper boundaryWrapper;
    private final ProbabilisticPageWrapper regionWrapper;

    public ProbabilisticRecordWrapper(Tree tree, Node distNode) {
        Tree boundary = locateBoundary(tree, distNode);
        List<Tree> regions = DataRegionLocator.locate(boundary);
        Tree region = locateRegionWithDistNode(regions, distNode);
        boundaryWrapper = new ProbabilisticPageWrapper(tree, boundary.root());
        regionWrapper = new ProbabilisticPageWrapper(region, distNode);
    }

    public List<Node> wrap(Tree tree) {
        List<Node> attributeNodes = new ArrayList<>();
        Tree boundary = boundaryWrapper.wrap(tree);
        for (Tree region : DataRegionLocator.locate(boundary)) {
            // TODO we could buildFromDom a record tree pattern and align that here
            attributeNodes.add(regionWrapper.wrap(region).root());
        }
        return attributeNodes;
    }

    private Tree locateRegionWithDistNode(List<Tree> regions, Node node) {
        for (Tree region : regions) {
            if (region.contains(node)) {
                return region;
            }
        }
        return null;
    }

    /**
     * Find a boundary node in a tree, given an attribute node inside a record.
     */
    private Tree locateBoundary(Tree tree, Node distNode) {
        // TODO need smth better
        /*
        if (DataRegionLocator.locate(attrNode).isEmpty()) {
            return locateBoundary(attrNode.getParent());
        } else {
            return attrNode;
        }
        */
        return null;
    }

}
