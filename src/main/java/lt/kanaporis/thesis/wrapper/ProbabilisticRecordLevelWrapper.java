package lt.kanaporis.thesis.wrapper;

import lt.kanaporis.thesis.changemodel.ProbabilisticChangeModel;
import lt.kanaporis.thesis.region.DataRecord;
import lt.kanaporis.thesis.region.DataRecordLocator;
import lt.kanaporis.thesis.tree.Node;
import lt.kanaporis.thesis.tree.Tree;

import java.util.ArrayList;
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
        Set<DataRecord> regions = DataRecordLocator.locate(tree);
        // TODO locate DatRegion w/ distinguishedNode
        Tree generalizedRegion = mergeRegionsIntoBroom(regions);
        return new ProbabilisticPageLevelWrapper(generalizedRegion, distinguishedNode, changeModel);
    }

    private Tree mergeRegionsIntoBroom(List<Tree> regions) {
        Tree merged = null;
        for (Tree region : regions) {
            merged = region.merge(merged);
        }
        return merged;
    }

    // --- Wrapper -------------------------------------------------------

    /**
     * 1. Locate candidate node inside new page.
     * 2. Find data regions inside new page (with candidate node).
     * 3. Match data records with regional wrapper.
     */
    public List<Node> wrap(Tree tree) {
        List<Node> attributeNodes = new ArrayList<>();
        for (Tree region : locateCandidateRegions(tree)) {
            attributeNodes.add(regionalWrapper.wrap(region).root());
        }
        return attributeNodes;
    }

    private List<Tree> locateCandidateRegions(Tree tree) {
        Tree candidateNode = globalWrapper.wrap(tree);
        return DataRecordLocator.locate(tree, candidateNode);
    }
}
