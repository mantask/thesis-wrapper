package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.changemodel.ProbabilisticChangeModel;
import lt.kanaporis.thesis.region.DataRegionLocator;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class ProbabilisticRecordLevelWrapper {

    private final ProbabilisticPageLevelWrapper boundaryWrapper;
    private final ProbabilisticPageLevelWrapper regionWrapper;

    public ProbabilisticRecordLevelWrapper(Tree tree, Tree distNode, ProbabilisticChangeModel changeModel) {
        Tree boundary = locateBoundary(tree, distNode.root());
        List<Tree> regions = DataRegionLocator.locate(boundary);
        Tree region = locateRegionWithDistNode(regions, distNode.root());
        boundaryWrapper = new ProbabilisticPageLevelWrapper(tree, boundary, changeModel);
        regionWrapper = new ProbabilisticPageLevelWrapper(region, distNode, changeModel);
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
                // TODO might build a generalized region, see Broom Extraction in [Zheng'09]
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
