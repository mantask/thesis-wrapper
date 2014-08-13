package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.tree.DataRegionLocator;
import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Node;

import java.util.ArrayList;
import java.util.List;

public class ProbabilisticRecordWrapper {

    private final ProbabilisticPageWrapper boundaryWrapper;
    private final ProbabilisticPageWrapper regionWrapper;

    public ProbabilisticRecordWrapper(Node tree, Node distNode) {
        Node boundary = locateBoundary(distNode);
        List<Node> regions = DataRegionLocator.locate(boundary);
        Node region = locateRegionWithDistNode(regions, distNode);
        boundaryWrapper = new ProbabilisticPageWrapper(tree, boundary);
        regionWrapper = new ProbabilisticPageWrapper(region, distNode);
    }

    public List<Node> wrap(Node tree) {
        List<Node> attributeNodes = new ArrayList<>();
        Node boundary = boundaryWrapper.wrap(new Forest(tree));
        for (Node region : DataRegionLocator.locate(boundary)) {
            // TODO we could buildFromDom a record tree pattern and align that here
            attributeNodes.add(regionWrapper.wrap(new Forest(region)));
        }
        return attributeNodes;
    }

    private Node locateRegionWithDistNode(List<Node> regions, Node node) {
        for (Node region : regions) {
            if (region.hasChild(node)) {
                return region;
            }
        }
        return null;
    }

    /**
     * Find a boundary node in a tree, given an attribute node inside a record.
     */
    private Node locateBoundary(Node attrNode) {
        // TODO need smth better
        if (DataRegionLocator.locate(attrNode).isEmpty()) {
            return locateBoundary(attrNode.getParent());
        } else {
            return attrNode;
        }
    }

}
