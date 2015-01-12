package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.changemodel.ProbabilisticChangeModel;
import lt.kanaporis.thesis.region.DataRegion;
import lt.kanaporis.thesis.region.DataRegionLocator;
import lt.kanaporis.thesis.tree.Forest;
import lt.kanaporis.thesis.tree.Tree;

import java.util.HashSet;
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
        Set<DataRegion> records = DataRegionLocator.locate(tree);
        DataRegion record = locateDataRegionWithDistinguishedNode(records, distinguishedNode);
        Forest generalizedRecord = mergeRegionsIntoBroom(record);
        return new ProbabilisticPageLevelWrapper(generalizedRecord.rightmostTree(), distinguishedNode, changeModel); // TODO forest vs tree wrapper. eg some fake parent?
    }

    private DataRegion locateDataRegionWithDistinguishedNode(Set<DataRegion> records, Tree distinguishedNode) {
        for (DataRegion record : records) {
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

    private Forest mergeRegionsIntoBroom(DataRegion record) {
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
    public Set<Tree> wrap(Tree tree) {
        Set<Tree> attributeNodes = new HashSet<>();
        for (Forest region : locateCandidateRegions(tree)) {
            attributeNodes.add(regionalWrapper.wrap(region.rightmostTree())); // TODO Forest vs Tree. eg some fake parent?
        }
        return attributeNodes;
    }

    private Set<Forest> locateCandidateRegions(Tree tree) {
        Tree candidateNode = globalWrapper.wrap(tree);
        Set<DataRegion> records = DataRegionLocator.locate(tree);
        DataRegion record = locateDataRegionWithDistinguishedNode(records, candidateNode);
        return record.generalizedNodes();
    }
}
