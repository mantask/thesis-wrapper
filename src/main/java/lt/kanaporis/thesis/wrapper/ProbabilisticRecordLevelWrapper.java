package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.changemodel.ProbabilisticChangeModel;
import lt.kanaporis.thesis.region.DataRecord;
import lt.kanaporis.thesis.region.DataRecordLocator;
import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProbabilisticRecordLevelWrapper {

    private final ProbabilisticPageLevelWrapper globalWrapper;
    private final ProbabilisticPageLevelWrapper regionalWrapper;

    // --- ctor ----------------------------------------------------------

    /**
     * 1. Build probabilistic wrapper to locate candidate node in a new tree.
     * 2. Build a regional wrapper to locate candidate node inside single record.
     *    - Locate data records in original tree (that contain dist node)
     *    - Build a generic record tree by merging all data records inside original tree.
     *    - Create a regional wrapper for data record wrapping
     */
    public ProbabilisticRecordLevelWrapper(Tree tree, Tree distNode, ProbabilisticChangeModel changeModel) {
        globalWrapper = new ProbabilisticPageLevelWrapper(tree, distNode, changeModel);
        regionalWrapper = buildGeneralizedRegionalWrapper(tree, distNode, changeModel);
    }

    private ProbabilisticPageLevelWrapper buildGeneralizedRegionalWrapper(Tree tree, Tree distinguishedNode, ProbabilisticChangeModel changeModel) {
        Set<DataRecord> records = DataRecordLocator.locate(tree);
        DataRecord record = locateDataRegionWithDistinguishedNode(records, distinguishedNode);
        Forest generalizedRecord = mergeRegionsIntoBroom(record);
        return new ProbabilisticPageLevelWrapper(generalizedRecord.rightmostTree(), distinguishedNode, changeModel); // TODO forest vs tree wrapper
    }

    private DataRecord locateDataRegionWithDistinguishedNode(Set<DataRecord> records, Tree distinguishedNode) {
        for (DataRecord record : records) {
            for (Forest generalizedNode : record.generalizedNodes()) {
                for (Tree tagNode : generalizedNode.trees()) {
                    if (tagNode == distinguishedNode) {
                        return record;
                    }
                }
            }
        }
        return null;
    }

    private Forest mergeRegionsIntoBroom(DataRecord record) {
        Forest merged = null;
        for (Forest generalizedNode : record.generalizedNodes()) {
            merged = generalizedNode.merge(merged);
        }
        return merged;
    }

    // --- Wrapper -------------------------------------------------------

    /**
     * 1. Locate candidate node inside new page.
     * 2. Find data regions inside new page (with candidate node).
     * 3. Match data records with regional wrapper.
     */
    public Set<Node> wrap(Tree tree) {
        Set<Node> attributeNodes = new HashSet<>();
        for (Forest region : locateCandidateRegions(tree)) {
            attributeNodes.add(regionalWrapper.wrap(region.rightmostTree()).root()); // TODO Forest vs Tree
        }
        return attributeNodes;
    }

    private Set<Forest> locateCandidateRegions(Tree tree) {
        Tree candidateNode = globalWrapper.wrap(tree);
        Set<DataRecord> records = DataRecordLocator.locate(tree);
        DataRecord record = locateDataRegionWithDistinguishedNode(records, candidateNode);
        return record.generalizedNodes();
    }
}
